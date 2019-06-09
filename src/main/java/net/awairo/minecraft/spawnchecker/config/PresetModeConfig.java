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
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

import lombok.NonNull;

import static net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig.*;

public final class PresetModeConfig {
    private static final String PATH = "preset_mode";

    private final Updater updater;

    PresetModeConfig(@NonNull Updater updater, @NonNull ForgeConfigSpec.Builder builder) {
        this.updater = updater;

        builder.comment(" Preset mode configurations");
        builder.push(PATH);

        drawGuideline = builder
            .comment(
                " True is drawing spawn check marker guidelines."
            )
            .translation(
                configGuiKey(PATH, "drawGuideline")
            )
            .define(
                "drawGuideline",
                false
            );
    }

    // region [preset_mode] guideline

    private final BooleanValue drawGuideline;

    public boolean drawGuideline() {
        return drawGuideline.get();
    }
    public UpdateResult guidelineOn() {
        return updater.update(drawGuideline, true);
    }
    public UpdateResult guidelineOff() {
        return updater.update(drawGuideline, false);
    }

    // endregion
}
