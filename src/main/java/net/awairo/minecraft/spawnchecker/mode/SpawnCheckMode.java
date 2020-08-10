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

import java.util.LinkedList;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.Marker;
import net.awairo.minecraft.spawnchecker.api.PlayerPos;
import net.awairo.minecraft.spawnchecker.config.PresetModeConfig;
import net.awairo.minecraft.spawnchecker.hud.HudIconResource;
import net.awairo.minecraft.spawnchecker.mode.marker.SpawnPointMarker;

import lombok.extern.log4j.Log4j2;
import lombok.val;

@Log4j2
public class SpawnCheckMode extends SelectableMode {
    public static final String TRANSLATION_KEY = "spawnchecker.mode.spawnchecker";
    static final Name NAME = new Name(TRANSLATION_KEY);
    private static final Priority PRIORITY = new Priority(100);

    private final PresetModeConfig config;

    public SpawnCheckMode(PresetModeConfig config) {
        super(NAME, HudIconResource.SPAWN_CHECKER, PRIORITY);
        this.config = config;
    }

    @Override
    protected void setUp() {
        // なんかあるっけ？
    }

    @Override
    protected void tearDown() {
        // なんかあるっけ？
    }

    @Override
    public Stream<Marker> update(State modeState, PlayerPos playerPos) {
        val world = modeState.worldClient();
        val area = new ScanArea(playerPos, modeState.horizontalRange(), modeState.verticalRange());

        // TODO: ネザー、エンド対応
        // if (world.getDimension().isSurfaceWorld()) {
        if (world.func_239132_a_() instanceof net.minecraft.client.world.DimensionRenderInfo.Overworld) {
            return updateInSurfaceWorld(world, area);
        }

        return Stream.empty();
    }

    private Stream<Marker> updateInSurfaceWorld(ClientWorld world, ScanArea area) {

        val markerBuilder = SpawnPointMarker.builder()
            .endermanMarkerColor(Color.ofColorCode("#40FF0064"))
            .zombieSizeMobMarkerColor(Color.ofColorCode("#FFFF6464"))
            .spiderMarkerColor(Color.ofColorCode("#4040ff64"))
            .slimeMarkerColor(Color.ofColorCode("#50E8C964"))
            .ghastMarkerColor(Color.ofColorCode("#4040FF64"))
            .drawGuideline(config.drawGuideline());

        return area.xzStream().parallel().flatMap(xz -> {
            val placeType = PlacementType.ON_GROUND;
            val posIterator = xz.posStream().iterator();

            if (!posIterator.hasNext())
                return Stream.empty();

            val lightLevelThreshold = 7;
            BlockPos underLoc = null, loc;
            BlockState underBlock = null, locBlock;
            FluidState locFluid;

            boolean underIsSpawnableBlock = false;

            val list = new LinkedList<Marker>();

            // 現在位置が空気で、下に上面が平らのブロックがある座標を下から上に向かって探索
            // TODO: クソ雑ロジック見直す
            while (posIterator.hasNext()) {
                loc = posIterator.next();
                locBlock = world.getBlockState(loc);

                // region 足元のブロックがスポーン可能であることの判定
                // 上面が平らならスポーンできるブロック
                if (locBlock.isSolid()) {
                    // 岩盤とバリアブロックにはスポーンできない
                    underIsSpawnableBlock =
                        locBlock.getBlock() != Blocks.BEDROCK && locBlock.getBlock() != Blocks.BARRIER;
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                // 下がスポーンできるブロックじゃない場合は次のブロックへ
                if (!underIsSpawnableBlock) {
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                // もうこの判定はしないため先にリセット
                underIsSpawnableBlock = false;
                // endregion

                // region 現在の座標はなにもない空気ブロックであることの判定
                // WorldEntitySpawner#func_234968_a_[isValidEmptySpawnBlock](IBlockReader, BlockPos, BlockState, FluidState, EntityType) と同様の判定

                if (locBlock.isOpaqueCube(world, loc) || locBlock.canProvidePower() || locBlock.isIn(BlockTags.RAILS)) {
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                locFluid = world.getFluidState(loc);
                if (!locFluid.isEmpty()) {
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }
                // endregion

                // region 明るさ判定
                // net.minecraft.entity.monster.MonsterEntity#isValidLightLevel()
                if (world.getLightFor(LightType.BLOCK, loc) > lightLevelThreshold) {
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }
                // endregion

                if (EntitySize.ENDERMAN.isNotCollidingWithoutOtherEntityCollision(world, loc) &&
                    underBlock.canCreatureSpawn(world, underLoc, placeType, EntityType.ENDERMAN)) {
                    list.add(markerBuilder.buildEndermanMarker(loc, YOffset.of(locBlock, underBlock)));
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                if (EntitySize.ZOMBIE.isNotCollidingWithoutOtherEntityCollision(world, loc) &&
                    underBlock.canCreatureSpawn(world, underLoc, placeType, EntityType.ZOMBIE)) {
                    // ゾンビOK
                    list.add(markerBuilder.buildZombieSizeMobMarker(loc, YOffset.of(locBlock, underBlock)));
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                if (EntitySize.SPIDER.isNotCollidingWithoutOtherEntityCollision(world, loc) &&
                    underBlock.canCreatureSpawn(world, underLoc, placeType, EntityType.SPIDER)) {
                    // クモOK
                    list.add(markerBuilder.buildSpiderMarker(loc, YOffset.of(locBlock, underBlock)));
                    underLoc = loc;
                    underBlock = locBlock;
                    continue;
                }

                underLoc = loc;
                underBlock = locBlock;
            }

            return list.stream();
        });
    }
}
