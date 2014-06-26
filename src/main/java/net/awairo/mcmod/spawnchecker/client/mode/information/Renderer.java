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

package net.awairo.mcmod.spawnchecker.client.mode.information;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * 情報の描画クラス.
 * 
 * @author alalwww
 */
class Renderer
{
    // なんか3以下だと不透明になるので魔法の数字で解決
    private static final int MIN_ALPHA = 4;

    private final Minecraft game = Minecraft.getMinecraft();
    private final Tessellator t = Tessellator.instance;

    private float alpha = 1.0f;

    /**
     * 次の描画に用いる透明度を設定します
     * 
     * @param alpha 0.0 ～ 1.0 で表した透明度
     */
    void setAlpha(float alpha)
    {
        this.alpha = alpha;
    }

    void endDrawing()
    {
        alpha = 1.0f;
    }

    /**
     * 情報を描画します.
     */
    void draw(int posX, int posY, Mode.Information info)
    {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(false);

        final int rgb = rgbOf(info.intColor());
        int a = alpha == 1.0f
                ? alphaOf(info.intColor())
                : Math.max(Math.round(alphaOf(info.intColor()) * alpha), MIN_ALPHA);

        if (info.hasIcon())
            drawIcon(posX, posY, info, rgb, a);

        drawString(posX, posY, info, rgb | a << 24);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void startDraw()
    {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    }

    private int drawString(int posX, int posY, Mode.Information info, int rgba)
    {
        startDraw();

        final int x = info.hasIcon() ? posX + getMessageOffset(info) : posX;
        final int y = posY;
        return game.fontRenderer.drawStringWithShadow(info.message(), x, y, rgba);
    }

    private void drawIcon(int posX, int posY, Mode.Information info, int rgb, int a)
    {
        startDraw();

        final double x = info.doubleSizeIcon() ? posX - consts().iconWidth / 2 : posX;
        final double y = info.doubleSizeIcon() ? posY - consts().iconHeight / 2 : posY;
        final double w = info.doubleSizeIcon() ? consts().iconWidth + consts().iconWidth : consts().iconWidth;
        final double h = info.doubleSizeIcon() ? consts().iconHeight + consts().iconHeight : consts().iconHeight;

        // left top
        final double ltx = x;
        final double lty = y;
        // right top
        final double rtx = x + w;
        final double rty = y;
        // left bottom
        final double lbx = x;
        final double lby = y + h;
        // right bottom
        final double rbx = x + w;
        final double rby = y + h;

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        game.renderEngine.bindTexture(info.getIconResource());
        t.startDrawingQuads();
        t.setColorRGBA_I(rgb, a);
        t.addVertexWithUV(lbx, lby, consts().guiPosZ, consts().iconTextureUMin, consts().iconTextureVMax);
        t.addVertexWithUV(rbx, rby, consts().guiPosZ, consts().iconTextureUMax, consts().iconTextureVMax);
        t.addVertexWithUV(rtx, rty, consts().guiPosZ, consts().iconTextureUMax, consts().iconTextureVMin);
        t.addVertexWithUV(ltx, lty, consts().guiPosZ, consts().iconTextureUMin, consts().iconTextureVMin);
        t.draw();
    }

    private static int rgbOf(int rgba)
    {
        return rgba & 0xFFFFFF;
    }

    private static int alphaOf(int rgba)
    {
        return rgba >> 24 & 0xFF;
    }

    private static int getMessageOffset(final Mode.Information info)
    {
        int offset = consts().spacerOfIconAndMessage;
        offset += info.doubleSizeIcon() ? consts().iconWidth + consts().iconWidth : consts().iconWidth;
        return offset;
    }

    private static ConstantsConfig consts()
    {
        return ConstantsConfig.instance();
    }
}
