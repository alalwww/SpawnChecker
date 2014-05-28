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

package net.awairo.mcmod.spawnchecker.client.marker.model;

/**
 * スポーナーのスポーン数制限範囲マーカーモデル.
 * 
 * @author alalwww
 */
public final class MobSpawnerSpawnLimitArea extends AreaMarkerModel
{
    private AreaMarkerModel inner = new AreaMarkerModel();
    private AreaMarkerModel outer = new AreaMarkerModel();

    @Override
    public void setMin(double min)
    {
        inner.setMin(min);
        outer.setMin(min);
    }

    @Override
    public void setMax(double max)
    {
        inner.setMax(max);
        outer.setMax(max);
    }

    @Override
    public void setTop(double top)
    {
        inner.setTop(top);
        outer.setTop(top);
    }

    @Override
    public void setBottom(double bottom)
    {
        inner.setBottom(bottom);
        outer.setBottom(bottom);
    }

    @Override
    public void setIntervals(double intervals)
    {
        inner.setIntervals(intervals);
        outer.setIntervals(intervals);
    }

    @Override
    public void setCycle(int cycle)
    {
        inner.setCycle(cycle);
        outer.setCycle(cycle);
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
