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

import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModels;

/**
 * @author alalwww
 */
public class ChunkMarker extends SkeletalMarker<ChunkMarker>
{
    private double height;
    private double intervals;

    /** @param height the height to set */
    public ChunkMarker setHeight(double height)
    {
        this.height = height;
        return this;
    }

    /** @param intervals the intervals to set */
    public ChunkMarker setIntervals(double intervals)
    {
        this.intervals = intervals;
        return this;
    }

    @Override
    public void doRender(long tickCount, float partialTick)
    {
        MarkerModels.CHUNK.setHeight(height);
        MarkerModels.CHUNK.setIntervals(intervals);
        render(MarkerModels.CHUNK, tickCount, partialTick);
    }

}
