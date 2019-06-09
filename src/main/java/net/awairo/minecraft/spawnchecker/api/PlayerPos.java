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

package net.awairo.minecraft.spawnchecker.api;

import java.util.Optional;
import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import lombok.Getter;
import lombok.NonNull;

public class PlayerPos {

    public static Optional<PlayerPos> of(@Nullable Minecraft mayBeMCInstance) {
        return Optional.ofNullable(mayBeMCInstance)
            .map(mc -> mc.player)
            .map(PlayerPos::new);
    }

    private final Vec3d underlying;

    @Getter(lazy = true)
    private final BlockPos blockPos = new BlockPos(underlying.x, underlying.y, underlying.z);

    public Vec3d get() {
        return underlying;
    }

    private PlayerPos(@NonNull EntityPlayer player) {
        this(player.getPositionVector());
    }

    @VisibleForTesting
    PlayerPos(@NonNull Vec3d playerPos) {
        this.underlying = playerPos;
    }

    private transient int hashCode;

    @Override
    public int hashCode() {
        int h = hashCode;
        if (h == 0)
            h = hashCode = underlying.hashCode();
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof PlayerPos && ((PlayerPos) obj).underlying.equals(underlying);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("x", underlying.x)
            .add("y", underlying.y)
            .add("z", underlying.z)
            .toString();
    }
}
