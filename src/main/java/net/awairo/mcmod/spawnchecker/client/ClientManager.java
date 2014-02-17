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

package net.awairo.mcmod.spawnchecker.client;

import java.util.Map;

import com.google.common.collect.Maps;

import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.Settings;

/**
 * いろんな管理を行うクラスの抽象クラス.
 * 
 * @author alalwww
 */
public abstract class ClientManager extends SpawnChecker.Manager
{
    static final Map<Class<? extends ClientManager>, ClientManager> managers = Maps.newConcurrentMap();
    static Settings settings;

    /**
     * マネージャーのインスタンスを取得.
     * 
     * @param keyClass マネージャーのクラス
     * @return マネージャーのインスタンス
     */
    @SuppressWarnings("unchecked")
    static <T extends ClientManager> T get(Class<T> keyClass)
    {
        return (T) managers.get(keyClass);
    }

    /**
     * Constructor.
     */
    protected ClientManager()
    {
        managers.put(getClass(), this);
    }

    /**
     * @return 設定
     */
    protected Settings settings()
    {
        return settings;
    }
}
