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

package net.awairo.mcmod.common.v1.util.config;

import static com.google.common.base.Preconditions.*;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * {@link Configuration} の機能を制限するラッパークラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public final class Config
{
    final Configuration forgeConfig;
    ConfigCategory settings;
    String category;

    public static Config wrapOf(Configuration forgeCongiguration)
    {
        return new Config(checkNotNull(forgeCongiguration));
    }

    private Config(Configuration forgeConfig)
    {
        this.forgeConfig = forgeConfig;
        forgeConfig.load();
    }

    public void addCategoryComment(String comment)
    {
        forgeConfig.addCustomCategoryComment(category, comment);
    }

    public Prop getValueOf(String key, boolean defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getListOf(String key, boolean... defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getValueOf(String key, int defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getListOf(String key, int... defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getValueOf(String key, double defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getListOf(String key, double... defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getValueOf(String key, String defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    public Prop getListOf(String key, String... defaultValue)
    {
        return wrap(forgeConfig.get(category, key, defaultValue));
    }

    private Prop wrap(Property property)
    {
        return new Prop(property, settings);
    }
}
