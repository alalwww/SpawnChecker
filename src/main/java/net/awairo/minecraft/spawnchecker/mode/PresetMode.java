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

import net.minecraft.util.ResourceLocation;

import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.hud.HudIconResource;

import lombok.NonNull;

abstract class PresetMode<T extends PresetMode<T>> implements Mode {
    private final Name name;
    private final HudIconResource icon;
    private final Priority priority;

    private boolean active = false;

    PresetMode(@NonNull Name name, @NonNull HudIconResource icon, @NonNull Priority priority) {
        this.name = name;
        this.icon = icon;
        this.priority = priority;
    }

    protected abstract void setUp();
    protected abstract void tearDown();

    @Override
    public final Name name() {
        return name;
    }

    @Override
    public ResourceLocation icon() {
        return icon.location();
    }

    @Override
    public final Priority priority() {
        return priority;
    }

    @Override
    public final boolean isActive() {
        return active;
    }

    @Override
    public final void activate(State state) {
        active = true;
        Mode.super.activate(state);
        setUp();
    }

    @Override
    public final void deactivate(State state) {
        tearDown();
        Mode.super.deactivate(state);
        active = false;
    }

    @Override
    public final boolean isInactive() {
        return Mode.super.isInactive();
    }

    @Override
    public final boolean isSelectable() {
        return Mode.super.isSelectable();
    }

    @Override
    public final boolean isConditional() {
        return Mode.super.isConditional();
    }
}
