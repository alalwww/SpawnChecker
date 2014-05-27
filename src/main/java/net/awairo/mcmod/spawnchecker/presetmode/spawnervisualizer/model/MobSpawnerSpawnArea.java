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

package net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer.model;

import net.awairo.mcmod.spawnchecker.client.model.CubeModel;

/**
 * スポーナーのスポーン範囲の枠のモデル.
 * 
 * @author alalwww
 */
public final class MobSpawnerSpawnArea extends CubeModel
{
    private final CubeModel inner = new CubeModel();
    private final CubeModel outer = new CubeModel();

    @Override
    public void setMin(double x, double y, double z)
    {
        inner.setMin(x, y, z);
        outer.setMin(x, y, z);
    }

    @Override
    public void setMax(double x, double y, double z)
    {
        inner.setMax(x, y, z);
        outer.setMax(x, y, z);
    }

    @Override
    public void setOffset(double offset)
    {
        inner.setOffset(-offset);
        outer.setOffset(offset);
    }

    @Override
    public void setColor(int color)
    {
        inner.setColor(color);
        outer.setColor(color);
    }

    @Override
    public void setTicks(long tickCounts, float partialTicks)
    {
        inner.setTicks(tickCounts, partialTicks);
        outer.setTicks(tickCounts, partialTicks);
    }

    @Override
    public void render()
    {
        inner.render();
        outer.render();
    }
}
