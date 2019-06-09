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

import java.util.stream.Stream;

import net.awairo.minecraft.spawnchecker.api.Marker;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.PlayerPos;
import net.awairo.minecraft.spawnchecker.hud.HudIconResource;

public class SpawnerVisualizerMode extends ConditionalMode {
    static final String TRANSLATION_KEY = "spawnchecker.mode.spawnerVisualizer";
    static final Name NAME = new Name(TRANSLATION_KEY);
    static final Priority PRIORITY = new Priority(100);

    public SpawnerVisualizerMode() {
        super(NAME, HudIconResource.SPAWNER_VISUALIZER, PRIORITY);
    }

    @Override
    public boolean canActivate(PlayerPos playerPos, Mode.State state) {
        return false;
    }

    @Override
    public boolean canContinue(PlayerPos playerPos, Mode.State state) {
        return false;
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void tearDown() {

    }

    @Override
    public Stream<Marker> update(State modeState, PlayerPos playerPos) {
        return Stream.empty();
    }
}
