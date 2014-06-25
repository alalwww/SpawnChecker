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

package net.awairo.mcmod.spawnchecker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

/**
 * Manager.
 * 
 * @author alalwww
 */
public abstract class HandlerManager
{
    /**
     * ロガー.
     */
    protected static final Logger LOGGER = LogManager.getLogger(SpawnChecker.MOD_ID);

    /**
     * Minecraftインスタンス
     */
    protected static final Minecraft GAME = Minecraft.getMinecraft();

    /**
     * Minecraftのプロファイラーを取得します.
     * 
     * @return プロファイラー
     */
    protected static Profiler profiler()
    {
        return GAME.mcProfiler;
    }

    /**
     * FMLイベントのリスナーインスタンスを取得します.
     * 
     * @return FMLイベントのリスナー
     */
    protected Object newFmlEventListener()
    {
        return null;
    }

    /**
     * Forgeイベントのリスナーインスタンスを取得します.
     * 
     * @return Forgeイベントのリスナー
     */
    protected Object newForgeEventListener()
    {
        return null;
    }
}
