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

package net.awairo.minecraft.spawnchecker.mode;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.HudData;
import net.awairo.minecraft.spawnchecker.api.HudRenderer;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.ScanRange;
import net.awairo.minecraft.spawnchecker.hud.HudIconResource;

import lombok.NonNull;
import lombok.val;

final class RangeConfigHudData extends HudData.Simple {

    private static final String HUD_H_RANGE_KEY = "spawnchecker.hud.horizontalRange";
    private static final String HUD_V_RANGE_KEY = "spawnchecker.hud.verticalRange";

    private static final float LINE_OFFSET = 7.0f;

    private final ITextComponent hRange;
    private final ITextComponent vRange;

    private final double iconMinX;
    private final double iconMaxX;
    private final float textX;

    RangeConfigHudData(
        @NonNull Mode.Name modeName,
        @NonNull ResourceLocation icon,
        @NonNull ScanRange.Horizontal hRange,
        @NonNull ScanRange.Vertical vRange,
        @NonNull ShowDuration showDuration) {
        super(modeName.textComponent(), icon, showDuration);

        this.hRange = new TranslationTextComponent(HUD_H_RANGE_KEY, hRange.value());
        this.vRange = new TranslationTextComponent(HUD_V_RANGE_KEY, vRange.value());

        this.iconMinX = TEXT_X;
        this.iconMaxX = TEXT_X + ICON_SIZE;
        this.textX = TEXT_X * 2;
    }

    @Override
    @SuppressWarnings("Duplicates")
    protected void drawIcon(ResourceLocation icon, HudRenderer renderer, Color color) {
        super.drawIcon(icon, renderer, color);

        final double xMin, yMin1, yMin2, xMax, yMax1, yMax2, z;
        final float uMin, uMax, vMin, vMax;
        xMin = iconMinX;
        xMax = iconMaxX;
        yMin1 = renderer.fontRenderer().FONT_HEIGHT + LINE_OFFSET;
        yMax1 = renderer.fontRenderer().FONT_HEIGHT + LINE_OFFSET + ICON_SIZE;
        yMin2 = yMin1 * 2;
        yMax2 = yMin1 * 2 + ICON_SIZE;
        z = 0d;
        uMin = vMin = 0f;
        uMax = vMax = 1f;

        renderer.bindTexture(HudIconResource.HORIZONTAL_RANGE.location());
        renderer.beginQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.addVertex(xMin, yMin1, z, uMin, vMin, color);
        renderer.addVertex(xMin, yMax1, z, uMin, vMax, color);
        renderer.addVertex(xMax, yMax1, z, uMax, vMax, color);
        renderer.addVertex(xMax, yMin1, z, uMax, vMin, color);
        renderer.draw();

        renderer.bindTexture(HudIconResource.VERTICAL_RANGE.location());
        renderer.beginQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.addVertex(xMin, yMin2, z, uMin, vMin, color);
        renderer.addVertex(xMin, yMax2, z, uMin, vMax, color);
        renderer.addVertex(xMax, yMax2, z, uMax, vMax, color);
        renderer.addVertex(xMax, yMin2, z, uMax, vMin, color);
        renderer.draw();

    }

    @Override
    protected void drawText(ITextComponent text, HudRenderer renderer, Color color) {
        if (isTransparentText(color))
            return;

        super.drawText(text, renderer, color);

        renderer.fontRenderer()
            .drawStringWithShadow(
                hRange.getString(),
                textX,
                TEXT_Y + renderer.fontRenderer().FONT_HEIGHT + LINE_OFFSET,
                color.toInt()
            );

        renderer.fontRenderer()
            .drawStringWithShadow(
                vRange.getString(),
                textX,
                TEXT_Y + (renderer.fontRenderer().FONT_HEIGHT + LINE_OFFSET) * 2,
                color.toInt()
            );
    }

    @Override
    protected float computeAlpha(long elapsedMillis) {
        val rate = showDuration().progressRate(elapsedMillis);
        if (rate >= 0.9f)
            return Math.min((1 - rate) * 10, 1f);
        return 1f;
    }
}
