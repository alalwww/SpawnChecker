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

import static net.awairo.mcmod.spawnchecker.client.marker.RenderingSupport.*;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;

/**
 * スポーン可能な場所描画用のモデル.
 * 
 * <p>
 * 大小2つの立方体の頂点座標を元に、ブロックの全面にマーカーを描画します。
 * 外側の立方体はブロックのサイズよりわずかに大きい立方体で、ブロックの表面に描画するための座標です。
 * 内側の立方体はマーカーの位置や四角形の大きさを決めるための座標となりますます。
 * </p>
 * 
 * @author alalwww
 */
public class SpawnPoint extends SkeletalMarkerModel
{
    /** 定数設定. */
    protected static final ConstantsConfig CONST = ConstantsConfig.instance();

    /** 外側の立方体の最小X/Z座標. */
    private final double oMin;
    /** 外側の立方体の最大X/Z座標. */
    private final double oMax;

    /** 上面のオフセット. */
    private double oMaxY;

    /** 上面のオフセット. */
    private final double oMinY;

    /** マーカーの四角の最小X座標. */
    protected double iMinX;
    /** マーカーの四角の最小Y座標. */
    protected double iMinY;
    /** マーカーの四角の最小Z座標. */
    protected double iMinZ;

    /** マーカーの四角の最大X座標. */
    protected double iMaxX;
    /** マーカーの四角の最大Y座標. */
    protected double iMaxY;
    /** マーカーの四角の最大Z座標. */
    protected double iMaxZ;

    SpawnPoint()
    {
        // 元のブロックより大きなBoxを描かないと重なって描画が見えないので少しだけ厚みをもたせる
        oMin = 0 - CONST.spawnPointMarkerThickness;
        oMax = CONST.blockSize + CONST.spawnPointMarkerThickness;

        // 1ブロック分下げる
        oMinY = oMin - CONST.blockSize;
        oMaxY = oMax - CONST.blockSize;

        setSize(markerSize());
    }

    /**
     * マーカーの大きさを取得します.
     * 
     * @return マーカーの大きさ
     */
    protected double markerSize()
    {
        return CONST.spawnPointMarkerSize;
    }

    /**
     * マーカーのサイズを指定します.
     * 
     * @param markerSize 0.0～1.0dの範囲のマーカーのサイズ
     */
    protected void setSize(double markerSize)
    {
        double innerBoxSizeOffset = (oMax - markerSize) / 2d;
        iMinX = iMinY = iMinZ = oMin + innerBoxSizeOffset;
        iMaxX = iMaxY = iMaxZ = oMax - innerBoxSizeOffset;

        // 1ブロック分下げる
        iMinY -= CONST.blockSize;
        iMaxY -= CONST.blockSize;
    }

    /**
     * 上面マーカーを浮かせるための値を設定します.
     * 
     * @param topOffset オフセット
     */
    public void setTopOffset(double topOffset)
    {
        oMaxY = oMax - CONST.blockSize + topOffset;
    }

    @Override
    public void render(long tickCount, float partialTick)
    {
        startDrawingQuads();
        setGLColorAndBrightness(color, brightness);

        // top
        addVertex(iMinX, oMaxY, iMinZ);
        addVertex(iMinX, oMaxY, iMaxZ);
        addVertex(iMaxX, oMaxY, iMaxZ);
        addVertex(iMaxX, oMaxY, iMinZ);

        // bottom
        addVertex(iMinX, oMinY, iMinZ);
        addVertex(iMaxX, oMinY, iMinZ);
        addVertex(iMaxX, oMinY, iMaxZ);
        addVertex(iMinX, oMinY, iMaxZ);

        // east
        addVertex(iMinX, iMinY, oMin);
        addVertex(iMinX, iMaxY, oMin);
        addVertex(iMaxX, iMaxY, oMin);
        addVertex(iMaxX, iMinY, oMin);

        // west
        addVertex(iMinX, iMinY, oMax);
        addVertex(iMaxX, iMinY, oMax);
        addVertex(iMaxX, iMaxY, oMax);
        addVertex(iMinX, iMaxY, oMax);

        // north
        addVertex(oMin, iMinY, iMinZ);
        addVertex(oMin, iMinY, iMaxZ);
        addVertex(oMin, iMaxY, iMaxZ);
        addVertex(oMin, iMaxY, iMinZ);

        // south
        addVertex(oMax, iMinY, iMinZ);
        addVertex(oMax, iMaxY, iMinZ);
        addVertex(oMax, iMaxY, iMaxZ);
        addVertex(oMax, iMinY, iMaxZ);

        draw();
    }

}
