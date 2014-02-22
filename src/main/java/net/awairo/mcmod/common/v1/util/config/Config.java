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

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * {@link Configuration} の機能を制限するラッパークラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public final class Config
{
    final Configuration forgeConfig;

    /**
     * 新しい設定ラッパーを取得します.
     * 
     * @param congigurationFile 設定ファイル
     * @return 設定
     */
    public static Config wrapOf(File congigurationFile)
    {
        return wrapOf(congigurationFile, true);
    }

    /**
     * 新しい設定ラッパーを取得します.
     * 
     * @param congigurationFile 設定ファイル
     * @param caseSensitiveCustomCategories trueはカテゴリ名の大文字小文字を無視しません
     * @return 設定
     */
    public static Config wrapOf(File congigurationFile, boolean caseSensitiveCustomCategories)
    {
        return wrapOf(new Configuration(checkNotNull(congigurationFile), caseSensitiveCustomCategories));
    }

    /**
     * 新しい設定ラッパーを取得します.
     * 
     * @param forgeCongiguration
     * @return 設定
     */
    public static Config wrapOf(Configuration forgeCongiguration)
    {
        return new Config(checkNotNull(forgeCongiguration));
    }

    private Config(Configuration forgeConfig)
    {
        this.forgeConfig = forgeConfig;
        forgeConfig.load();
    }
}
