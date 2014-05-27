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

import net.awairo.mcmod.common.v1.util.Colors;
import net.awairo.mcmod.spawnchecker.client.model.MarkerModel;

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

    protected int argbColor;

    protected long tickCounts;
    protected float partialTicks;

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

    @Override
    public T setColorAndBrightness(Color color, int brightness)
    {
        return setColor(Colors.applyBrightnessTo(color, brightness));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setColor(int argbColor)
    {
        this.argbColor = argbColor;
        return (T) this;
    }

    @Override
    public T reset()
    {
        argbColor = 0;
        return setPoint(0, 0, 0);
    }

    /**
     * 描画するモデルを取得します.
     * 
     * @return モデル
     */
    protected abstract MarkerModel model();

    @Override
    public void doRender(long tickCounts, float partialTicks)
    {
        setTicks(tickCounts, partialTicks);
        render(model());
    }

    protected void setTicks(long tickCounts, float partialTicks)
    {
        this.tickCounts = tickCounts;
        this.partialTicks = partialTicks;
    }

    /**
     * 指定したモデルをこのマーカーの位置に描画します.
     * 
     * @param model
     */
    protected void render(MarkerModel model)
    {
        setTranslation(
                posX - renderManager.viewerPosX,
                posY - renderManager.viewerPosY,
                posZ - renderManager.viewerPosZ);

        model.render();

        setTranslation(0, 0, 0);
    }
}
