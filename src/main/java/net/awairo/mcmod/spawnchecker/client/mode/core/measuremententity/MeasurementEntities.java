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
 * 衝突判定に用いる測定用エンティティ.
 * 
 * @author alalwww
 */
public final class MeasurementEntities
{
    /**
     * 新しい測定用エンティティを生成します.
     * 
     * @param theWorld 現在のワールド
     * @return 新しいインスタンス
     */
    public static MeasurementEntities of(World theWorld)
    {
        return new MeasurementEntities(theWorld);
    }

    /** 標準的な大きさのMobエンティティ. */
    public final EntityLiving standardSizeMob;
    /** エンダーマン. */
    public final EntityLiving enderman;
    /** スパイダー. */
    public final EntityLiving spider;
    /** ケーブスパイダー. */
    public final EntityLiving caveSpider;
    /** スライム(最小サイズ). */
    public final EntityLiving slime;
    /** シルバーフィッシュ. */
    public final EntityLiving silverFish;

    /**
     * Constructor.
     * 
     * @param world 現在のワールド
     */
    private MeasurementEntities(World world)
    {
        standardSizeMob = new StandardSizeMobMeasure(world);
        enderman = new EndermanMeasure(world);
        spider = new SpiderMeasure(world);
        caveSpider = new CaveSpiderMeasure(world);
        slime = new SlimeMeasure(world);
        silverFish = new SilverfishMeasure(world);
    }

}
