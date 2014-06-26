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

package net.awairo.mcmod.common.v1.util;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Optional;

import net.awairo.mcmod.common.v1.function.ConvertFunctions;

/**
 * Env constant.
 * 
 * @author alalwww
 */
public final class Env
{

    /**
     * 指定のキーに紐づくシステムプロパティを文字列値で取得.
     * 
     * @param key キー
     * @param defaultValue 設定されてなかった場合の初期値
     * @return プロパティ値もしくは初期値
     */
    public static String getString(String key, String defaultValue)
    {
        return getString(key).or(checkNotNull(defaultValue, "defaultValue"));
    }

    /**
     * 指定のキーに紐づくシステムプロパティを取得.
     * 
     * @param key キー
     * @return プロパティ値
     */
    public static Optional<String> getString(String key)
    {
        return getProperty(key);
    }

    /**
     * 指定のキーに紐づくシステムプロパティを真偽値で取得.
     * 
     * @param key キー
     * @param defaultValue 設定されてなかった場合の初期値
     * @return プロパティ値もしくは初期値
     */
    public static Boolean getBoolean(String key, Boolean defaultValue)
    {
        return getBoolean(key).or(checkNotNull(defaultValue, "defaultValue"));
    }

    /**
     * 指定のキーに紐づくシステムプロパティを取得.
     * 
     * @param key キー
     * @return プロパティ値
     */
    public static Optional<Boolean> getBoolean(String key)
    {
        return getProperty(key).transform(ConvertFunctions.TO_BOOLEAN);
    }

    /**
     * 指定のキーに紐づくシステムプロパティを整数値で取得.
     * 
     * @param key キー
     * @param defaultValue 設定されてなかった場合の初期値
     * @return プロパティ値もしくは初期値
     */
    public static Integer getInteger(String key, Integer defaultValue)
    {
        return getInteger(key).or(checkNotNull(defaultValue, "defaultValue"));
    }

    /**
     * 指定のキーに紐づくシステムプロパティを取得.
     * 
     * @param key キー
     * @return プロパティ値
     */
    public static Optional<Integer> getInteger(String key)
    {
        return getProperty(key).transform(ConvertFunctions.TO_INTEGER);
    }

    private static Optional<String> getProperty(String key)
    {
        return Optional.fromNullable(System.getProperty(checkNotNull(key, "key")));
    }

    private static final boolean DEVELOP = getBoolean("net.awairo.develop", Boolean.FALSE);
    private static final boolean DEBUG = getBoolean("net.awairo.debug", Boolean.FALSE);

    /**
     * @return trueは開発環境フラグが有効
     */
    public static boolean develop()
    {
        return DEVELOP;
    }

    /**
     * @return trueはデバッグフラグが有効
     */
    public static boolean debug()
    {
        return DEBUG;
    }

    private Env()
    {
        throw new InternalError();
    }
}
