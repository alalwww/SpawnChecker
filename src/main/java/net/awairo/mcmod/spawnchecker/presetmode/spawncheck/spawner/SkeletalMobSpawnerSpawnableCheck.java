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

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.CopiedLogics;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.measuremententity.MeasurementEntities;

/**
 * @author alalwww
 */
abstract class SkeletalMobSpawnerSpawnableCheck implements MobSpawnerSpawnableCheck
{
    static ConstantsConfig consts()
    {
        return ConstantsConfig.instance();
    }

    static MeasurementEntities entities()
    {
        return MeasurementEntities.INSTANCE;
    }

    static CopiedLogics logics()
    {
        return CopiedLogics.INSTANCE;
    }

    @Override
    public final boolean supported()
    {
        return true;
    }
}
