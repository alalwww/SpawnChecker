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

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck.measuremententity;

import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

/**
 * シルバーフィッシュの測定用エンティティ.
 * 
 * @author alalwww
 */
final class SilverfishMeasure extends EntitySilverfish
{
    SilverfishMeasure()
    {
        super(null);
    }

    @Override
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        return false;
    }
}
