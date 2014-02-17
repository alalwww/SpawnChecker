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
public final class Settings
{
    private final State state;
    private final ConfigHolder holder;

    /**
     * Constructor.
     * 
     * @param configFile 設定ファイル
     */
    public Settings(File configFile)
    {
        final Config config = Config.wrapOf(new Configuration(checkNotNull(configFile), true));

        final ConstantsConfig consts = new ConstantsConfig(config);
        final CommonConfig common = new CommonConfig(config);
        state = new State(common);

        final KeyConfig key = new KeyConfig(config);

        final ColorConfig color = new ColorConfig(config);

        holder = new ConfigHolder(consts, common, key, color)
                .setLogger(LogManager.getLogger(SpawnChecker.MOD_ID))
                .setInterval(3000L);
    }

    // ------------------------------

    /**
     * @return 設定ホルダー
     */
    public ConfigHolder holder()
    {
        return holder;
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
        return holder.get(CommonConfig.class);
    }

    /**
     * @return キー設定
     */
    public KeyConfig keyConfig()
    {
        return holder.get(KeyConfig.class);
    }

    /**
     * @return 色設定
     */
    public ColorConfig color()
    {
        return holder.get(ColorConfig.class);
    }
}
