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

import java.util.Objects;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.properties.SlabType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum YOffset {
    DEFAULT(0.0d, 0.0d),
    SNOW_ON_TOP(0.1d, 0.0d),
    SINGLE_TOP_SLAB(0.0d, 0.5d),
    SINGLE_TOP_SLAB_SNOW_ON_TOP(SNOW_ON_TOP.topOffset, SINGLE_TOP_SLAB.bottomOffset),
    ;

    final double topOffset;
    final double bottomOffset;

    static YOffset of(IBlockState state, IBlockState underState) {
        if (snowOnTop(state))
            return singleTopSlab(underState) ? SINGLE_TOP_SLAB_SNOW_ON_TOP : SNOW_ON_TOP;
        return singleTopSlab(underState) ? SINGLE_TOP_SLAB : DEFAULT;
    }

    private static boolean snowOnTop(IBlockState state) {
        return Objects.equals(state.getBlock(), Blocks.SNOW);
    }

    private static boolean singleTopSlab(IBlockState underState) {
        return underState.getBlock() instanceof BlockSlab && underState.get(BlockSlab.TYPE) == SlabType.TOP;
    }

    @Override
    public String toString() {
        return "YOffset(top=" + topOffset + ",bottom=" + bottomOffset + ")";
    }
}
