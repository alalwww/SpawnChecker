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

import static com.google.common.base.Preconditions.*;
import static net.awairo.mcmod.spawnchecker.client.marker.RenderingSupport.*;

/**
 * 広い立方体の範囲を可視化するマーカーモデル.
 * 
 * <p>上面と下面には中心から広がる四角形を、側面には下から上に積み上げられた四角形を描画します。</p>
 * 
 * @author alalwww
 */
public class AreaMarkerModel extends SkeletalMarkerModel
{
    private double min;
    private double max;

    private double top;
    private double bottom;

    private double intervals;
    private int cycle;

    public void setMin(double min)
    {
        this.min = min;
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public void setTop(double top)
    {
        this.top = top;
    }

    public void setBottom(double bottom)
    {
        this.bottom = bottom;
    }

    public void setIntervals(double intervals)
    {
        this.intervals = intervals;
    }

    public void setCycle(int cycle)
    {
        checkArgument(cycle >= 0, "negative value(%s)", cycle);
        this.cycle = cycle;
    }

    public void setOffset(double offset)
    {
        min -= offset;
        max += offset;
        top += offset;
        bottom -= offset;
    }

    @Override
    public void render()
    {
        startDrawingLines();
        setGLColor(color);

        final double halfSize = (max - min) / 2;
        final double center = min + halfSize;
        final double tick = cycle != 0 ? ((tickCounts + partialTicks) % cycle) / (double) cycle : 1;

        double maxXZOffset = max - center;
        double xz;
        double y;

        // 底面の広がる四角形を描画
        for (xz = intervals * tick; xz <= maxXZOffset; xz += intervals)
        {
            final double minXZ = center - xz;
            final double maxXZ = center + xz;

            // north
            addVertex(minXZ, bottom, minXZ);
            addVertex(minXZ, bottom, maxXZ);

            // west
            addVertex(minXZ, bottom, maxXZ);
            addVertex(maxXZ, bottom, maxXZ);

            // south
            addVertex(maxXZ, bottom, maxXZ);
            addVertex(maxXZ, bottom, minXZ);

            // east
            addVertex(maxXZ, bottom, minXZ);
            addVertex(minXZ, bottom, minXZ);
        }

        // 縦に積み重なった四角形を描画
        for (y = bottom - maxXZOffset + xz; y <= top; y += intervals)
        {
            // north
            addVertex(min, y, min);
            addVertex(min, y, max);

            // west
            addVertex(min, y, max);
            addVertex(max, y, max);

            // south
            addVertex(max, y, max);
            addVertex(max, y, min);

            // east
            addVertex(max, y, min);
            addVertex(min, y, min);
        }

        // 上面の小さくなる四角形を描画
        for (xz = y - top; xz <= halfSize; xz += intervals)
        {
            final double minXZ = min + xz;
            final double maxXZ = max - xz;

            // north
            addVertex(minXZ, top, minXZ);
            addVertex(minXZ, top, maxXZ);

            // west
            addVertex(minXZ, top, maxXZ);
            addVertex(maxXZ, top, maxXZ);

            // south
            addVertex(maxXZ, top, maxXZ);
            addVertex(maxXZ, top, minXZ);

            // east
            addVertex(maxXZ, top, minXZ);
            addVertex(minXZ, top, minXZ);
        }

        draw();
    }

}
