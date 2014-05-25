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

import java.awt.Color;

/**
 * マーカー.
 * 
 * @author alalwww
 * 
 * @param <T> このインターフェイスを実装したクラスのタイプ
 */
public interface Marker<T extends Marker<T>>
{
    /**
     * 座標を設定.
     * 
     * @param posX x
     * @param posY y
     * @param posZ z
     * @return this instance
     */
    T setPoint(double posX, double posY, double posZ);

    /**
     * 座標を設定.
     * 
     * @param posX x
     * @return this instance
     */
    T setPosX(double posX);

    /**
     * 座標を設定.
     * 
     * @param posY y
     * @return this instance
     */
    T setPosY(double posY);

    /**
     * 座標を設定.
     * 
     * @param posZ z
     * @return this instance
     */
    T setPosZ(double posZ);

    /**
     * マーカーの色と明るさを設定.
     * 
     * @param color color
     * @param brightness brightness
     * @return this instance
     */
    T setColorAndBrightness(Color color, int brightness);

    /**
     * マーカーの色を設定(ARGB).
     * 
     * @param color color
     * @return this instance
     */
    T setColor(int argbColor);

    /**
     * 状態をリセットします.
     */
    T reset();

    /**
     * 描画します.
     * 
     * @param tickCounts tick
     * @param partialTicks tick間の経過時間
     */
    void doRender(long tickCounts, float partialTicks);

}
