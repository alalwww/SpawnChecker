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

import org.lwjgl.opengl.GL11;

import net.awairo.mcmod.common.v1.util.Colors;
import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModel;
import net.awairo.mcmod.spawnchecker.client.marker.model.MobSpawnerActivateArea;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.SpawnerVisualizerConfig;

/**
 * スポーナー活性化範囲マーカー.
 * 
 * @author alalwww
 */
public class MobSpawnerActivateAreaMarker extends SkeletalMarker<MobSpawnerActivateAreaMarker>
{
    private final MobSpawnerActivateArea model = new MobSpawnerActivateArea();

    private boolean computed;

    private boolean drawingFillSphere;

    private Color lineColor;
    private Color fillColor;
    private int brightness;

    public MobSpawnerActivateAreaMarker(SpawnerVisualizerConfig config)
    {
        // TODO: アニメーションなどの設定ロード

        drawingFillSphere = true;
    }

    @Override
    public MobSpawnerActivateAreaMarker setPosX(double posX)
    {
        return super.setPosX(posX + 0.5);
    }

    @Override
    public MobSpawnerActivateAreaMarker setPosY(double posY)
    {
        return super.setPosY(posY + 0.5);
    }

    @Override
    public MobSpawnerActivateAreaMarker setPosZ(double posZ)
    {
        return super.setPosZ(posZ + 0.5);
    }

    @Override
    public MobSpawnerActivateAreaMarker reset()
    {
        // 座標がリセットされるので親のメソッドは実行しない
        // super.reset();
        computed = false;
        return this;
    }

    @Override
    @Deprecated
    public MobSpawnerActivateAreaMarker setColorAndBrightness(Color color, int brightness)
    {
        throw new UnsupportedOperationException();
    }

    public MobSpawnerActivateAreaMarker setLineColor(Color lineColor)
    {
        reset();
        this.lineColor = lineColor;
        return this;
    }

    public MobSpawnerActivateAreaMarker setFillColor(Color fillColor)
    {
        reset();
        this.fillColor = fillColor;
        return this;
    }

    public MobSpawnerActivateAreaMarker setBrightness(int brightness)
    {
        reset();
        this.brightness = brightness;
        return this;
    }

    @Override
    protected MarkerModel model()
    {
        compute();
        return model;
    }

    private void compute()
    {
        if (computed) return;

        model.setLineColor(Colors.applyBrightnessTo(lineColor, brightness));

        if (drawingFillSphere)
        {
            model.setDrawingFillSphere(true);
            model.setFillColor(Colors.applyBrightnessTo(fillColor, brightness));
        }
        else
        {
            model.setDrawingFillSphere(false);
        }

        computed = true;
    }

    @Override
    protected void render(MarkerModel model)
    {
        GL11.glPushMatrix();

        {
            GL11.glTranslated(
                    posX - renderManager.viewerPosX,
                    posY - renderManager.viewerPosY,
                    posZ - renderManager.viewerPosZ);

            // 線の集まる部分が左右なのはなんかかっちょわるから、90度回転し上下に集まるようにする
            GL11.glRotatef(90, 1, 0, 0);
            // 回す
            GL11.glRotatef((tickCounts + partialTicks) / 6, 0, 0, 1);

            model.render();
        }

        GL11.glPopMatrix();
    }

}
