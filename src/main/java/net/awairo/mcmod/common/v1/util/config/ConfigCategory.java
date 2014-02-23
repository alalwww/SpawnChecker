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

import com.google.common.base.Strings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * 設定カテゴリを抽象化したクラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public abstract class ConfigCategory
{
    private static final String JOIN_FORMAT = "%s" + Configuration.CATEGORY_SPLITTER + "%s";
    final Config config;
    protected final String categoryName;

    // 初回は変更されてる前提で保存対象にするためtrue
    private boolean changed = true;

    /**
     * Constructor.
     * 
     * @param config config wrapper
     */
    protected ConfigCategory(Config config)
    {
        this(config, null);
    }

    /**
     * Constructor.
     * 
     * @param parent 親カテゴリ
     */
    protected ConfigCategory(ConfigCategory parent)
    {
        this(parent.config, parent.categoryName);
    }

    private ConfigCategory(Config config, String parentCategory)
    {
        this.config = checkNotNull(config, "config");
        this.categoryName = Strings.isNullOrEmpty(parentCategory)
                ? checkNotNull(configurationCategory())
                : String.format(JOIN_FORMAT, parentCategory, checkNotNull(configurationCategory()));
    }

    /**
     * カテゴリ名を取得します.
     * 
     * @return カテゴリ名
     */
    protected abstract String configurationCategory();

    /**
     * 設定変更済みフラグをたてます.
     */
    protected void setChangedFlag()
    {
        changed = true;
    }

    /**
     * 設定変更済みフラグをリセットします.
     */
    protected void clearChangedFlag()
    {
        changed = false;
    }

    /**
     * 設定変更済みフラグ値を取得します.
     * 
     * @return true はこのカテゴリの設定が変更されている事を意味する
     */
    protected boolean isSettingChanged()
    {
        return changed;
    }

    /**
     * カテゴリコメントを設定します
     * 
     * @param comment カテゴリコメント
     */
    protected final void setCategoryComment(String comment)
    {
        config.forgeConfig.addCustomCategoryComment(categoryName, comment);
    }

    /**
     * 真偽値型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValue 初期値
     * @return 設定値
     */
    protected Prop getValueOf(String key, boolean defaultValue)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValue));
    }

    /**
     * 真偽値リスト型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValues 初期値
     * @return 設定値
     */
    protected Prop getListOf(String key, boolean... defaultValues)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValues));
    }

    /**
     * 32bit整数値型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValue 初期値
     * @return 設定値
     */
    protected Prop getValueOf(String key, int defaultValue)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValue));
    }

    /**
     * 32bit整数値リスト型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValues 初期値
     * @return 設定値
     */
    protected Prop getListOf(String key, int... defaultValues)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValues));
    }

    /**
     * 64bit浮動小数点値型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValue 初期値
     * @return 設定値
     */
    protected Prop getValueOf(String key, double defaultValue)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValue));
    }

    /**
     * 64bit浮動小数点値リスト型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValues 初期値
     * @return 設定値
     */
    protected Prop getListOf(String key, double... defaultValues)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValues));
    }

    /**
     * 文字列値型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValue 初期値
     * @return 設定値
     */
    protected Prop getValueOf(String key, String defaultValue)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValue));
    }

    /**
     * 文字列値リスト型の設定値を取得します.
     * 
     * @param key 設定キー
     * @param defaultValues 初期値
     * @return 設定値
     */
    protected Prop getListOf(String key, String... defaultValues)
    {
        return wrap(config.forgeConfig.get(categoryName, key, defaultValues));
    }

    private Prop wrap(Property property)
    {
        return new Prop(property, this);
    }
}
