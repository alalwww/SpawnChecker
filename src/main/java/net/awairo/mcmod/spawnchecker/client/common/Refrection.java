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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.MobSpawnerBaseLogic;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

import net.awairo.mcmod.spawnchecker.SpawnChecker;

/**
 * リフレクションを行う処理.
 */
public final class Refrection
{
    private static final Minecraft GAME = Minecraft.getMinecraft();
    private static final Logger LOGGER = LogManager.getLogger(SpawnChecker.MOD_ID);

    /**
     * 現在接続しているサーバーのアドレス情報を取得します.
     * 
     * @return サーバーのアドレス情報.
     */
    public static Optional<InetSocketAddress> getServerAddress()
    {
        checkState(GAME.world != null, "world is not started");
        checkState(GAME.getIntegratedServer() == null, "current mode is the single player.");

        final NetHandlerPlayClient sendQueue = getFieldValue(
                WorldClient.class, GAME.world, "sendQueue", ConstantsConfig.instance().sendQueueSrgName);

        if (sendQueue == null)
            return Optional.absent();

        final NetworkManager netManager = sendQueue.getNetworkManager();

        if (netManager.getRemoteAddress() instanceof InetSocketAddress)
            return Optional.fromNullable((InetSocketAddress) netManager.getRemoteAddress());

        if (LOGGER.isDebugEnabled() && netManager.getRemoteAddress() != null)
            LOGGER.debug(netManager.getRemoteAddress().getClass().getName());

        LOGGER.warn("not found InetSocketAddress");

        return Optional.absent();
    }

    private static volatile Method getEntityNameToSpawn;
    private static volatile boolean failed;
    private static final Object lock = new Object();

    public static String getEntityNameToSpawnFrom(MobSpawnerBaseLogic logic)
    {
        // TODO: コードがひどい、なおしたい
        if (failed) return "Pig";

        if (getEntityNameToSpawn == null)
        {
            synchronized (lock)
            {
                if (getEntityNameToSpawn == null)
                {
                    String[] names = { "getEntityNameToSpawn", ConstantsConfig.instance().getEntityNameToSpawnSrgName };
                    try
                    {
                        getEntityNameToSpawn = ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, null, names);
                    }
                    catch (RuntimeException ignore)
                    {
                        LOGGER.warn("refrection failed", ignore);
                        failed = true;
                        return "Pig";
                    }
                }
            }
        }

        try
        {
            return (String) getEntityNameToSpawn.invoke(logic);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            throw Throwables.propagate(e);
        }
    }

    private static <T, E> T getFieldValue(Class<? super E> clazz, E instance, String... names)
    {
        try
        {
            return ReflectionHelper.getPrivateValue(clazz, instance, names);
        }
        catch (RuntimeException ignore)
        {
            LOGGER.warn("refrection failed.", ignore);
            return null;
        }
    }

    private Refrection()
    {
        throw new InternalError();
    }
}
