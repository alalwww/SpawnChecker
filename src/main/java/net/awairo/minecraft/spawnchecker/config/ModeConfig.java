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
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import net.awairo.minecraft.spawnchecker.api.Brightness;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Horizontal;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Vertical;
import net.awairo.minecraft.spawnchecker.mode.SpawnCheckMode;
import net.awairo.minecraft.spawnchecker.mode.UpdateTimer.Interval;

import lombok.NonNull;
import lombok.var;

import static net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig.*;

public final class ModeConfig {
    private static final String PATH = "mode";

    private final Updater updater;

    ModeConfig(@NonNull Updater updater, @NonNull ForgeConfigSpec.Builder builder) {
        this.updater = updater;

        builder.push(PATH);

        selectedModeNameValue = builder
            .comment(
                " Last selected mode id."
            )
            .translation(
                configGuiKey(PATH, "selectedMode")
            )
            .define(
                "selectedMode",
                SpawnCheckMode.TRANSLATION_KEY
            );

        checkIntervalValue = builder
            .comment(
                " Minimum scan interval. (ms)",
                defaultMinMax(Interval.DEFAULT.milliSeconds(), Interval.MIN_VALUE, Interval.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "scan.interval")
            )
            .defineInRange(
                "scan.interval",
                Interval.DEFAULT::milliSeconds, Interval.MIN_VALUE, Interval.MAX_VALUE
            );

        horizontalRangeValue = builder
            .comment(
                " Horizontal range of scan.",
                defaultMinMax(Horizontal.DEFAULT.value(), Horizontal.MIN_VALUE, Horizontal.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "scan.horizontalRange")
            )
            .defineInRange(
                "scan.horizontalRange",
                Horizontal.DEFAULT::value, Horizontal.MIN_VALUE, Horizontal.MAX_VALUE
            );

        verticalRangeValue = builder
            .comment(
                " Vertical range of scan.",
                defaultMinMax(Vertical.DEFAULT.value(), Vertical.MIN_VALUE, Vertical.MAX_VALUE)
            )
            .translation(
                configGuiKey(PATH, "scan.verticalRange")
            )
            .defineInRange(
                "scan.verticalRange",
                Vertical.DEFAULT::value, Vertical.MIN_VALUE, Vertical.MAX_VALUE
            );

        brightnessValue = builder
            .comment(
                " Marker brightness.",
                defaultValue(Brightness.DEFAULT),
                allowValues(Brightness.values())
            )
            .translation(
                configGuiKey(PATH, "marker.brightness")
            )
            .defineEnum(
                "marker.brightness",
                Brightness.DEFAULT
            );

        builder.pop();
    }

    // region Selected mode name

    private final ConfigValue<String> selectedModeNameValue;

    public Mode.Name selectedModeName() {
        return new Mode.Name(selectedModeNameValue.get());
    }

    public UpdateResult selectedModeName(Mode.Name name) {
        return updater.update(selectedModeNameValue, name.translationKey());
    }

    // endregion

    // region Check interval

    private final IntValue checkIntervalValue;
    private Interval checkIntervalValueCache;

    public Interval checkInterval() {
        var cache = checkIntervalValueCache;
        if (cache == null || cache.milliSeconds() != checkIntervalValue.get())
            checkIntervalValueCache = cache = Interval.ofMilliSeconds(checkIntervalValue.get());
        return cache;
    }

    // endregion

    // region Horizontal scan ranges

    private final IntValue horizontalRangeValue;

    public Horizontal horizontalRange() {
        return Horizontal.of(horizontalRangeValue.get());
    }

    public UpdateResult horizontalRange(Horizontal newValue) {
        return updater.update(horizontalRangeValue, newValue.value());
    }

    // endregion

    // region Vertical scan ranges

    private final IntValue verticalRangeValue;

    public Vertical verticalRange() {
        return Vertical.of(verticalRangeValue.get());
    }

    public UpdateResult verticalRange(Vertical newValue) {
        return updater.update(verticalRangeValue, newValue.value());
    }

    // endregion

    // region Brightness

    private final EnumValue<Brightness> brightnessValue;

    public Brightness brightness() {
        return brightnessValue.get();
    }

    public UpdateResult brightness(Brightness newValue) {
        return updater.update(brightnessValue, newValue);
    }

    // endregion

}
