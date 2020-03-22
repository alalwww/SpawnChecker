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

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;
import net.awairo.minecraft.spawnchecker.mode.marker.MarkerModel;

public class GuidelineModel implements MarkerModel {

    @Override
    public void draw(MarkerRenderer renderer) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            SourceFactor.SRC_ALPHA.param, DestFactor.ONE_MINUS_SRC_ALPHA.param,
            SourceFactor.ONE.param, DestFactor.ZERO.param
        );
        renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        renderer.addVertex(0.5d, 0d, 0.5d);
        renderer.addVertex(0.5d, 32d, 0.5d);
        renderer.draw();
        RenderSystem.disableBlend();
    }
}
