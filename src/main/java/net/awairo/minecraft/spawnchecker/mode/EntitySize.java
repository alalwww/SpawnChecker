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

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
enum EntitySize {
    ENDERMAN(0.6F, 2.9F),
    ZOMBIE(0.6F, 1.95F),
    SKELETON(0.6F, 1.99F), // zombie より微妙にでかいので判定ではzombieサイズを使うようにした
    SPIDER(1.4F, 0.9F),
    SLIME(0.51000005F, 0.51000005F), // smallest size
    GHAST(4.0F, 4.0F),

    ;

    private final float width;
    private final float height;

    // EntityLiving#isNotColliding(IWorldReaderBase)
    boolean isNotColliding(ClientWorld worldIn, BlockPos pos) {
        val bb = boundingBox(pos);
        return !worldIn.containsAnyLiquid(bb)
            && worldIn.func_226665_a__(null, bb) // isCollisionBoxesEmpty
            && worldIn.checkNoEntityCollision(null, VoxelShapes.create(bb));
    }

    boolean isNotCollidingWithoutOtherEntityCollision(ClientWorld worldIn, BlockPos pos) {
        val bb = boundingBox(pos);
        return !worldIn.containsAnyLiquid(bb)
            && worldIn.func_226665_a__(null, bb); // isCollisionBoxesEmpty
    }

    AxisAlignedBB boundingBox(BlockPos pos) {
        return new AxisAlignedBB(
            (double) pos.getX(),
            (double) pos.getY(),
            (double) pos.getZ(),
            (double) pos.getX() + width,
            (double) pos.getY() + height,
            (double) pos.getZ() + width
        );
    }
}
