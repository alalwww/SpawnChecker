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

package net.awairo.minecraft.spawnchecker.mode.marker;

import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;

import net.minecraft.util.math.Vec3d;

import net.awairo.minecraft.spawnchecker.api.Marker;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
abstract class FixedMarker implements Marker {

    private final Vec3d pos;

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("x", pos.x)
            .add("y", pos.y)
            .add("z", pos.z)
            .toString();
    }
}
