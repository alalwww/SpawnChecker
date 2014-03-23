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
 * チャンクを描画するレンダラー.
 * 
 * @author alalwww
 */
public class ChunkMarkerModel extends SkeletalMarkerModel
{
    public static final ChunkMarkerModel INSTANCE = new ChunkMarkerModel();

    private final double minY = 0;
    private final double maxY = 32;

    private final double intervals = 1.5d;

    private final double chunkSizeMin = 0;
    private final double chunkSizeMax = 16;

    @Override
    public void render(long tickCount, float partialTick)
    {
        startDrawingLines();
        setColorAndBrightness(color, brightness);

        for (double y = minY; y <= maxY; y += intervals)
        {
            // north
            addVertex(chunkSizeMin, y, chunkSizeMin);
            addVertex(chunkSizeMin, y, chunkSizeMax);

            // west
            addVertex(chunkSizeMin, y, chunkSizeMax);
            addVertex(chunkSizeMax, y, chunkSizeMax);

            // south
            addVertex(chunkSizeMax, y, chunkSizeMax);
            addVertex(chunkSizeMax, y, chunkSizeMin);

            // east
            addVertex(chunkSizeMax, y, chunkSizeMin);
            addVertex(chunkSizeMin, y, chunkSizeMin);
        }

        draw();
    }

}
