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

    public final boolean override;

    public final double blockSize;
    public final int worldHeightMax;

    public final int scanRangeLimitMaxY;
    public final int scanRangeLimitMinY;

    public final int spawnableLightLevel;
    public final float spawnableMaxLightValue;

    public final long slimeRandomSeed;
    public final int chunkRandomSeedX1;
    public final int chunkRandomSeedX2;
    public final long chunkRandomSeedZ1;
    public final int chunkRandomSeedZ2;

    public final double slimeSpawnLimitY;

    public final int defaultMarkerListSize;

    public final int iconTextureUMin;
    public final int iconTextureVMin;
    public final int iconTextureUMax;
    public final int iconTextureVMax;

    public final double iconWidth;
    public final double iconHeight;

    public final int spacerOfIconAndMessage;

    public final double guiPosZ;

    public final int baseBrightness;
    public final int brightnessRatio;

    public final int[] ctrlKeyCodes;
    public final int[] shiftKeyCodes;
    public final int[] altKeyCodes;

    public static ConstantsConfig instance()
    {
        return instance;
    }

    /**
     * Constructor.
     * 
     * @param config 設定
     */
    public ConstantsConfig(Config config)
    {
        super(config);

        checkState(instance == null);
        instance = this;

        config.addCategoryComment("internal constant settings.\n *** DO NOT CHANGE IF YOU DO NOT NEED! ***");

        override = config.getValueOf("override", false)
                .comment("default: false")
                .getBoolean();

        blockSize = getValueOf("blockSize", 1d)
                .comment("default: 1.0")
                .getDouble();

        worldHeightMax = getValueOf("worldHeightMax", 255)
                .comment("default: 255")
                .getInt();

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

        slimeSpawnLimitY = getValueOf("slimeSpawnLimitY", 40)
                .comment("default: 40")
                .getDouble();

        // SpawnChecker
        defaultMarkerListSize = getValueOf("defaultMarkerListSize", 256)
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

    @SuppressWarnings("unused")
    private Prop getValueOf(String key, boolean defaultValue)
    {
        final Prop p = config.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    private Prop getValueOf(String key, int defaultValue)
    {
        final Prop p = config.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    private Prop getValueOf(String key, double defaultValue)
    {
        final Prop p = config.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    @SuppressWarnings("unused")
    private Prop getValueOf(String key, String defaultValue)
    {
        final Prop p = config.getValueOf(key, defaultValue);
        if (!override) p.set(defaultValue);
        return p;
    }

    @SuppressWarnings("unused")
    private Prop getListOf(String key, boolean... defaultValues)
    {
        final Prop p = config.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    private Prop getListOf(String key, int... defaultValues)
    {
        final Prop p = config.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @SuppressWarnings("unused")
    private Prop getListOf(String key, double... defaultValues)
    {
        final Prop p = config.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @SuppressWarnings("unused")
    private Prop getListOf(String key, String... defaultValues)
    {
        final Prop p = config.getListOf(key, defaultValues);
        if (!override) p.setList(defaultValues);
        return p;
    }

    @Override
    protected String configurationCategory()
    {
        return "internal";
    }
}
