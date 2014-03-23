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
    public abstract T setPoint(double posX, double posY, double posZ);

    /**
     * 座標を設定.
     * 
     * @param posX x
     * @return this instance
     */
    public abstract T setPosX(double posX);

    /**
     * 座標を設定.
     * 
     * @param posY y
     * @return this instance
     */
    public abstract T setPosY(double posY);

    /**
     * 座標を設定.
     * 
     * @param posZ z
     * @return this instance
     */
    public abstract T setPosZ(double posZ);

    /**
     * マーカーの色を設定.
     * 
     * @param color color
     * @return this instance
     */
    public abstract T setColor(Color color);

    /**
     * マーカーの明るさを設定.
     * 
     * @param brightness brightness
     * @return this instance
     */
    public abstract T setBrightness(int brightness);

    /**
     * 状態をリセットします.
     */
    T reset();

    /**
     * 描画します.
     * 
     * @param tickCount tick
     * @param partialTick tick間の経過時間
     */
    void doRender(long tickCount, float partialTick);

}
