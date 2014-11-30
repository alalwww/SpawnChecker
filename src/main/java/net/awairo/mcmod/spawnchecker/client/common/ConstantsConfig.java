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

package net.awairo.mcmod.spawnchecker.client.common;

import static com.google.common.base.Preconditions.*;

import org.lwjgl.input.Keyboard;

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;

/**
 * 定数設定.
 * 
 * @author alalwww
 */
public class ConstantsConfig extends ConfigCategory
{
    private static ConstantsConfig instance;

    /** 上書き有効化フラグ. */
    private final boolean override;

    /** ブロックのサイズ. */
    public final double blockSize;
    /** ワールドのy座標の上限. */
    public final int worldHeightMax;

    /** 感圧板の高さ. */
    public final double pressurePlateOffset;

    /** 検索範囲のY座標の上限. */
    public final int scanRangeLimitMaxY;
    /** 検索範囲のY座標の下限. */
    public final int scanRangeLimitMinY;

    /** スポーン可判定する明るさ.(15段階の明るさのやつ) */
    public final int spawnableLightLevel;
    /** スポーン可判定する明るさ.(スポーナーの判定で使用してる実際の明るさとの比較用) */
    public final float spawnableMaxLightValue;

    /** スポーンマーカーの厚さ(ブロック表面から離す距離). */
    public final double spawnPointMarkerThickness;

    /** スポーンマーカーの大きさ. */
    public final double spawnPointMarkerSize;
    /** スライムスポーンマーカーの大きさ. */
    public final double slimeSpawnPointMarkerSize;

    /** スライムスポーンマーカーを中心からずらす距離. */
    public final double slimeMarkerOffset;

    /** スライムスポーンチャンクのランダムシード. */
    public final long slimeRandomSeed;
    /** チャンク生成用のシード値. */
    public final int chunkRandomSeedX1;
    /** チャンク生成用のシード値. */
    public final int chunkRandomSeedX2;
    /** チャンク生成用のシード値. */
    public final long chunkRandomSeedZ1;
    /** チャンク生成用のシード値. */
    public final int chunkRandomSeedZ2;

    /** スライムがスポーンする高さ上限. */
    public final int slimeSpawnLimitMaxY;
    /** 湿地でスライムがスポーンする高さ下限. */
    public final int slimeSpawnLimitMinYOnSwampland;
    /** 湿地でスライムがスポーンする高さ上限. */
    public final int slimeSpawnLimitMaxYOnSwampland;

    /** スライムチャンク判定結果のキャシュテーブルの行と列の上限. */
    public final int slimeChunkCacheSize;

    /** スライムチャンクマーカーのキャッシュ初期サイズ. */
    public final int slimeChunkMarkerCacheInitSize;

    /** WorldClient#sendQueue のsrg-name. */
    public final String sendQueueSrgName;

    /** MobSpawnerBaseLogic#getEntityNameToSpawn() の srg-name. */
    public final String getEntityNameToSpawnSrgName;

    /** CachedSupplierのキャッシュサイズ初期値. */
    public final int defaultCachedSupplierSize;

    /** スポーンチェッカーモードのマーカーインスタンスのキャッシュサイズ初期値. */
    public final int defaultSpawnCheckerMarkerListSize;

    /** 状態情報のアイコンのMinU. */
    public final int iconTextureUMin;
    /** 状態情報のアイコンのMaxU. */
    public final int iconTextureVMin;
    /** 状態情報のアイコンのMinV. */
    public final int iconTextureUMax;
    /** 状態情報のアイコンのMaxV. */
    public final int iconTextureVMax;

    /** 状態情報アイコンの幅. */
    public final double iconWidth;
    /** 状態情報アイコンの高さ. */
    public final double iconHeight;

    /** アイコンとメッセージの間隔. */
    public final int spacerOfIconAndMessage;

    /** 状態情報アイコンなどのZ座標. */
    public final double guiPosZ;

    /** ブライトネスの基本値. */
    public final int baseBrightness;
    /** ブライトネスの変化幅. */
    public final int brightnessRatio;

