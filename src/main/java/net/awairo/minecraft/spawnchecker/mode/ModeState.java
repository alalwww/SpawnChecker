/*
 * SpawnChecker
 * Copyright (C) 2019 alalwww
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.awairo.minecraft.spawnchecker.mode;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;

import net.awairo.minecraft.spawnchecker.api.Brightness;
import net.awairo.minecraft.spawnchecker.api.HudData;
import net.awairo.minecraft.spawnchecker.api.HudData.ShowDuration;
import net.awairo.minecraft.spawnchecker.api.Marker;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.PlayerPos;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Horizontal;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Vertical;
import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import lombok.val;

@Log4j2
public class ModeState {
    private final ModeList modeList = new ModeList();

    private final Minecraft minecraft;
    private final SpawnCheckerConfig config;
    private final Consumer<HudData> hudDataRegistry;
    private final UpdateTimer timer;

    private List<Marker> markers = Collections.emptyList();

    @Nullable
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PRIVATE)
    private ClientWorld worldClient = null;

    private boolean worldClientLoaded() { return !worldClientNotLoaded(); }

    private boolean worldClientNotLoaded() { return worldClient == null; }

    @Getter
    private int tickCount = 0;

    public void initialize() {
        modeList.selectBy(config.modeConfig().selectedModeName());
    }

    private ShowDuration hudShowDuration() {
        return config.hudConfig().showDuration();
    }

    private Horizontal horizontalRange() {
        return config.modeConfig().horizontalRange();
    }

    private Vertical verticalRange() {
        return config.modeConfig().verticalRange();
    }

    private Brightness brightness() {
        return config.modeConfig().brightness();
    }

    public ModeState(Minecraft minecraft, SpawnCheckerConfig config, Consumer<HudData> hudDataRegistry) {
        this.minecraft = minecraft;
        this.config = config;
        this.hudDataRegistry = hudDataRegistry;
        this.timer = new UpdateTimer(config);
    }

    public void proceedNextMode() {
        modeList.scheduleChangeNextMode();
    }

    public void proceedPrevMode() {
        modeList.scheduleChangePrevMode();
    }

    public void proceedNextModeOption() {
        modeList.current().proceedNextOption(new ModeStateSnapshot());
    }

    public void proceedPrevModeOption() {
        modeList.current().proceedPrevOption(new ModeStateSnapshot());
    }

    public void proceedNextHorizontalRange() {
        if (config.modeConfig().horizontalRange(horizontalRange().next()).changed())
            registerScanRangeHud();
    }

    public void proceedPrevHorizontalRange() {
        if (config.modeConfig().horizontalRange(horizontalRange().prev()).changed())
            registerScanRangeHud();
    }

    public void proceedNextVerticalRange() {
        if (config.modeConfig().verticalRange(verticalRange().next()).changed())
            registerScanRangeHud();
    }

    public void proceedPrevVerticalRange() {
        if (config.modeConfig().verticalRange(verticalRange().prev()).changed())
            registerScanRangeHud();
    }

    public void proceedNextBrightness() {
        if (config.modeConfig().brightness(brightness().next()).changed()) {
            // TODO: Hud 描く
        }
    }

    public void proceedPrevBrightness() {
        if (config.modeConfig().brightness(brightness().prev()).changed()) {
            // TODO: Hud 描く
        }
    }

    public void loadWorldClient(ClientWorld loadedWorld) {
        if (worldClientLoaded()) {
            log.debug("World change. ({} -> {})", worldClient(), loadedWorld);
            unloadWorldClient(worldClient());
        }
        worldClient(loadedWorld);
        log.debug("Load world. ({})", loadedWorld);
    }

    public void unloadWorldClient(ClientWorld unloadingWorld) {
        if (worldClient() == unloadingWorld) { // because equals not implemented
            log.debug("Unload world. ({})", unloadingWorld);
            deactivateCurrentMode(new ModeStateSnapshot());
            worldClient(null);
            return;
        }
        log.debug("Skip unload world. (target={}, current={})", unloadingWorld, worldClient());
    }

    public void onTick(int tickCount, long nowMilliTime) {
        if (worldClientNotLoaded()) {
            if (!markers.isEmpty())
                markers = Collections.emptyList();
            return;
        }

        this.tickCount = tickCount;
        PlayerPos.of(minecraft).ifPresent(playerPos -> {
            val beforeMode = modeList.current();
            val state = new ModeStateSnapshot();
            if (modeList.updateList(playerPos, state).changed()) {
                log.info("mode changed. {} -> {}", beforeMode, modeList.current());

                if (beforeMode.isSelectable())
                    config.modeConfig().selectedModeName(modeList.current().name());
            }
            activateCurrentMode(state);
            if (timer.canUpdate(nowMilliTime)) {
                markers = modeList.current().update(state, playerPos)
                    .collect(Collectors.toList());
            }
        });
    }

    public void renderMarkers(WorldRenderer worldRenderer, float partialTicks) {
        if (worldClientNotLoaded())
            return;

        val renderer = new MyMarkerRendererImpl(
            worldRenderer,
            partialTicks,
            minecraft.getTextureManager(),
            minecraft.getRenderManager()
        );

        markers.forEach(m -> m.draw(renderer));
    }

    @SuppressWarnings("UnusedReturnValue")
    public ModeState add(Mode mode) {
        modeList.add(mode);
        return this;
    }

    private void activateCurrentMode(Mode.State state) {
        if (modeList.current().isInactive()) {
            log.info("Activate mode: '{}'", modeList.current().name());
            modeList.current().activate(state);
        }
    }

    private void deactivateCurrentMode(Mode.State state) {
        if (modeList.current().isActive()) {
            log.info("Deactivate mode: '{}'", modeList.current().name());
            modeList.current().deactivate(state);
        }
    }

    private void registerScanRangeHud() {
        hudDataRegistry.accept(new RangeConfigHudData(
            modeList.current().name(),
            modeList.current().icon(),
            horizontalRange(),
            verticalRange(),
            hudShowDuration()
        ));
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
            .add("tickCount", tickCount)
            .add("modeList.current.name", modeList.current().name())
            .add("markers.size", markers.size())
            .add("world", worldClient)
            .add("config", config)
            .toString();
    }

    @Value
    @RequiredArgsConstructor
    private final class ModeStateSnapshot implements Mode.State {
        ModeStateSnapshot() {
            this(
                ModeState.this.worldClient,
                ModeState.this.tickCount,
                ModeState.this.config.modeConfig().horizontalRange(),
                ModeState.this.config.modeConfig().verticalRange(),
                ModeState.this.config.modeConfig().brightness(),
                ModeState.this.config.hudConfig().showDuration(),
                ModeState.this.hudDataRegistry
            );
        }

        private final ClientWorld worldClient;
        private final int tickCount;
        private final Horizontal horizontalRange;
        private final Vertical verticalRange;
        private final Brightness brightness;
        private final ShowDuration hudShowDuration;
        private final Consumer<HudData> hudDataRegistry;
    }
}
