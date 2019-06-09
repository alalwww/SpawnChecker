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

package net.awairo.minecraft.spawnchecker.hud;

import net.minecraft.util.ResourceLocation;

import net.awairo.minecraft.spawnchecker.SpawnChecker;

import lombok.Getter;

public enum HudIconResource {
    SPAWN_CHECKER("spawn_checker.png"),
    SLIME_CHUNK_CHECKER("slime_chunk_visualizer.png"),
    SPAWNER_VISUALIZER("spawner_visualizer.png"),

    HORIZONTAL_RANGE("horizontal_range.png"),
    VERTICAL_RANGE("vertical_range.png"),

    BRIGHTNESS("brightness.png"),

    ;

    private static final String NAMESPACE = SpawnChecker.MOD_ID;
    private static final String DIRECTORY_NAME = "textures/hud";

    @Getter
    private final ResourceLocation location;

    HudIconResource(String name) {
        location = new ResourceLocation(NAMESPACE, DIRECTORY_NAME + "/" + name);
    }
}
