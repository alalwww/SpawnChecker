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

import java.io.File;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.config.Configuration;

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigHolder;
import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.controls.KeyConfig;

/**
 * spawn checker all settings.
 * 
 * @author alalwww
 */
public final class Settings extends ConfigHolder
{
    private final Config config;
    private final State state;

    /**
     * Constructor.
     * 
     * @param configFile 設定ファイル
     */
    public Settings(File configFile)
    {
        super(LogManager.getLogger(SpawnChecker.MOD_ID));

        config = Config.wrapOf(new Configuration(checkNotNull(configFile), true));
        add(new ConstantsConfig(config));
        add(new CommonConfig(config));
        add(new KeyConfig(config));
        add(new ColorConfig(config));

        setInterval(common().saveInterval.getInt());

        state = new State(common());
    }

    // ------------------------------

    /**
     * @return 設定
     */
    public Config config()
    {
        return config;
    }

    /**
     * @return 状態
     */
    public State state()
    {
        return state;
    }

    /**
     * @return 汎用設定
     */
    public CommonConfig common()
    {
        return get(CommonConfig.class);
    }

    /**
     * @return キー設定
     */
    public KeyConfig keyConfig()
    {
        return get(KeyConfig.class);
    }

    /**
     * @return 色設定
     */
    public ColorConfig color()
    {
        return get(ColorConfig.class);
    }
}
