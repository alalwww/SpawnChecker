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

package net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer.model;

import static net.awairo.mcmod.spawnchecker.client.marker.RenderingSupport.*;

import net.awairo.mcmod.spawnchecker.client.model.SphereModel;

/**
 * スポーナーが活性化する範囲用の球を描画
 * 
 * @author alalwww
 */
public class MobSpawnerActivateArea extends SphereModel
{
    private boolean drawingFillSphere;
    private int lineColor;
    private int fillColor;

    /**
     * @param drawingFillSphere trueは球の面を描画する
     */
    public void setDrawingFillSphere(boolean drawingFillSphere)
    {
        this.drawingFillSphere = drawingFillSphere;
    }

    /**
     * @param lineColor ワイヤーフレーム線の色
     */
    public void setLineColor(int lineColor)
    {
        this.lineColor = lineColor;
    }

    /**
     * @param fillColor 級の表面の色
     */
    public void setFillColor(int fillColor)
    {
        this.fillColor = fillColor;
    }

    @Override
    public void render()
    {
        setDrawStyleToLine();

        setGLColor(lineColor);

        if (drawingFillSphere)
        {
            // 球の面を描く場合は内周と外周にラインを描画
            setRadius(15.5f + 0.05f);
            super.render();
            setRadius(15.5f - 0.05f);
            super.render();

            setDrawStyleToFill();
            setGLColor(fillColor);
            setRadius(15.5f);
            setSlices(15);
            setStacks(15);
            super.render();
        }
        else
        {
            // 球の面を描かない場合は単一のラインのみ描画
            setRadius(15.5f - 0.05f);
            super.render();
        }
    }
}
