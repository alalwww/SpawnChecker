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
 */
public class Marker<T extends Marker<T>>
{
    protected double x;
    protected double y;
    protected double z;

    protected Color color;

    protected int brightness;

    /**
     * Constructor.
     */
    protected Marker()
    {
        reset();
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }

    public double z()
    {
        return z;
    }

    public T setPoint(double x, double y, double z)
    {
        return setX(x).setY(y).setZ(z);
    }

    @SuppressWarnings("unchecked")
    public T setX(double x)
    {
        this.x = x;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setY(double y)
    {
        this.y = y;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setZ(double z)
    {
        this.z = z;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setColor(Color color)
    {
        this.color = color;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setBrightness(int brightness)
    {
        this.brightness = brightness;
        return (T) this;
    }

    public T reset()
    {
        return setPoint(0, 0, 0);
    }
}
