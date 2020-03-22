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

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.awairo.minecraft.spawnchecker.api.UsingHands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SpawnCheckerConfig {

    @Setter(AccessLevel.PROTECTED)
    private CommentedConfig underlying;

    @Getter
    private final HudConfig hudConfig;
    @Getter
    private final KeyConfig keyConfig;
    @Getter
    private final ModeConfig modeConfig;

    @Getter
    private final PresetModeConfig presetModeConfig;

    public SpawnCheckerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(
            "",
            " SpawnChecker configurations.",
            "",
            "   https://github.com/alalwww/SpawnChecker",
            ""
        );
        builder.push(SpawnChecker.MOD_ID);

        enabledValue = builder
            .comment(
                " This value limits the behavior of all the SpawnChecker."
            )
            .translation(
                configGuiKey("enabled")
            )
            .define(
                "enabled",
                true
            );

        usingHandsValue = builder
            .comment(
                " Hands to use for item possession condition.",
                defaultValue(UsingHands.DEFAULT),
                allowValues(UsingHands.values())
            )
            .translation(
                configGuiKey("usingHands")
            )
            .defineEnum(
                "usingHands",
                UsingHands.DEFAULT
            );

        hudConfig = new HudConfig(this::update, builder);
        keyConfig = new KeyConfig(this::update, builder);
        modeConfig = new ModeConfig(this::update, builder);
        presetModeConfig = new PresetModeConfig(this::update, builder);

        builder.pop();
    }

    // region [config] Enabled

    private final BooleanValue enabledValue;

    public boolean enabled() { return enabledValue.get(); }

    public UpdateResult enable() { return update(enabledValue, true); }

    public UpdateResult disable() { return update(enabledValue, false); }

    // endregion

    // region [config] usingHands

    private final EnumValue<UsingHands> usingHandsValue;

    public UsingHands usingHand() { return usingHandsValue.get(); }

    public UpdateResult usingHand(UsingHands value) { return update(usingHandsValue, value); }

    // endregion

    // region utility methods

    private <T> UpdateResult update(ConfigValue<? super T> value, T newValue) {
        if (underlying != null && !Objects.equals(value.get(), newValue)) {
            underlying.update(value.getPath(), newValue);
            return UpdateResult.CHANGED;
        }
        return UpdateResult.NO_CHANGED;
    }

    private static String configGuiKey(String key) {
        return String.join(".", SpawnChecker.MOD_ID, "config", key);
    }

    static String configGuiKey(String path, String key) {
        return String.join(".", SpawnChecker.MOD_ID, "config", path, key);
    }

    static String defaultMinMax(Object defaultValue, Object min, Object max) {
        return String.format("  default: %s, min: %s, max: %s", defaultValue, min, max);
    }

    static <E extends Enum<E>> String defaultValue(E defaultValue) {
        return "  default: " + defaultValue.name();
    }

    static <E extends Enum<E>> String allowValues(E[] values) {
        return Arrays.stream(values)
            .map(E::name)
            .collect(Collectors.joining(", ", "  values: [", "]"));
    }

    // endregion

}
