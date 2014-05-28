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

package net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer;

import net.awairo.mcmod.spawnchecker.client.marker.SkeletalMarker;
import net.awairo.mcmod.spawnchecker.client.model.MarkerModel;
import net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer.model.MobSpawnerSpawnLimitArea;

/**
 * スポーナーのスポーン数制限範囲マーカー.
 * 
 * @author alalwww
 */
public class MobSpawnerSpawnLimitAreaMarker extends SkeletalMarker<MobSpawnerSpawnLimitAreaMarker>
{
    private final MobSpawnerSpawnLimitArea model = new MobSpawnerSpawnLimitArea();

    public MobSpawnerSpawnLimitAreaMarker(SpawnerVisualizerModeConfig config)
    {
        // TODO: 設定ロード
        model.setMin(-8.0d);
        model.setMax(9.0d);
        model.setTop(5.0d);
        model.setBottom(-4.0d);
        model.setIntervals(2.0d);
        model.setCycle(100);
        model.setOffset(0.01d);
    }

    @Override
    protected MarkerModel model()
    {
        model.setColor(argbColor);
        model.setTicks(tickCounts, partialTicks);
        return model;
    }
}
