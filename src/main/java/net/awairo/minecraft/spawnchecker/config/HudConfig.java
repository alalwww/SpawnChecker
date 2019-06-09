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

package net.awairo.minecraft.spawnchecker.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;

import net.awairo.minecraft.spawnchecker.api.HudData.ShowDuration;
import net.awairo.minecraft.spawnchecker.hud.HudOffset;

import lombok.NonNull;
import lombok.var;

import static net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig.*;

public final class HudConfig {
    private static final String PATH = "hud";

    private final Updater updater;

    HudConfig(@NonNull Updater updater, @NonNull ForgeConfigSpec.Builder builder) {
        this.updater = updater;

        builder.comment(" HUD configurations");
        builder.push(PATH);

        showDurationValue = builder
            .comment(
                " Hud show duration. (ms)",
                defaultMinMax(ShowDuration.DEFAULT.milliSeconds(), ShowDuration.MIN_VALUE, ShowDuration.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "showDuration")
            )
            .defineInRange(
                "showDuration",
                ShowDuration.DEFAULT::milliSeconds, ShowDuration.MIN_VALUE, ShowDuration.MAX_VALUE
            );

        xOffsetValue = builder
            .comment(
                " HUD position x topOffset.",
                defaultMinMax(HudOffset.DEFAULT_VALUE, HudOffset.MIN_VALUE, HudOffset.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "xOffset")
            )
            .defineInRange(
                "xOffset",
                HudOffset.X.DEFAULT::value, HudOffset.MIN_VALUE, HudOffset.MAX_VALUE
            );

        yOffsetValue = builder
            .comment(
                " HUD position y topOffset.",
                defaultMinMax(HudOffset.Y.DEFAULT.value(), HudOffset.MIN_VALUE, HudOffset.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "topOffset")
            )
            .defineInRange(
                "topOffset",
                HudOffset.Y.DEFAULT::value, HudOffset.MIN_VALUE, HudOffset.MAX_VALUE
            );
    }

    // region [hud] HudShowDuration

    private final LongValue showDurationValue;
    private ShowDuration showDurationCache = null;

    public ShowDuration showDuration() {
        var cache = showDurationCache;
        if (cache == null || cache.milliSeconds() != showDurationValue.get())
            cache = showDurationCache = new ShowDuration(showDurationValue.get());
        return cache;
    }

    // endregion

    private final IntValue xOffsetValue;
    private HudOffset.X xOffsetCache;

    public HudOffset.X xOffset() {
        var cache = xOffsetCache;
        if (cache == null || cache.value() != xOffsetValue.get())
            cache = xOffsetCache = HudOffset.xOf(xOffsetValue.get());
        return cache;
    }

    private final IntValue yOffsetValue;
    private HudOffset.Y yOffsetCache;

    public HudOffset.Y yOffset() {
        var cache = yOffsetCache;
        if (cache == null || cache.value() != yOffsetValue.get())
            cache = yOffsetCache = HudOffset.yOf(yOffsetValue.get());
        return cache;
    }
}
