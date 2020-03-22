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

package net.awairo.minecraft.spawnchecker.mode.marker.model;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;
import net.awairo.minecraft.spawnchecker.mode.YOffset;
import net.awairo.minecraft.spawnchecker.mode.marker.MarkerModel;

import lombok.NonNull;
import lombok.val;

public class SpawnPointModel implements MarkerModel {

    private static final double BLOCK_SIZE = 1.0d;

    private final ResourceLocation texture;
    private final double markerSize;
    private final double offset;
    private final YOffset yOffset;

    private final double oMin;
    private final double oMax;
    private final double oMinY;
    private final double oMaxY;
    private final double iMin;
    private final double iMax;

    public SpawnPointModel(@NonNull ResourceLocation texture, double markerSize, double offset, YOffset yOffset) {
        this.texture = texture;
        this.markerSize = markerSize;
        this.offset = offset;
        this.yOffset = yOffset;

        this.oMin = 0d - offset;
        this.oMax = BLOCK_SIZE + offset;
        this.oMinY = oMin + yOffset.bottomOffset();
        this.oMaxY = oMax + yOffset.topOffset();
        val markerSizeOffset = (oMax - markerSize) / 2;

        this.iMin = oMin + markerSizeOffset;
        this.iMax = oMax - markerSizeOffset;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void draw(MarkerRenderer renderer) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            SourceFactor.SRC_ALPHA.param, DestFactor.ONE_MINUS_SRC_ALPHA.param,
            SourceFactor.ONE.param, DestFactor.ZERO.param
        );
        RenderSystem.enableTexture();

        renderer.bindTexture(texture);

        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float minU = 0.0f;
        float maxU = 0.5f;
        float minV = 0.0f;
        float maxV = 0.5f;
        // top
        renderer.addVertex(iMin, oMaxY, iMin, minU, minV);
        renderer.addVertex(iMin, oMaxY, iMax, minU, maxV);
        renderer.addVertex(iMax, oMaxY, iMax, maxU, maxV);
        renderer.addVertex(iMax, oMaxY, iMin, maxU, minV);
        // bottom
        renderer.addVertex(iMin, oMinY, iMin, minU, minV);
        renderer.addVertex(iMax, oMinY, iMin, maxU, minV);
        renderer.addVertex(iMax, oMinY, iMax, maxU, maxV);
        renderer.addVertex(iMin, oMinY, iMax, minU, maxV);
        // east
        renderer.addVertex(iMin, iMin, oMin, minU, minV);
        renderer.addVertex(iMin, iMax, oMin, minU, maxV);
        renderer.addVertex(iMax, iMax, oMin, maxU, maxV);
        renderer.addVertex(iMax, iMin, oMin, maxU, minV);
        // west
        renderer.addVertex(iMin, iMin, oMax, minU, minV);
        renderer.addVertex(iMax, iMin, oMax, maxU, minV);
        renderer.addVertex(iMax, iMax, oMax, maxU, maxV);
        renderer.addVertex(iMin, iMax, oMax, minU, maxV);
        // north
        renderer.addVertex(oMin, iMin, iMin, minU, minV);
        renderer.addVertex(oMin, iMin, iMax, minU, maxV);
        renderer.addVertex(oMin, iMax, iMax, maxU, maxV);
        renderer.addVertex(oMin, iMax, iMin, maxU, minV);
        // south
        renderer.addVertex(oMax, iMin, iMin, minU, minV);
        renderer.addVertex(oMax, iMax, iMin, maxU, minV);
        renderer.addVertex(oMax, iMax, iMax, maxU, maxV);
        renderer.addVertex(oMax, iMin, iMax, minU, maxV);

        renderer.draw();
        RenderSystem.disableBlend();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("markerSize", markerSize)
            .add("topOffset", offset)
            .add("yOffset", yOffset)
            .add("oMin", oMin)
            .add("oMax", oMax)
            .add("oMaxY", oMaxY)
            .add("iMin", iMin)
            .add("iMax", iMax)
            .toString();
    }
}
