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

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.common.MinecraftForge;

import net.awairo.mcmod.common.v1.util.Fingerprint;

/**
 * SpawnChecker
 * 
 * @author alalwww
 */
@Mod(
        modid = SpawnChecker.MOD_ID,
        certificateFingerprint = Fingerprint.VALUE,
        guiFactory = "net.awairo.mcmod.spawnchecker.client.gui.GuiScreenFactory")
public final class SpawnChecker
{
    /** mod id. */
    public static final String MOD_ID = "spawnchecker";

    /** mode registration message key. */
    public static final String IMC_REGISTERMODE = "registerMode";

    static final String FINGERPRINT = "26e70d58815b73cd7c6c865fe091672f79070a35";

    /** logger of the SpawnChecker. */
    static final Logger LOGGER = LogManager.getLogger(SpawnChecker.MOD_ID);

    // ----------------------

    @SidedProxy(
            modId = SpawnChecker.MOD_ID,
            clientSide = "net.awairo.mcmod.spawnchecker.client.ClientSideProxy",
            serverSide = "net.awairo.mcmod.spawnchecker.server.ServerSideProxy")
    static Proxy sideProxy;

    // ----------------------

    @Mod.InstanceFactory
    static SpawnChecker newInstance()
    {
        return new SpawnChecker();
    }

    //------------------------

    private SpawnChecker()
    {
    }

    @Mod.EventHandler
    private void handleModEvent(FMLPreInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLPostInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(IMCEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLFingerprintViolationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    //------------------------

    /**
     * side proxy.
     * 
     * @author alalwww
     */
    public static abstract class Proxy
    {
        protected static final Logger LOGGER = SpawnChecker.LOGGER;

        protected final void handleModEvent(FMLFingerprintViolationEvent event)
        {
            Fingerprint.HANDLER.handle(MOD_ID, event);
        }

        protected void handleModEvent(FMLPreInitializationEvent event)
        {
            final Properties prop = event.getVersionProperties();

            LOGGER.info("initialization SpawnChecker version {} (hash:{})",
                    prop.getProperty(SpawnChecker.MOD_ID + ".version"),
                    prop.getProperty(SpawnChecker.MOD_ID + ".version.githash"));
        }

        protected void handleModEvent(FMLInitializationEvent event)
        {
        }

        protected void handleModEvent(FMLPostInitializationEvent event)
        {
        }

        protected final void handleModEvent(IMCEvent event)
        {
            LOGGER.debug("IMCEvent: %s", event.getMessages());

            for (IMCMessage message : event.getMessages())
            {
                // null値送信もできちゃうので、switchで落とさないように
                if (message.key == null)
                {
                    handleUnknownMessageKey(message);
                    continue;
                }

                handleMessage(message);
            }
        }

        protected void handleMessage(IMCMessage message)
        {
            // nothing
        }

        protected final void handleUnknownMessageKey(IMCMessage msg)
        {
            // いえ、知らない子ですね・・・
            LOGGER.warn("unexpected key from {} (key:{})", msg.getSender(), msg.key);
        }

        //------------------------

        protected static void registerEventListener(Manager manager)
        {
            registerFMLEventListener(manager.newFmlEventListener());
            registerForgeEventListener(manager.newForgeEventListener());
        }

        protected static void registerFMLEventListener(Object eventListener)
        {
            if (eventListener == null) return;
            FMLCommonHandler.instance().bus().register(eventListener);
        }

        protected static void registerForgeEventListener(Object eventListener)
        {
            if (eventListener == null) return;
            MinecraftForge.EVENT_BUS.register(eventListener);
        }
    }

    /**
     * Manager.
     * 
     * @author alalwww
     */
    public static abstract class Manager
    {
        protected static final Logger LOGGER = SpawnChecker.LOGGER;
        protected static final Minecraft GAME = Minecraft.getMinecraft();

        protected static Profiler profiler()
        {
            return GAME.mcProfiler;
        }

        protected Object newFmlEventListener()
        {
            return null;
        }

        protected Object newForgeEventListener()
        {
            return null;
        }
    }
}
