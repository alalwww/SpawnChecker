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

package net.awairo.mcmod.spawnchecker.client.mode.preset.config;

import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;

/**
 * プリセットモードの設定.
 * 
 * @author alalwww
 */
public class PresetModeConfigs
{
    /** スポーンチェッカーモードの設定. */
    public final SpawnCheckerConfig spawnCheckerMode;
    /** スライムチャンク可視化の設定. */
    public final SlimeChunkVisualizerConfig slimeChunkVisualizerMode;

    public PresetModeConfigs(ModeConfig config)
    {
        spawnCheckerMode = new SpawnCheckerConfig(config);
        slimeChunkVisualizerMode = new SlimeChunkVisualizerConfig(config);
    }

}
