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

/**
 * 立方体.
 * 
 * @author alalwww
 */
public class CubeModel extends SkeletalMarkerModel
{
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;

    protected CubeModel()
    {
    }

    public void setMin(double min)
    {
        setMin(min, min, min);
    }

    public void setMin(double minX, double minY, double minZ)
    {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    public void setMax(double max)
    {
        setMax(max, max, max);
    }

    public void setMax(double maxX, double maxY, double maxZ)
    {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void setOffset(double offset)
    {
        minX -= offset;
        minY -= offset;
        minZ -= offset;
        maxX += offset;
        maxY += offset;
        maxZ += offset;
    }

    @Override
    public void render()
    {
        startDrawingLines();
        setGLColor(color);

        // 下面の四角形
        addVertex(minX, minY, minZ);
        addVertex(minX, minY, maxZ);
        addVertex(minX, minY, maxZ);
        addVertex(maxX, minY, maxZ);
        addVertex(maxX, minY, maxZ);
        addVertex(maxX, minY, minZ);
        addVertex(maxX, minY, minZ);
        addVertex(minX, minY, minZ);
        // 側面の縦線 4本
        addVertex(minX, minY, minZ);
        addVertex(minX, maxY, minZ);
        addVertex(minX, minY, maxZ);
        addVertex(minX, maxY, maxZ);
        addVertex(maxX, minY, maxZ);
        addVertex(maxX, maxY, maxZ);
        addVertex(maxX, minY, minZ);
        addVertex(maxX, maxY, minZ);
        // 上面の四角形
        addVertex(minX, maxY, maxZ);
        addVertex(maxX, maxY, maxZ);
        addVertex(maxX, maxY, maxZ);
        addVertex(maxX, maxY, minZ);
        addVertex(maxX, maxY, minZ);
        addVertex(minX, maxY, minZ);
        addVertex(minX, maxY, minZ);
        addVertex(minX, maxY, maxZ);

        draw();
    }
}
