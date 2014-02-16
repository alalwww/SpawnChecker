/*
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

/**
 * 設定カテゴリを抽象化したクラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public abstract class ConfigCategory
{
    /** configuration. */
    protected final Config config;

    // 初回は変更されてる前提で保存対象にするため
    private boolean changed = true;

    /**
     * Constructor.
     * 
     * @param config config wrapper
     */
    protected ConfigCategory(Config config)
    {
        this.config = checkNotNull(config, "config");
        config.settings = this;
        config.category = checkNotNull(configurationCategory());
    }

    protected void setChangedFlag()
    {
        changed = true;
    }

    /**
     * @return true はこのカテゴリの設定が変更されている事を意味する
     */
    protected boolean isSettingChanged()
    {
        return changed;
    }

    /**
     * 設定変更済みフラグをリセットします.
     */
    protected void clearChangedFlag()
    {
        changed = false;
    }

    /**
     * @return category name
     */
    protected abstract String configurationCategory();
}
