/*
 * SpawnChecker
 * Copyright (C) 2019 alalwww
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.awairo.minecraft.spawnchecker.hud;

import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import net.awairo.minecraft.spawnchecker.api.HudData;
import net.awairo.minecraft.spawnchecker.api.HudData.Visibility;
import net.awairo.minecraft.spawnchecker.api.HudRenderer;
import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;

// TODO: 位置と表示内容の実装
@Log4j2
@RequiredArgsConstructor
public final class HudRendererImpl implements HudRenderer {
    private final Minecraft minecraft;
    private final SpawnCheckerConfig config;

    private static final long UNDEFINED = -1;
    @Nullable
    private HudData hudData = null;
    private long showStartTime = UNDEFINED;
    private int tickCount;
    private float partialTicks;

    @Override
    public int tickCount() {
        return tickCount;
    }

    @Override
    public float partialTicks() {
        return partialTicks;
    }

    @Override
    public FontRenderer fontRenderer() {
        return minecraft.fontRenderer;
    }

    @Override
    public void bindTexture(ResourceLocation texture) {
        minecraft.textureManager.bindTexture(texture);
    }

    public void setData(HudData hudData) {
        removeData();
        this.hudData = Objects.requireNonNull(hudData, "hudData");
    }

    private void removeData() {
        this.hudData = null;
        showStartTime = UNDEFINED;
    }

    public void render(int tickCount, float partialTicks) {
        if (hudData == null || minecraft.isGamePaused())
            return;

        this.tickCount = tickCount;
        this.partialTicks = partialTicks;
        val now = Util.milliTime();
        if (showStartTime == UNDEFINED) {
            showStartTime = now;
        }
        val h = minecraft.mainWindow.getScaledHeight();
        val w = minecraft.mainWindow.getScaledWidth();
        GlStateManager.pushMatrix();
        GlStateManager.translated(
            w / 20 + config.hudConfig().xOffset().value(),
            h / 3 + config.hudConfig().yOffset().value(),
            0d
        );
        GlStateManager.scalef(1.0f, 1.0f, 1f);
        val hudVisibility = hudData.draw(this, now - showStartTime);
        GlStateManager.popMatrix();

        if (hudVisibility == Visibility.HIDE)
            removeData();
    }

    //
    //    public void renderHUD(@SuppressWarnings("unused") float partialTicks) {
    //        if (!state.enabled())
    //            return;
    //
    //        GlStateManager.enableBlend();
    //        GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
    //        GlStateManager.enableTexture2D();
    //
    //        val x = 0f;
    //        val y = 50f;
    //        val r = 0;
    //        val g = 255;
    //        val b = 0;
    //        val a = 127;
    //        val color = (r << 16) | (g << 8) | b | (a << 24);
    //        val text = "test";
    //        val iconResource = HUDTextures.SPAWN_CHECKER;
    //
    //        drawIcon(minecraft, iconResource, x, y, 255, g, b, a);
    //        drawTextWithShadow(minecraft, text, x + 30, y, color);
    //    }
    //
    //    private void drawTextWithShadow(Minecraft game, String text, float x, float y, int color) {
    //        game.fontRenderer.drawStringWithShadow(text, x, y, color);
    //    }
    //
    //    private void drawIcon(Minecraft game, HUDTextures resource, float x, float y, int r, int g, int b, int a) {
    //        final double ltx, rtx, lbx, rbx, lty, rty, lby, rby;
    //
    //        val iconWidth = 16d;
    //        val iconHeight = 16d;
    //        val z = 1d;
    //        val uMin = 0d;
    //        val uMax = 1d;
    //        val vMin = 0d;
    //        val vMax = 1d;
    //
    //        ltx = lbx = x + 0d;
    //        rtx = rbx = x + iconWidth;
    //        lty = rty = y + iconHeight;
    //        lby = rby = y + 0d;
    //
    //        game.textureManager.bindTexture(resource.location);
    //
    //        val tessellator = Tessellator.getInstance();
    //        val buffer = tessellator.getBuffer();
    //        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    //        addVertexWithUV(buffer, lbx, lby, z, uMin, vMin, r, g, b, a);
    //        addVertexWithUV(buffer, ltx, lty, z, uMin, vMax, r, g, b, a);
    //        addVertexWithUV(buffer, rtx, rty, z, uMax, vMax, r, g, b, a);
    //        addVertexWithUV(buffer, rbx, rby, z, uMax, vMin, r, g, b, a);
    //        tessellator.draw();
    //    }
    //
    //    private void addVertexWithUV(
    //        BufferBuilder buffer, double x, double y, double z, double u, double v, int r, int g, int b, int a) {
    //        buffer
    //            .pos(x, y, z)
    //            .tex(u, v)
    //            .color(r, g, b, a)
    //            .endVertex();
    //    }
}
