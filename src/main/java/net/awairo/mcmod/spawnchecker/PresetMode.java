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

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.relauncher.Side;

import net.awairo.mcmod.common.v1.util.Fingerprint;
import net.awairo.mcmod.spawnchecker.client.ClientSideProxy;
import net.awairo.mcmod.spawnchecker.client.common.ColorConfig;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SpawnCheckerMode;

/**
 * SpawnChecker preset mode.
 * 
 * @author alalwww
 */
@Mod(
        modid = PresetMode.MOD_ID,
        certificateFingerprint = Fingerprint.VALUE)
public class PresetMode
{
    /** mod id. */
    public static final String MOD_ID = SpawnChecker.MOD_ID + ".presetmode";

    /** logger of the SpawnChecker. */
    private static final Logger logger = LogManager.getLogger(PresetMode.MOD_ID);

    ColorConfig color;

    @Mod.EventHandler
    private void handleModEvent(FMLFingerprintViolationEvent event)
    {
        Fingerprint.HANDLER.handle(MOD_ID, event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLPreInitializationEvent event)
    {
        if (isNotClient(event)) return;

        final Properties prop = event.getVersionProperties();

        // バージョンは本体と同期
        // version.propertiesにこっちのmodidを足してもいけるけど、汎用的にしておきたいのでコードで対応
        event.getModMetadata().version = prop.getProperty(SpawnChecker.MOD_ID + ".version");

        color = ((ClientSideProxy) SpawnChecker.sideProxy).settings().color();
    }

    @Mod.EventHandler
    private void handleModEvent(FMLInitializationEvent event)
    {
        if (isNotClient(event)) return;

        registerMode(SpawnCheckerMode.class);
    }

    private void registerMode(Class<? extends Mode> modeClass)
    {
        if (FMLInterModComms.sendMessage(SpawnChecker.MOD_ID, SpawnChecker.IMC_REGISTERMODE, modeClass.getName()))
        {
            logger.info("[IMC] send of SpawnChecker registerMode message was succeed.");
            return;
        }

        final String msg = "[IMC] has been failed to preset mode registering.";
        logger.error(msg);
        throw new RuntimeException(msg);
    }

    private static boolean isNotClient(FMLStateEvent event)
    {
        return event.getSide() != Side.CLIENT;
    }
}
