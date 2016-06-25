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

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck.spawner;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * 管理可能エンティティ用の判定.
 * 
 * @author alalwww
 */
abstract class SkeletalAgeableSpawnableCheck extends SkeletalMobSpawnerSpawnableCheck
{
    abstract EntityAgeable measurementEntity();

    @Override
    public boolean isSpawnable(BlockPos pos)
    {
        // TODO：見直す
        if (Minecraft.getMinecraft().theWorld.getBlockState(pos.down()).getBlock() != Blocks.GRASS)
            return false;

        // TODO：空の明るさを見る、ブロックのblightnessを調べるほうの明るさ判定が必要
        if (logics().canSpawnByLightLevel(pos, consts().spawnableLightLevel))
            return false;

        if (logics().isColliding(pos, measurementEntity()))
            return false;

        return true;
    }

}
