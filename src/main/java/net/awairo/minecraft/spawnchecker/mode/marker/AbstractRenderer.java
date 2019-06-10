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

package net.awairo.minecraft.spawnchecker.mode.marker;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.Vec3d;

import net.awairo.minecraft.spawnchecker.api.Marker;
import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;
import net.awairo.minecraft.spawnchecker.mode.ModeState;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRenderer<M extends Marker> implements MarkerRenderer {

    @NonNull
    private final ModeState state;

    @NonNull
    private final EntityRendererManager renderManager;

    public void render(M marker, WorldRenderer renderer, int tickCount, float partialTicks) {
        GlStateManager.pushMatrix();
        //        translatedTo(marker.pos(), renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ);
        //        marker.color().setToColor4F(GlStateManager::color4f);
        drawModel();
        GlStateManager.popMatrix();
    }

    protected void translatedTo(Vec3d pos, double viewerPosX, double viewerPosY, double viewerPosZ) {
        GlStateManager.translated(
            pos.x - viewerPosX,
            pos.y - viewerPosY,
            pos.z - viewerPosZ
        );
    }

    protected abstract void drawModel();
}
