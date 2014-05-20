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
