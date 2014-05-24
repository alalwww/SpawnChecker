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

import static net.awairo.mcmod.spawnchecker.client.marker.RenderingSupport.*;

import java.awt.Color;

import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModel;

/**
 * マーカーのスケルトン.
 * 
 * @author alalwww
 */
public abstract class SkeletalMarker<T extends SkeletalMarker<T>> implements Marker<T>
{
    protected double posX;
    protected double posY;
    protected double posZ;

    protected Color color;

    protected int brightness;

    /**
     * Constructor.
     */
    protected SkeletalMarker()
    {
        reset();
    }

    /**
     * 座標を設定.
     * 
     * @param posX x
     * @param posY y
     * @param posZ z
     * @return this instance
     */
    @Override
    public T setPoint(double posX, double posY, double posZ)
    {
        return setPosX(posX).setPosY(posY).setPosZ(posZ);
    }

    /**
     * 座標を設定.
     * 
     * @param posX x
     * @return this instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setPosX(double posX)
    {
        this.posX = posX;
        return (T) this;
    }

    /**
     * 座標を設定.
     * 
     * @param posY y
     * @return this instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setPosY(double posY)
    {
        this.posY = posY;
        return (T) this;
    }

    /**
     * 座標を設定.
     * 
     * @param posZ z
     * @return this instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setPosZ(double posZ)
    {
        this.posZ = posZ;
        return (T) this;
    }

    /**
     * @param color color
     * @return this instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setColor(Color color)
    {
        this.color = color;
        return (T) this;
    }

    /**
     * @param brightness brightness
     * @return this instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public T setBrightness(int brightness)
    {
        this.brightness = brightness;
        return (T) this;
    }

    @Override
    public T reset()
    {
        return setPoint(0, 0, 0);
    }

    /**
     * 指定したモデルをこのマーカーの位置に描画します.
     * 
     * @param model
     * @param tickCounts
     * @param partialTicks
     */
    protected void render(MarkerModel model, long tickCounts, float partialTicks)
    {
        model.setColor(color);
        model.setBrightness(brightness);

        setTranslation(
                posX - renderManager.viewerPosX,
                posY - renderManager.viewerPosY,
                posZ - renderManager.viewerPosZ);

        model.render(tickCounts, partialTicks);

        setTranslation(0, 0, 0);
    }
}
