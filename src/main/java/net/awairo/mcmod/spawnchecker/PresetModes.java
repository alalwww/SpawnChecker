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
import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SlimeChunkVisualizerMode;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SpawnCheckerMode;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.PresetModeConfigs;

/**
 * SpawnChecker preset mode.
 * 
 * @author alalwww
 */
@Mod(
        modid = PresetModes.MOD_ID,
        certificateFingerprint = Fingerprint.VALUE)
public class PresetModes
{
    /** mod id. */
    public static final String MOD_ID = SpawnChecker.MOD_ID + ".presetmode";

    /** logger of the SpawnChecker. */
    private static final Logger logger = LogManager.getLogger(PresetModes.MOD_ID);

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
        // version.propertiesにこっちのmodidを足してもいけるけど、生成処理は汎用的にしておきたいのでコードで対応
        event.getModMetadata().version = prop.getProperty(SpawnChecker.MOD_ID + ".version");

        // 本体の設定ファイルを使ってプリセットモード用の設定も生成
        final Settings settings = ((ClientSideProxy) SpawnChecker.sideProxy).settings();
        PresetMode.configs = new PresetModeConfigs(settings.mode());
        settings.add(PresetMode.configs.spawnCheckerMode);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLInitializationEvent event)
    {
        if (isNotClient(event)) return;

        registerMode(SpawnCheckerMode.class);
        registerMode(SlimeChunkVisualizerMode.class);
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

    /**
     * プリセットモードのスケルトン.
     * 
     * @author alalwww
     *
     * @param <M> プリセットモードのタイプ
     */
    public static abstract class PresetMode<M extends PresetMode<M>> extends ModeBase<M>
    {
        private static PresetModeConfigs configs;

        protected static PresetModeConfigs configs()
        {
            return configs;
        }

        protected PresetMode(String id, int ordinal)
        {
            super(id, ordinal);
        }
    }
}
