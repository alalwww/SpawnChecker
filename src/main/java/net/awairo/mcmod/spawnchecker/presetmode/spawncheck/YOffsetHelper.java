/*
 * SpawnChecker.
 * 
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;

/**
 * ブロックの上に設置されるスポーン判定に影響しないブロックの高さを求める処理.
 * 
 * @author alalwww
 */
public enum YOffsetHelper
{
    /** instance. */
    INSTANCE;

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final ImmutableMap<Block, YOffsetFunction> map;

    private YOffsetHelper()
    {
        // BlockBBのMaxYでは大きすぎるので定数より取得(踏んだときの接触判定のためだと思う)
        final YOffsetFunction pressurePlateOffset = fixedValueFunction(
                ConstantsConfig.instance().pressurePlateOffset);

        map = ImmutableMap.<Block, YOffsetFunction> builder()
                .put(Blocks.lever, new LeverOffset())
                .put(Blocks.snow_layer, new SnowOffset())
                .put(Blocks.carpet, fixedValueFunction(Blocks.carpet))
                .put(Blocks.rail, fixedValueFunction(Blocks.rail))
                .put(Blocks.detector_rail, fixedValueFunction(Blocks.detector_rail))
                .put(Blocks.golden_rail, fixedValueFunction(Blocks.golden_rail))
                .put(Blocks.activator_rail, fixedValueFunction(Blocks.activator_rail))
                .put(Blocks.wooden_pressure_plate, pressurePlateOffset)
                .put(Blocks.stone_pressure_plate, pressurePlateOffset)
                .put(Blocks.heavy_weighted_pressure_plate, pressurePlateOffset)
                .put(Blocks.light_weighted_pressure_plate, pressurePlateOffset)
                .build();
    }

    /**
     * 指定座標のブロックの種類からスポーンマーカーのY座標のオフセット値を取得.
     * 
     * @param x
     *            座標
     * @param y
     *            座標
     * @param z
     *            座標
     * @return オフセット値
     */
    public static double getYOffset(double x, double y, double z)
    {
        return INSTANCE.getYOffsetInternal(
                MathHelper.ceiling_double_int(x),
                MathHelper.ceiling_double_int(y),
                MathHelper.ceiling_double_int(z));
    }

    private double getYOffsetInternal(final int x, final int y, final int z)
    {
        final WorldClient world = minecraft.theWorld;
        final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();

        if (block != null)
        {
            final YOffsetFunction fn = map.get(block);

            if (fn != null)
                return fn.apply(world, x, y, z);
        }

        return 0.0D;
    }

    /**
     * 状態判定が必要なBlockの高さを求める関数.
     * 
     * @author alalwww
     */
    private interface YOffsetFunction
    {
        double apply(WorldClient world, int x, int y, int z);
    }

    /**
     * 固定値を返す関数を生成します.
     * 
     * @param block ブロック
     * @return 固定値を返す関数
     */
    private static YOffsetFunction fixedValueFunction(Block block)
    {
        final IBlockState dummyState = new BlockStateContainer.StateImplementation(
                Blocks.stone, ImmutableMap.<IProperty<?>, Comparable<?>>of()) {};
        return fixedValueFunction(block.getBoundingBox(dummyState, null, null).maxY);
    }

    /**
     * 固定値を返す関数を生成します.
     * 
     * @param value 値
     * @return 固定値を返す関数
     */
    private static YOffsetFunction fixedValueFunction(final double value)
    {
        return new YOffsetFunction()
        {
            @Override
            public double apply(WorldClient world, int x, int y, int z)
            {
                return value;
            }
        };
    }

    /**
     * レバーの高さ取得関数.
     * 
     * @author alalwww
     */
    private static class LeverOffset implements YOffsetFunction
    {
        // TODO: 定数化
        private static final double OFFSET = 0.18d;

        private static final Set<BlockLever.EnumOrientation> ON_BLOCK = EnumSet.of(
                BlockLever.EnumOrientation.UP_X,
                BlockLever.EnumOrientation.UP_Z);

        @Override
        public double apply(final WorldClient world, final int x, final int y, final int z)
        {
            return ON_BLOCK.contains(world.getBlockState(new BlockPos(x, y, z)).getValue(BlockLever.FACING)) ? OFFSET : 0;
        }
    }

    /**
     * (降り積もる方の)雪の高さ取得関数.
     * 
     * @author alalwww
     */
    private static class SnowOffset implements YOffsetFunction
    {
        @Override
        public double apply(WorldClient world, int x, int y, int z)
        {
            final BlockPos pos = new BlockPos(x, y, z);
            return Blocks.snow_layer.getBoundingBox(world.getBlockState(pos), world, pos).maxY;
        }

    }
}
