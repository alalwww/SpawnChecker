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

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;

import net.awairo.mcmod.common.v1.util.Colors;

/**
 * RenderingSupport.
 * 
 * @author alalwww
 */
public final class RenderingSupport
{
    public static final RenderManager renderManager = RenderManager.instance;
    public static final Tessellator tessellator = Tessellator.instance;

    /**
     * 描画開始前のGL設定.
     */
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
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }

    /**
     * 描画終了時のGL設定.
     */
    public static void endRendering()
    {
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glDepthMask(true);

        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * 四角形の描画を開始します.
     */
    public static void startDrawingQuads()
    {
        tessellator.startDrawingQuads();
    }

    /**
     * 線の描画を開始します.
     */
    public static void startDrawingLines()
    {
        tessellator.startDrawing(GL11.GL_LINES);
    }

    /**
     * オフセットを設定します.
     * 
     * @param xOffset オフセット値
     * @param yOffset オフセット値
     * @param zOffset オフセット値
     */
    public static void setTranslation(double xOffset, double yOffset, double zOffset)
    {
        tessellator.setTranslation(xOffset, yOffset, zOffset);
    }

    /**
     * 頂点座標を追加.
     * 
     * @param x x
     * @param y y
     * @param z z
     */
    public static void addVertex(double x, double y, double z)
    {
        tessellator.addVertex(x, y, z);
    }

    /**
     * テクスチャの頂点座標を追加.
     * 
     * @param x x
     * @param y y
     * @param z z
     * @param u u
     * @param v v
     */
    public static void addVertexWithUV(double x, double y, double z, double u, double v)
    {
        tessellator.addVertexWithUV(x, y, z, u, v);
    }

    /**
     * 描画します.
     * 
     * @return バッファインデックス？
     */
    public static int draw()
    {
        return tessellator.draw();
    }

    /**
     * 色と明るさを設定します.
     * 
     * @param color 色
     * @param brightness 明るさ
     */
    // TODO: なんかうまくいかないのでこっち使わずGL11.glColor4ubで色に直接明るさ反映してる
    // brightnessの値はかなり大きな数値っぽいけど何を設定しても色に反映されてくれない…
    public static void setColorAndBrightness(Color color, int brightness)
    {
        tessellator.setBrightness(brightness);
        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * 色と明るさを設定します.
     * 
     * @param color 色
     * @param brightness 明るさ
     */
    public static void setGLColorAndBrightness(Color color, int brightness)
    {
        // てっせれーたー使わない場合のブライトネス反映は、
        // ライトマップテクスチャに明るさを設定するのではなく、直接ブライトネスを色に反映して実現してる
        // tessellator のあのあたりの処理よく分かってないので自前で解決…

        setGLColor(Colors.applyBrightnessTo(color, brightness));
    }

    /**
     * 色を設定します.
     * 
     * @param c 色
     */
    public static void setGLColor(Color c)
    {
        setGLColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    /**
     * 色を設定します.
     * 
     * @param argb 色
     */
    public static void setGLColor(int argb)
    {
        int a = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = (argb >> 0) & 0xff;
        setGLColor(r, g, b, a);
    }

    /**
     * 色を設定します.
     * 
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a 透明度
     */
    public static void setGLColor(int r, int g, int b, int a)
    {
        GL11.glColor4ub((byte) r, (byte) g, (byte) b, (byte) a);
    }

    /**
     * Constructor.
     */
    private RenderingSupport()
    {
        throw new InternalError();
    }
}
