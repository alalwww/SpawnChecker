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

package net.awairo.mcmod.spawnchecker.client.model;

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

    private double bottom;
    private double top;

    private double intervals;
    private int cycle;

    /**
     * @param min X軸Z軸の負数方向の大きさ
     */
    public void setMin(double min)
    {
        this.min = min;
    }

    /**
     * @param max X軸Z軸の正数方向の大きさ
     */
    public void setMax(double max)
    {
        this.max = max;
    }

    /**
     * @param bottom Y軸の負数方向の大きさ
     */
    public void setBottom(double bottom)
    {
        this.bottom = bottom;
    }

    /**
     * @param top Y軸の正数方向の大きさ
     */
    public void setTop(double top)
    {
        this.top = top;
    }

    /**
     * @param intervals ラインの描画の間隔
     */
    public void setIntervals(double intervals)
    {
        this.intervals = intervals;
    }

    /**
     * @param cycle ラインアニメーションが1サイクルするまでに必要なtick
     */
    public void setCycle(int cycle)
    {
        checkArgument(cycle >= 0, "negative value(%s)", cycle);
        this.cycle = cycle;
    }

    /**
     * @param offset ラインの描画オフセット
     */
    public void setOffset(double offset)
    {
        min -= offset;
        max += offset;
        bottom -= offset;
        top += offset;
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
            addSquareVertex(center - xz, center + xz, bottom);

        // 縦に積み重なった四角形を描画
        for (y = bottom - maxXZOffset + xz; y <= top; y += intervals)
            addSquareVertex(min, max, y);

        // 上面の小さくなる四角形を描画
        for (xz = y - top; xz <= halfSize; xz += intervals)
            addSquareVertex(min + xz, max - xz, top);

        draw();
    }

    private void addSquareVertex(double minXZ, double maxXZ, double y)
    {
        // north　line
        addVertex(minXZ, y, minXZ);
        addVertex(minXZ, y, maxXZ);

        // west　line
        addVertex(minXZ, y, maxXZ);
        addVertex(maxXZ, y, maxXZ);

        // south　line
        addVertex(maxXZ, y, maxXZ);
        addVertex(maxXZ, y, minXZ);

        // east　line
        addVertex(maxXZ, y, minXZ);
        addVertex(minXZ, y, minXZ);
    }
}
