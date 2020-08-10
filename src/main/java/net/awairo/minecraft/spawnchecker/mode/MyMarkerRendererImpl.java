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

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;

import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
final class MyMarkerRendererImpl implements MarkerRenderer {
    private final WorldRenderer worldRenderer;
    private final float partialTicks;
    @Getter(AccessLevel.PRIVATE)
    private final MatrixStack matrixStack;
    private final TextureManager textureManager;
    private final EntityRendererManager renderManager;

    @Override
    public void bindTexture(ResourceLocation texture) {
        textureManager.bindTexture(texture);
    }

    @Override
    public void addVertex(double x, double y, double z) {
        buffer()
            .pos(matrixStack.getLast().getMatrix(), (float) x, (float) y, (float) z)
            .endVertex();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v) {
        buffer()
            .pos(matrixStack.getLast().getMatrix(), (float) x, (float) y, (float) z)
            .tex(u, v)
            .endVertex();
    }

    @Override
    public void addVertex(double x, double y, double z, Color color) {
        buffer()
            .pos(matrixStack.getLast().getMatrix(), (float) x, (float) y, (float) z)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .endVertex();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v, Color color) {
        buffer()
            .pos(matrixStack.getLast().getMatrix(), (float) x, (float) y, (float) z)
            .tex(u, v)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .endVertex();
    }

    @Override
    public void push() {
        matrixStack.push();
    }

    @Override
    public void pop() {
        matrixStack.pop();
    }

    @Override
    public void translate(double x, double y, double z) {
        matrixStack.translate(x, y, z);
    }

    @Override
    public void scale(float m00, float m11, float m22) {
        matrixStack.scale(m00, m11, m22);
    }

    @Override
    public void rotate(Quaternion quaternion) {
        matrixStack.rotate(quaternion);
    }

    @Override
    public void clear() {
        matrixStack.clear();
    }
}
