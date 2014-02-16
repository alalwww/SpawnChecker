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

package net.awairo.mcmod.spawnchecker.client.mode.core.measuremententity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

/**
 * 測定用エンティティ.
 * 
 * @author alalwww
 */
public final class MeasurementEntities
{
    public static MeasurementEntities of(World theWorld)
    {
        return new MeasurementEntities(theWorld);
    }

    public final EntityLiving standardSizeMob;
    public final EntityLiving enderman;
    public final EntityLiving spider;
    public final EntityLiving slime;
    public final EntityLiving silverFish;

    private MeasurementEntities(World world)
    {
        standardSizeMob = new StandardSizeMobMeasure(world);
        slime = new SlimeMeasure(world);
        enderman = new EndermanMeasure(world);
        spider = new SpiderMeasure(world);
        silverFish = new SilverfishMeasure(world);
    }

}
