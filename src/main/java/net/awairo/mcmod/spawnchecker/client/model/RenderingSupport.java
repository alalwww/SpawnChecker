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

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;

/**
 * @author alalwww
 */
public final class RenderingSupport
{
    static final RenderManager renderManager = RenderManager.instance;
    static final Tessellator tessellator = Tessellator.instance;

    public static void beginRendering()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(false);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,
                GL11.GL_ONE, GL11.GL_ZERO);
    }

    public static void endRendering()
    {
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glDepthMask(true);

        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void startDrawingQuads()
    {
        tessellator.startDrawingQuads();
    }

    public static void startDrawingLines()
    {
        tessellator.startDrawing(GL11.GL_LINES);
    }

    public static void addVertex(double x, double y, double z)
    {
        tessellator.addVertex(x, y, z);
    }

    public static void addVertexWithUV(double x, double y, double z, double u, double v)
    {
        tessellator.addVertexWithUV(x, y, z, u, v);
    }

    public static int draw()
    {
        return tessellator.draw();
    }

    // TODO: なんかうまくいかないのでこっち使わずGL11.glColor4ubで色に直接明るさ反映してる
    // brightnessの値はかなり大きな数値っぽいけど何を設定しても色に反映されてくれない…
    public static void setColorAndBrightness(Color color, int brightness)
    {
        tessellator.setBrightness(brightness);
        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void setGLColorAndBrightness(Color color, int brightness)
    {
        // てっせれーたー使わない場合のブライトネス反映は、
        // ライトマップテクスチャに明るさを設定するのではなく、直接ブライトネスを色に反映して実現してる
        // tessellator のあのあたりの処理よく分かってないので自前で解決…

        final int nowBrightness = Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
        float ratio = (float) brightness / (float) nowBrightness;
        final int r = (int) (color.getRed() * ratio);
        final int g = (int) (color.getGreen() * ratio);
        final int b = (int) (color.getBlue() * ratio);
        setGLColor(r, g, b, color.getAlpha());
    }

    public static void setGLColor(Color c)
    {
        setGLColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static void setGLColor(int r, int g, int b, int a)
    {
        GL11.glColor4ub((byte) r, (byte) g, (byte) b, (byte) a);
    }

    private RenderingSupport()
    {
        throw new InternalError();
    }
}
