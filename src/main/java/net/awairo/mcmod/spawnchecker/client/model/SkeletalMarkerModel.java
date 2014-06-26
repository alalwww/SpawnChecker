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

package net.awairo.mcmod.spawnchecker.client.model;

/**
 * マーカーモデルのスケルトン.
 * 
 * @author alalwww
 */
public abstract class SkeletalMarkerModel implements MarkerModel
{
    protected long tickCounts;
    protected float partialTicks;

    protected int color;

    /**
     * アニメーションを行うモデルで用いれるタイミングを設定します.
     * 
     * @param tickCounts
     * @param partialTicks
     */
    public void setTicks(long tickCounts, float partialTicks)
    {
        this.tickCounts = tickCounts;
        this.partialTicks = partialTicks;
    }

    /**
     * 描画色を設定します.
     * 
     * @param argbColor RGBカラー
     */
    public void setColor(int argbColor)
    {
        this.color = argbColor;
    }
}
