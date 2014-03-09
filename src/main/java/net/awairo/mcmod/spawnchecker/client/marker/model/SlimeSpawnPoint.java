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
 * スライムのスポーン可能な場所のモデル.
 * 
 * @author alalwww
 */
public class SlimeSpawnPoint extends SpawnPoint
{
    SlimeSpawnPoint()
    {
        super();

        // 中心からずらす
        iMinX += CONST.slimeMarkerOffset;
        iMinY += CONST.slimeMarkerOffset;
        iMinZ += CONST.slimeMarkerOffset;
        iMaxX += CONST.slimeMarkerOffset;
        iMaxY += CONST.slimeMarkerOffset;
        iMaxZ += CONST.slimeMarkerOffset;
    }

    @Override
    protected double markerSize()
    {
        return CONST.slimeSpawnPointMarkerSize;
    }
}
