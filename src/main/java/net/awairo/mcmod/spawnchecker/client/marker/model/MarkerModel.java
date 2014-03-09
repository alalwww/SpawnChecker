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

import java.awt.Color;

/**
 * マーカーモデル.
 * 
 * <p>
 * 全てのモデルは再利用される可能性があります。
 * そのため個別にリセット処理が行われていない限りは、直前の描画状態を保持しています。
 * 描画を行う前に、常に最適な状態に設定する必要があります。
 * </p>
 * 
 * @author alalwww
 */
public interface MarkerModel
{
    /**
     * 描画色を設定します.
     * 
     * @param color
     */
    void setColor(Color color);

    /**
     * 0～255の範囲の明るさを設定します.
     * 
     * @param brightness
     */
    void setBrightness(int brightness);

    /**
     * レンダリングします.
     * 
     * @param tickCount tick count
     * @param partialTick render partial ticks
     */
    void render(long tickCount, float partialTick);
}
