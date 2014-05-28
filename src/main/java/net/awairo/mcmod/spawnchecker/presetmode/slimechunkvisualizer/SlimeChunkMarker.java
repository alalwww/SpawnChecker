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

package net.awairo.mcmod.spawnchecker.presetmode.slimechunkvisualizer;

import net.awairo.mcmod.spawnchecker.client.marker.SkeletalMarker;
import net.awairo.mcmod.spawnchecker.client.model.ChunkMarkerModel;
import net.awairo.mcmod.spawnchecker.client.model.MarkerModel;

/**
 * チャンクマーカー.
 * 
 * @author alalwww
 */
public class SlimeChunkMarker extends SkeletalMarker<SlimeChunkMarker>
{
    private static final ChunkMarkerModel MODEL = new ChunkMarkerModel();
    private double height;
    private double intervals;
    private int cycle;

    /** @param height the height to set */
    public SlimeChunkMarker setHeight(double height)
    {
        this.height = height;
        return this;
    }

    /** @param intervals the intervals to set */
    public SlimeChunkMarker setIntervals(double intervals)
    {
        this.intervals = intervals;
        return this;
    }

    public SlimeChunkMarker setAnimationCycle(int cycle)
    {
        this.cycle = cycle;
        return this;
    }

    @Override
    protected MarkerModel model()
    {
        MODEL.setTop(height);
        MODEL.setIntervals(intervals);
        MODEL.setCycle(cycle);
        MODEL.setColor(argbColor);
        MODEL.setTicks(tickCounts, partialTicks);
        return MODEL;
    }

}
