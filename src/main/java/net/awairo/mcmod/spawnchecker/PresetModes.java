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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

import net.awairo.mcmod.common.v1.util.Fingerprint;
import net.awairo.mcmod.spawnchecker.client.ClientSideProxy;
import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.presetmode.SkeletalConfig;
import net.awairo.mcmod.spawnchecker.presetmode.slimechunkvisualizer.SlimeChunkVisualizerMode;
import net.awairo.mcmod.spawnchecker.presetmode.slimechunkvisualizer.SlimeChunkVisualizerModeConfig;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.SpawnCheckerMode;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.SpawnCheckerModeConfig;
import net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer.SpawnerVisualizerMode;
import net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer.SpawnerVisualizerModeConfig;

/**
 * SpawnChecker preset mode.
 * 
 * @author alalwww
 */
@Mod(
        modid = PresetModes.MOD_ID,
        version = "@VERSION@",
        certificateFingerprint = Fingerprint.VALUE)
public class PresetModes
{
    /** mod id. */
    public static final String MOD_ID = SpawnChecker.MOD_ID + ".presetmode";

    /** logger of the SpawnChecker. */
    private static final Logger LOGGER = LogManager.getLogger(PresetModes.MOD_ID);

    /** プリセットモードの設定. */
    private SpawnCheckerMode spawnCheckerMode;
    private SlimeChunkVisualizerMode slimeChunkVisualizerMode;
    private SpawnerVisualizerMode spawnerVisualizeMode;

    @Mod.InstanceFactory
    private static PresetModes newInstance()
    {
        return new PresetModes();
    }

    private PresetModes()
    {
    }

    @Mod.EventHandler
    private void handleModEvent(FMLFingerprintViolationEvent event)
    {
        Fingerprint.HANDLER.handle(MOD_ID, event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLPreInitializationEvent event)
    {
        if (isNotClientSide(event)) return;

        final Properties prop = event.getVersionProperties();

        // バージョンはSpawnChecker本体と同期
        // version.propertiesにこっちのmodidを足してもいけるけど、生成処理は汎用的にしておきたいのでコードで対応
        event.getModMetadata().version = prop.getProperty(SpawnChecker.MOD_ID + ".version");

        // 本体の設定ファイルを使ってプリセットモード用の設定も生成
        final Settings settings = ((ClientSideProxy) SpawnChecker.sideProxy).settings();

        spawnCheckerMode = new SpawnCheckerMode(
                addTo(settings, new SpawnCheckerModeConfig(settings.mode())));

        slimeChunkVisualizerMode = new SlimeChunkVisualizerMode(
                addTo(settings, new SlimeChunkVisualizerModeConfig(settings.mode())));

        spawnerVisualizeMode = new SpawnerVisualizerMode(
                addTo(settings, new SpawnerVisualizerModeConfig(settings.mode())));
    }

    private static <T extends SkeletalConfig> T addTo(Settings settings, T config)
    {
        settings.add(config);
        return config;
    }

    @Mod.EventHandler
    private void handleModEvent(FMLInitializationEvent event)
    {
        if (isNotClientSide(event)) return;

        if (FMLInterModComms.sendMessage(SpawnChecker.MOD_ID, SpawnChecker.IMC_HELLO, ""))
        {
            SpawnChecker.registerMode(spawnCheckerMode);
            SpawnChecker.registerMode(slimeChunkVisualizerMode);
            SpawnChecker.registerMode(spawnerVisualizeMode);

            LOGGER.info("[IMC] send of SpawnChecker registerMode message was succeed.");
            return;
        }

        final String msg = "[IMC] has been failed to preset mode registering.";
        LOGGER.error(msg);
        throw new RuntimeException(msg);
    }

    private static boolean isNotClientSide(FMLStateEvent event)
    {
        return event.getSide() != Side.CLIENT;
    }
}
