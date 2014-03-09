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

import static com.google.common.base.Preconditions.*;

import java.awt.Color;

/**
 * マーカーモデルのスケルトン.
 * 
 * @author alalwww
 */
public abstract class SkeletalMarkerModel implements MarkerModel
{
    protected Color color;
    protected int brightness;

    @Override
    public void setColor(Color color)
    {
        this.color = checkNotNull(color);
    }

    @Override
    public void setBrightness(int brightness)
    {
        checkArgument(brightness >= 0 && brightness <= 255);
        this.brightness = brightness;
    }
}
