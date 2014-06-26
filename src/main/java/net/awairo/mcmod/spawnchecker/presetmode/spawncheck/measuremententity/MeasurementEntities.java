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

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;

/**
 * 衝突判定に用いる測定用エンティティ.
 * 
 * @author alalwww
 */
public enum MeasurementEntities
{
    /** instance. */
    INSTANCE;

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

    /** ゴーレム. */
    public final EntityLiving golem;

    /** 鶏. */
    public final EntityAgeable chicken;
    /** 牛. */
    public final EntityAgeable cow;
    /** 馬. */
    public final EntityAgeable horse;
    /** 猫. */
    public final EntityAgeable ocelot;
    /** 豚. */
    public final EntityAgeable pig;
    /** 羊. */
    public final EntityAgeable sheep;
    /** 狼. */
    public final EntityAgeable wolf;

    /** 村人. */
    public final EntityAgeable villager;

    private MeasurementEntities()
    {
        standardSizeMob = new StandardSizeMobMeasure();
        enderman = new EndermanMeasure();
        spider = new SpiderMeasure();
        caveSpider = new CaveSpiderMeasure();
        slime = new SlimeMeasure();
        silverFish = new SilverfishMeasure();

        golem = new GolemMeasure();

        chicken = new ChickenMeasure();
        cow = new CowMeasure();
        horse = new HorseMeasure();
        ocelot = new OcelotMeasure();
        pig = new PigMeasure();
        sheep = new SheepMeasure();
        wolf = new WolfMeasure();

        villager = new VillagerMeasure();
    }

}
