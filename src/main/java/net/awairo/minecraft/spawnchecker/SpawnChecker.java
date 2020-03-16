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

package net.awairo.minecraft.spawnchecker;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.client.Minecraft;

import net.awairo.minecraft.spawnchecker.config.ConfigHolder;
import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;
import net.awairo.minecraft.spawnchecker.mode.SpawnCheckMode;

import lombok.extern.log4j.Log4j2;
import lombok.val;

@Log4j2
@Mod(SpawnChecker.MOD_ID)
public final class SpawnChecker {

    public static final String MOD_ID = "spawnchecker";

    private final WrappedProfiler profiler;
    private final ConfigHolder configHolder;
    private final SpawnCheckerState state;

    public SpawnChecker() {
        log.info("SpawnChecker initializing.");

        val minecraft = Minecraft.getInstance();
        this.profiler = new WrappedProfiler(minecraft.getProfiler());

        val pair = new ForgeConfigSpec.Builder().configure(SpawnCheckerConfig::new);
        val config = pair.getLeft();
        val configSpec = pair.getRight();
        this.configHolder = new ConfigHolder(config);

        this.state = new SpawnCheckerState(minecraft, config);

        this.state.modeState()
            .add(new SpawnCheckMode(config.presetModeConfig()));
        // FIXME: not implemented X(
        //            .add(new SlimeCheckMode())
        //            .add(new SpawnerVisualizerMode());

        // register events

        ModLoadingContext.get()
            .registerConfig(Type.CLIENT, configSpec);

        val modBus = FMLJavaModLoadingContext.get().getModEventBus();

        val forgeBus = MinecraftForge.EVENT_BUS;

        // region Add event listeners
        // Mod lifecycle events
        modBus.addListener(this::onFMLCommonSetup);
        modBus.addListener(this::onFMLClientSetup);
        modBus.addListener(this::onFMLDedicatedServerSetup);
        modBus.addListener(this::onFMLLoadComplete);
        modBus.addListener(this::onFMLFingerprintViolation);

        // Mod config events
        modBus.addListener(this::onModConfigLoading);

        // IMC events
        modBus.addListener(this::onIMCEnqueue);
        modBus.addListener(this::onIMCProcess);

        forgeBus.addListener(this::onWorldLoad);
        forgeBus.addListener(this::onWorldUnload);

        // gui connecting hook
        forgeBus.addListener(this::onGuiOpenEvent);

        // Tick events
        forgeBus.addListener(this::onClientTick);
        forgeBus.addListener(this::onRenderTick);
        forgeBus.addListener(this::onRenderWorldLast);
        // endregion

        log.info("SpawnChecker initialized.");
    }

    // region [FML] Mod lifecycle events

    private void onFMLCommonSetup(FMLCommonSetupEvent event) {
        log.info("onFMLCommonSetup({})", event);
    }

    private void onFMLClientSetup(FMLClientSetupEvent event) {
        log.info("[spawnchecker] onFMLClientSetup({})", event);
        this.state.keyBindingStates().bindings()
            .forEach(ClientRegistry::registerKeyBinding);
    }

    private void onFMLDedicatedServerSetup(@SuppressWarnings("unused") FMLDedicatedServerSetupEvent event) {
        // not supported server mod
        log.warn("SpawnChecker is unsupported the Minecraft server.");
        throw new SpawnCheckerException("SpawnChecker is unsupported the Minecraft server.");
    }

    private void onFMLLoadComplete(FMLLoadCompleteEvent event) {
        state.initialize();
        log.info("[spawnchecker] onFMLLoadComplete({})", event);
    }

    private void onFMLFingerprintViolation(FMLFingerprintViolationEvent event) {
        // TODO: 未実装っぽい。
        log.error("[spawnchecker] onFMLFingerprintViolation({})", event);
        throw new SpawnCheckerException("FMLFingerprintViolation");
    }

    // endregion

    // region [FML] Mod config events

    private void onModConfigLoading(ModConfig.Loading event) {
        log.info("SpawnChecker config loading.");
        configHolder.loadConfig(event.getConfig());
        log.info("SpawnChecker config loaded.");
    }

    // endregion

    // region [FML] IMC events

    private void onIMCEnqueue(InterModEnqueueEvent event) {
        log.debug("onIMCEnqueue({})", event);
    }

    private void onIMCProcess(InterModProcessEvent event) {
        log.debug("onIMCProcess({})", event);
    }

    // endregion

    // region [Forge] World events

    private void onGuiOpenEvent(ClientChatEvent event) {
        if (!event.getMessage().startsWith("/spawnchecker"))
            return;

        if (state.commands().parse(event.getMessage())) {
            event.setCanceled(true);
            log.debug("cancel '/spawnchecker' command chat.");
        }
    }

    private void onWorldLoad(WorldEvent.Load event) {
        state.loadWorld(event.getWorld());
    }

    private void onWorldUnload(WorldEvent.Unload event) {
        state.unloadWorld(event.getWorld());
    }

    // endregion

    // region [Forge] Tick/Render events

    private void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.START) {
            state.onTickStart();
        }
        if (event.phase == Phase.END && state.started()) {
            profiler.startClientTick();
            state.onTickEnd();
            profiler.endClientTick();
        }
    }

    private void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == Phase.END && state.started()) {
            profiler.startRenderHud();
            state.renderHud(event.renderTickTime);
            profiler.endRenderHud();
        }
    }

    private void onRenderWorldLast(RenderWorldLastEvent event) {
        if (state.started()) {
            profiler.startRenderMarker();
            state.modeState().renderMarkers(event.getContext(), event.getPartialTicks(), event.getMatrixStack());
            profiler.endRenderMarker();
        }
    }

    // endregion

}