    /** CTRLキーとみなすキーコード. */
    public final int[] ctrlKeyCodes;
    /** SHIFTキーとみなすキーコード. */
    public final int[] shiftKeyCodes;
    /** ALTキーとみなすキーコード. */
    public final int[] altKeyCodes;

    /**
     * @return 定数クラスのインスタンス
     */
    public static ConstantsConfig instance()
    {
        return instance;
    }

    // CHECKSTYLE:OFF

    /**
     * Constructor.
     * 
     * @param config 設定
     */
    public ConstantsConfig(Config config)
    {
        super(config);
        setCategoryComment("internal constant settings.\n"
                + "*****************************************\n"
                + "*** DO NOT CHANGE IF YOU DO NOT NEED! ***\n"
                + "*****************************************");

        checkState(instance == null);
        instance = this;

        override = getValueOf("override", false)
                .comment("default: false")
                .getBoolean();

        blockSize = getValueOf("blockSize", 1d)
                .comment("default: 1.0")
                .getDouble();

        worldHeightMax = getValueOf("worldHeightMax", 255)
                .comment("default: 255")
                .getInt();

        pressurePlateOffset = getValueOf("pressurePlateOffset", 0.0625d)
                .comment("default: 0.0625d")
                .getDouble();

        scanRangeLimitMaxY = getValueOf("scanRangeLimitMaxY", 255)
                .comment("default: 255")
                .getInt();

        scanRangeLimitMinY = getValueOf("scanRangeLimitMinY", 1)
                .comment("default: 1")
                .getInt();

        // net.minecraft.entity.monster.EntityMob#isValidLightLevel()
        spawnableLightLevel = getValueOf("spawnableLightLevel", 8)
                .comment("default: 8")
                .getInt();

        // net.minecraft.entity.monster.EntityMob#getBlockPathWeight(int, int, int)
        spawnableMaxLightValue = (float) getValueOf("spawnableMaxLightValue", 0.5)
                .comment("default: 0.5")
                .getDouble();

        spawnPointMarkerThickness = getValueOf("spawnPointMarkerThickness", 0.01d)
                .comment("default: 0.01d")
                .getDouble();

        spawnPointMarkerSize = getValueOf("spawnPointMarkerSize", 0.35d)
                .comment("default: 0.35d")
                .getDouble();

        slimeSpawnPointMarkerSize = getValueOf("slimeSpawnPointMarkerSize", 0.2d)
                .comment("default: 0.2d")
                .getDouble();

        // スライムスポーン判定周りの定数
        // net.minecraft.entity.monster.EntitySlime#getCanSpawnHere()
        // see net.minecraft.world.chunk.Chunk#getRandomWithSeed(long)

        slimeRandomSeed = getValueOf("slimeRandomSeed", 987234911)
                .comment("default: 987234911")
                .getInt();

        chunkRandomSeedX1 = getValueOf("chunkRandomSeedX1", 4987142)
                .comment("default: 4987142")
                .getInt();

        chunkRandomSeedX2 = getValueOf("chunkRandomSeedX2", 5947611)
                .comment("default: 5947611")
                .getInt();

        chunkRandomSeedZ1 = getValueOf("chunkRandomSeedZ1", 4392871)
                .comment("default: 4392871")
                .getInt();

        chunkRandomSeedZ2 = getValueOf("chunkRandomSeedZ2", 389711)
                .comment("default: 389711")
                .getInt();

        slimeSpawnLimitMaxY = getValueOf("slimeSpawnLimitMaxY", 40)
                .comment("default: 40")
                .getInt();

        slimeSpawnLimitMinYOnSwampland = getValueOf("slimeSpawnLimitMinYOnSwampland", 50)
                .comment("default: 50")
                .getInt();

        slimeSpawnLimitMaxYOnSwampland = getValueOf("slimeSpawnLimitMaxYOnSwampland", 70)
                .comment("default: 70")
                .getInt();

        // スライムスポーン判定処理関連の定数
        slimeMarkerOffset = getValueOf("slimeMarkerOffset", 0.15d)
                .comment("default: 0.15d")
                .getDouble();

        slimeChunkCacheSize = getValueOf("slimeChunkCacheSize", 32)
                .comment("default: 32")
                .getInt();

        slimeChunkMarkerCacheInitSize = getValueOf("slimeChunkMarkerCacheInitSize", 256)
                .comment("default: 256")
                .getInt();

        // リフレクション
        sendQueueSrgName = getValueOf("sendQueueSrgName", "field_73035_a")
                .comment("default: field_73035_a (for 1.8-11.14.0.1245-1.8)")
                .getString();

        getEntityNameToSpawnSrgName = getValueOf("getEntityNameToSpawnSrgName", "func_98276_e")
                .comment("default: func_98276_e (for 1.8-11.14.0.1245-1.8)")
                .getString();

        // マーカーキャッシュのデフォルトサイズ
        defaultCachedSupplierSize = getValueOf("defaultCachedSupplierSize", 555)
                .comment("default: 555")
                .getInt();

        // SpawnChecker
        defaultSpawnCheckerMarkerListSize = getValueOf("defaultSpawnCheckerMarkerListSize", 256)
                .comment("default: 256")
                .getInt();

        // Information
        iconTextureUMin = getValueOf("iconTextureUMin", 0)
                .comment("default: 0")
                .getInt();
        iconTextureVMin = getValueOf("iconTextureVMin", 0)
                .comment("default: 0")
                .getInt();
        iconTextureUMax = getValueOf("iconTextureUMax", 1)
                .comment("default: 1")
                .getInt();
        iconTextureVMax = getValueOf("iconTextureVMax", 1)
                .comment("default: 1")
                .getInt();

        iconWidth = getValueOf("iconWidth", 8)
                .comment("default: 8")
                .getDouble();
        iconHeight = getValueOf("iconHeight", 8)
                .comment("default: 8")
                .getDouble();

        spacerOfIconAndMessage = getValueOf("spacerOfIconAndMessage", 2)
                .comment("default: 2")
                .getInt();

        guiPosZ = getValueOf("guiPosZ", 1.0d)
                .comment("default: 1.0")
                .getDouble();

        // marker rendering
        baseBrightness = getValueOf("baseBrightness", 180)
                .comment("default: 180")
                .getInt();

        brightnessRatio = getValueOf("brightnessRatio", 10)
                .comment("default: 10")
                .getInt();

        ctrlKeyCodes = getListOf("control.ctrlKeyCodes", Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL)
                .comment("default: " + Keyboard.KEY_LCONTROL + ", " + Keyboard.KEY_RCONTROL)
                .getIntList();

        shiftKeyCodes = getListOf("control.shiftKeyCodes", Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT)
                .comment("default: " + Keyboard.KEY_LSHIFT + ", " + Keyboard.KEY_RSHIFT)
                .getIntList();

        altKeyCodes = getListOf("control.altKeyCodes", Keyboard.KEY_LMENU, Keyboard.KEY_RMENU)
                .comment("default: " + Keyboard.KEY_LMENU + ", " + Keyboard.KEY_RMENU)
                .getIntList();
    }

    // CHECKSTYLE:ON

    // ---------------------------

    @Override
    protected String configurationCategory()
    {
        return "internal";
    }

    // ---------------------------

    @Override
    protected Prop getValueOf(String key, boolean defaultValue)
    {
        final Prop p = super.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    @Override
    protected Prop getValueOf(String key, int defaultValue)
    {
        final Prop p = super.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    @Override
    protected Prop getValueOf(String key, double defaultValue)
    {
        final Prop p = super.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    @Override
    protected Prop getValueOf(String key, String defaultValue)
    {
        final Prop p = super.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    // ---------------------------

    @Override
    protected Prop getListOf(String key, boolean... defaultValues)
    {
        final Prop p = super.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @Override
    protected Prop getListOf(String key, int... defaultValues)
    {
        final Prop p = super.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @Override
    protected Prop getListOf(String key, double... defaultValues)
    {
        final Prop p = super.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @Override
    protected Prop getListOf(String key, String... defaultValues)
    {
        final Prop p = super.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }
}
