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

import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;

import lombok.Value;

@Value
final class MyMarkerRendererImpl implements MarkerRenderer {
    private final WorldRenderer worldRenderer;
    private final float partialTicks;
    private final MatrixStack matrixStack;
    private final TextureManager textureManager;
    private final EntityRendererManager renderManager;

    @Override
    public void bindTexture(ResourceLocation texture) {
        textureManager.bindTexture(texture);
    }
}
