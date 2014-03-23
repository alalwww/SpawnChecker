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

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Optional;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;

import net.awairo.mcmod.spawnchecker.SpawnChecker;

/**
 * リフレクションを行う処理.
 */
public final class Refrection
{
    private static final Minecraft game = Minecraft.getMinecraft();
    private static final Logger LOGGER = LogManager.getLogger(SpawnChecker.MOD_ID);

    /**
     * 現在接続しているサーバーのアドレス情報を取得します.
     * 
     * @return サーバーのアドレス情報.
     */
    public static Optional<InetSocketAddress> getServerAddress()
    {
        checkState(game.theWorld != null, "world is not started");
        checkState(game.getIntegratedServer() == null, "current mode is the single player.");

        final NetHandlerPlayClient sendQueue = getFieldValue(
                WorldClient.class, game.theWorld, "sendQueue", ConstantsConfig.instance().sendQueueSrgName);

        if (sendQueue == null)
            return Optional.absent();

        final NetworkManager netManager = sendQueue.getNetworkManager();

        if (netManager.getSocketAddress() instanceof InetSocketAddress)
            return Optional.fromNullable((InetSocketAddress) netManager.getSocketAddress());

        if (LOGGER.isDebugEnabled() && netManager.getSocketAddress() != null)
            LOGGER.debug(netManager.getSocketAddress().getClass().getName());

        LOGGER.warn("not found InetSocketAddress");

        return Optional.absent();
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
