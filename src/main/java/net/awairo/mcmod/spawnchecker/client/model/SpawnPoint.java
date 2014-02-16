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

import static net.awairo.mcmod.spawnchecker.client.model.RenderingSupport.*;

import com.google.common.base.Supplier;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;

/**
 * SpwnPoint.
 * 
 * @author alalwww
 */
public final class SpawnPoint extends Marker<SpawnPoint> implements Renderer
{
    static final double GUIDELINE_LENGTH = 64d;
    static final double DEFAULT_MARKER_SIZE = 0.35d;

    double innerBoxSizeOffset;
    boolean computed;
    boolean showGuideline;

    double outerMinX;
    double outerMinY;
    double outerMinZ;

    double outerMaxX;
    double outerMaxY;
    double outerMaxZ;

    double innerMinX;
    double innerMinY;
    double innerMinZ;

    double innerMaxX;
    double innerMaxY;
    double innerMaxZ;

    double guidelineTopX;
    double guidelineTopY;
    double guidelineTopZ;

    double guidelineBottomX;
    double guidelineBottomY;
    double guidelineBottomZ;

    double topOffset;

    double innerOffsetX;
    double innerOffsetY;
    double innerOffsetZ;

    @Override
    public void doRender(long tickCount, float partialTick)
    {
        computeVertics();

        double oMaxX = outerMaxX - renderManager.viewerPosX;
        double oMaxY = outerMaxY - renderManager.viewerPosY;
        double oMaxZ = outerMaxZ - renderManager.viewerPosZ;
        double oMinX = outerMinX - renderManager.viewerPosX;
        double oMinY = outerMinY - renderManager.viewerPosY;
        double oMinZ = outerMinZ - renderManager.viewerPosZ;
        double iMaxX = innerMaxX - renderManager.viewerPosX;
        double iMaxY = innerMaxY - renderManager.viewerPosY;
        double iMaxZ = innerMaxZ - renderManager.viewerPosZ;
        double iMinX = innerMinX - renderManager.viewerPosX;
        double iMinY = innerMinY - renderManager.viewerPosY;
        double iMinZ = innerMinZ - renderManager.viewerPosZ;

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
        addVertex(iMinX, iMinY, oMinZ);
        addVertex(iMinX, iMaxY, oMinZ);
        addVertex(iMaxX, iMaxY, oMinZ);
        addVertex(iMaxX, iMinY, oMinZ);
        // west
        addVertex(iMinX, iMinY, oMaxZ);
        addVertex(iMaxX, iMinY, oMaxZ);
        addVertex(iMaxX, iMaxY, oMaxZ);
        addVertex(iMinX, iMaxY, oMaxZ);
        // north
        addVertex(oMinX, iMinY, iMinZ);
        addVertex(oMinX, iMinY, iMaxZ);
        addVertex(oMinX, iMaxY, iMaxZ);
        addVertex(oMinX, iMaxY, iMinZ);
        // south
        addVertex(oMaxX, iMinY, iMinZ);
        addVertex(oMaxX, iMaxY, iMinZ);
        addVertex(oMaxX, iMaxY, iMaxZ);
        addVertex(oMaxX, iMinY, iMaxZ);

        draw();

        if (showGuideline)
        {
            final double topX = guidelineTopX - renderManager.viewerPosX;
            final double topY = guidelineTopY - renderManager.viewerPosY;
            final double topZ = guidelineTopZ - renderManager.viewerPosZ;

            final double botX = guidelineBottomX - renderManager.viewerPosX;
            final double botY = guidelineBottomY - renderManager.viewerPosY;
            final double botZ = guidelineBottomZ - renderManager.viewerPosZ;

            startDrawingLines();
            setGLColorAndBrightness(color, brightness);
            addVertex(topX, topY, topZ);
            addVertex(botX, botY, botZ);
            draw();
        }
    }

    /**
     * マーカーのサイズを指定します.
     * 
     * <p>
     * 事前にマーカーの表示位置を設定しておく必要があります
     * </p>
     * 
     * @param markerSize マーカーのサイズ
     * @return このインスタンス
     */
    public SpawnPoint setSize(double markerSize)
    {
        innerBoxSizeOffset = (ConstantsConfig.instance().blockSize - markerSize) / 2d;
        return this;
    }

    /**
     * 上面マーカーを浮かせます.
     * 
     * @param topOffset 上部マーカーのオフセット
     * @return このインスタンス
     */
    public SpawnPoint setTopMarkerOffset(double topOffset)
    {
        this.topOffset = topOffset;
        return this;
    }

    /**
     * マーカーの描画位置を中心からずらします.
     * 
     * @param offsetX x軸の移動距離
     * @param offsetY y軸の移動距離
     * @param offsetZ z軸の移動距離
     * @return このインスタンス
     */
    public SpawnPoint setInnerBoxOffset(double offsetX, double offsetY, double offsetZ)
    {
        this.innerOffsetX = offsetX;
        this.innerOffsetY = offsetY;
        this.innerOffsetZ = offsetZ;

        return this;
    }

    /**
     * ガイドラインを表示します.
     * 
     * @return このインスタンス
     */
    public SpawnPoint showGuideline(boolean showGuideline)
    {
        this.showGuideline = showGuideline;
        return this;
    }

    @Override
    public SpawnPoint reset()
    {
        super.reset();

        computed = false;
        showGuideline = false;

        topOffset = 0;

        innerOffsetX = 0;
        innerOffsetY = 0;
        innerOffsetZ = 0;

        return setSize(DEFAULT_MARKER_SIZE);
    }

    private void computeVertics()
    {
        if (computed) return;

        // マーカーは下のブロックに描くのでブロック分下げる

        double minX = x();
        double minY = y() - ConstantsConfig.instance().blockSize;
        double minZ = z();

        double maxX = minX + ConstantsConfig.instance().blockSize;
        double maxY = minY + ConstantsConfig.instance().blockSize;
        double maxZ = minZ + ConstantsConfig.instance().blockSize;

        // 元のブロックより大きなBoxを描かないと重なって描画が見えないので少しだけ厚みをもたせる
        final double thickness = 0.01d;

        outerMinX = minX - thickness;
        outerMinY = minY - thickness + topOffset;
        outerMinZ = minZ - thickness;
        outerMaxX = maxX + thickness;
        outerMaxY = maxY + thickness;
        outerMaxZ = maxZ + thickness;

        innerMinX = minX + innerBoxSizeOffset + innerOffsetX;
        innerMinY = minY + innerBoxSizeOffset + innerOffsetY;
        innerMinZ = minZ + innerBoxSizeOffset + innerOffsetZ;
        innerMaxX = maxX - innerBoxSizeOffset + innerOffsetX;
        innerMaxY = maxY - innerBoxSizeOffset + innerOffsetY;
        innerMaxZ = maxZ - innerBoxSizeOffset + innerOffsetZ;

        if (showGuideline)
        {
            guidelineBottomX = guidelineTopX = (innerMaxX + innerMinX) / 2.0d;
            guidelineBottomZ = guidelineTopZ = (innerMaxZ + innerMinZ) / 2.0d;

            guidelineBottomY = outerMaxY;
            guidelineTopY = Math.min(outerMaxY + GUIDELINE_LENGTH, ConstantsConfig.instance().worldHeightMax);
        }

        computed = true;
    }

    public static Supplier<SpawnPoint> supplier()
    {
        return new Supplier<SpawnPoint>()
        {
            @Override
            public SpawnPoint get()
            {
                return new SpawnPoint();
            }
        };
    }
}
