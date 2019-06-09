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

package net.awairo.minecraft.spawnchecker.keybinding;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;

import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;
import net.awairo.minecraft.spawnchecker.mode.ModeState;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;

@Getter
@Log4j2
public final class KeyBindingState {

    @Getter(AccessLevel.NONE)
    private final Collection<SpawnCheckerKeyBinding> bindings;
    @Getter(AccessLevel.NONE)
    private final ModeState modeState;
    @Getter(AccessLevel.NONE)
    private final SpawnCheckerConfig config;

    private final SpawnCheckerKeyBinding prevMode;
    private final SpawnCheckerKeyBinding nextMode;

    private final SpawnCheckerKeyBinding prevModeOption;
    private final SpawnCheckerKeyBinding nextModeOption;

    private final SpawnCheckerKeyBinding horizontalRangePlus;
    private final SpawnCheckerKeyBinding horizontalRangeMinus;

    private final SpawnCheckerKeyBinding verticalRangePlus;
    private final SpawnCheckerKeyBinding verticalRangeMinus;

    private final SpawnCheckerKeyBinding brightnessPlus;
    private final SpawnCheckerKeyBinding brightnessMinus;

    public KeyBindingState(@NonNull ModeState modeState, @NonNull SpawnCheckerConfig config) {
        this.modeState = modeState;
        this.config = config;

        val bindings = new LinkedList<SpawnCheckerKeyBinding>();

        // FIXME: キーバインド処理の実装とリストへの登録

        prevMode = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("prevMode")
            .keyModifier(KeyModifier.CONTROL)
            .keyCode(GLFW.GLFW_KEY_UP)
            .ordinal(0)
            .build();
//        bindings.add(prevMode);

        nextMode = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("nextMode")
            .keyModifier(KeyModifier.CONTROL)
            .keyCode(GLFW.GLFW_KEY_DOWN)
            .ordinal(1)
            .build();
//        bindings.add(nextMode);

        prevModeOption = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("prevModeOption")
            .keyModifier(KeyModifier.NONE)
            .keyCode(GLFW.GLFW_KEY_UP)
            .ordinal(2)
            .build();
//        bindings.add(prevModeOption);

        nextModeOption = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("nextModeOption")
            .keyModifier(KeyModifier.NONE)
            .keyCode(GLFW.GLFW_KEY_DOWN)
            .ordinal(3)
            .build();
//        bindings.add(nextModeOption);

        horizontalRangePlus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("horizontalRangePlus")
            .keyModifier(KeyModifier.NONE)
            .keyCode(GLFW.GLFW_KEY_KP_ADD)
            .ordinal(4)
            .build();
        bindings.add(horizontalRangePlus);

        horizontalRangeMinus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("horizontalRangeMinus")
            .keyModifier(KeyModifier.NONE)
            .keyCode(GLFW.GLFW_KEY_KP_SUBTRACT)
            .ordinal(5)
            .build();
        bindings.add(horizontalRangeMinus);

        verticalRangePlus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("verticalRangePlus")
            .keyModifier(KeyModifier.CONTROL)
            .keyCode(GLFW.GLFW_KEY_KP_ADD)
            .ordinal(6)
            .build();
        bindings.add(verticalRangePlus);

        verticalRangeMinus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("verticalRangeMinus")
            .keyModifier(KeyModifier.CONTROL)
            .keyCode(GLFW.GLFW_KEY_KP_SUBTRACT)
            .ordinal(7)
            .build();
        bindings.add(verticalRangeMinus);

        brightnessPlus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("brightnessPlus")
            .keyModifier(KeyModifier.ALT)
            .keyCode(GLFW.GLFW_KEY_KP_ADD)
            .ordinal(8)
            .build();
//        bindings.add(brightnessPlus);

        brightnessMinus = new SpawnCheckerKeyBinding.Builder(this)
            .descriptionSuffix("brightnessMinus")
            .keyModifier(KeyModifier.ALT)
            .keyCode(GLFW.GLFW_KEY_KP_SUBTRACT)
            .ordinal(9)
            .build();
//        bindings.add(brightnessMinus);

        this.bindings = Collections.unmodifiableCollection(bindings);
    }

    RepeatDelay repeatDelay() {
        return config.keyConfig().repeatDelay();
    }

    RepeatRate repeatRate() {
        return config.keyConfig().repeatRate();
    }

    public Collection<KeyBinding> bindings() {
        return Collections.unmodifiableCollection(bindings);
    }

    public void onTick(long nowMilliTime) {
        bindings.parallelStream().forEach(s -> s.update(nowMilliTime));

        // mode
        while (prevMode().isPressed()) {
            modeState.proceedPrevMode();
        }
        while (nextMode().isPressed()) {
            modeState.proceedNextMode();
        }

        // mode option
        while (prevModeOption().isPressed()) {
            modeState.proceedPrevModeOption();
        }
        while (nextModeOption().isPressed()) {
            modeState.proceedNextModeOption();
        }

        // h range
        while (horizontalRangePlus().isPressed()) {
            modeState.proceedNextHorizontalRange();
        }
        while (horizontalRangeMinus().isPressed()) {
            modeState.proceedPrevHorizontalRange();
        }

        // v range
        while (verticalRangePlus().isPressed()) {
            modeState.proceedNextVerticalRange();
        }
        while (verticalRangeMinus().isPressed()) {
            modeState.proceedPrevVerticalRange();
        }

        // brightness
        while (brightnessPlus().isPressed()) {
            modeState.proceedNextBrightness();
        }
        while (brightnessMinus().isPressed()) {
            modeState.proceedPrevBrightness();
        }
    }
}

