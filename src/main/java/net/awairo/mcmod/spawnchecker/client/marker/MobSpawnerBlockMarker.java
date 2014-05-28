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
import net.awairo.mcmod.spawnchecker.client.marker.model.MobSpawnerBlockOutline;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.SpawnerVisualizerConfig;

/**
 * スポーナー可視化モードが有効な場合にスポーナーブロックに描画する枠線.
 * 
 * @author alalwww
 */
public class MobSpawnerBlockMarker extends SkeletalMarker<MobSpawnerBlockMarker>
{
    /** スポーナーブロックの外周. */
    private final MobSpawnerBlockOutline model = new MobSpawnerBlockOutline();

    public MobSpawnerBlockMarker(SpawnerVisualizerConfig config)
    {
        // TODO: 設定ロード
        model.setOffset(0.01d);
    }

    @Override
    protected MarkerModel model()
    {
        model.setColor(argbColor);
        return model;
    }

}
