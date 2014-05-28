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

package net.awairo.mcmod.spawnchecker.client.marker;

import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModel;
import net.awairo.mcmod.spawnchecker.client.marker.model.MobSpawnerSpawnArea;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.SpawnerVisualizerConfig;

/**
 * スポーナーのスポーンエリアマーカー.
 * 
 * @author alalwww
 */
public class MobSpawnerSpawnAreaMarker extends SkeletalMarker<MobSpawnerSpawnAreaMarker>
{
    /** スポーナーのスポーン範囲. */
    private final MobSpawnerSpawnArea model = new MobSpawnerSpawnArea();

    public MobSpawnerSpawnAreaMarker(SpawnerVisualizerConfig config)
    {
        model.setMin(-4, -1, -4);
        model.setMax(4, 2, 4);
        model.setOffset(0.01d);
    }

    @Override
    protected MarkerModel model()
    {
        model.setColor(argbColor);
        return model;
    }
}
