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

import javax.annotation.Nullable;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.Marker;
import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;
import net.awairo.minecraft.spawnchecker.mode.YOffset;
import net.awairo.minecraft.spawnchecker.mode.marker.model.GuidelineModel;
import net.awairo.minecraft.spawnchecker.mode.marker.model.SpawnPointModel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SpawnPointMarker implements Marker {

    enum Texture {
        DEFAULT, ENDERMAN, GHAST, SLIME, SPIDER;
        @Getter
        private final ResourceLocation location =
            new ResourceLocation(SpawnChecker.MOD_ID, "textures/markers/spawn_marker_" + name().toLowerCase() + ".png");
    }

    public static Builder builder() { return new Builder(); }

    @Data
    @Accessors(chain = true, fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {
        private Color endermanMarkerColor;
        private Color zombieSizeMobMarkerColor;
        private Color spiderMarkerColor;
        private Color slimeMarkerColor;
        private Color ghastMarkerColor;
        private boolean drawGuideline;
        private double blockSize = 0.4d;

        public SpawnPointMarker buildEndermanMarker(BlockPos pos, YOffset yOffset) {
            return new SpawnPointMarker(Texture.ENDERMAN, blockSize, endermanMarkerColor, pos, yOffset, drawGuideline);
        }

        public SpawnPointMarker buildZombieSizeMobMarker(BlockPos pos, YOffset yOffset) {
            return new SpawnPointMarker(Texture.DEFAULT, blockSize, zombieSizeMobMarkerColor, pos, yOffset, drawGuideline);
        }

        public SpawnPointMarker buildSpiderMarker(BlockPos pos, YOffset yOffset) {
            return new SpawnPointMarker(Texture.SPIDER, blockSize, spiderMarkerColor, pos, yOffset, drawGuideline);
        }

        public SpawnPointMarker buildSlimeMarker(BlockPos pos, YOffset yOffset) {
            return new SpawnPointMarker(Texture.SLIME, blockSize, slimeMarkerColor, pos, yOffset, drawGuideline);
        }

        public SpawnPointMarker buildGhastMarker(BlockPos pos, YOffset yOffset) {
            return new SpawnPointMarker(Texture.GHAST, blockSize, ghastMarkerColor, pos, yOffset, drawGuideline);
        }
    }

    private final SpawnPointModel markerModel;

    @Nullable
    private final GuidelineModel guidelineModel;

    private final BlockPos pos;
    private final Color color;

    private SpawnPointMarker(
        Texture texture,
        double blockSize,
        Color color,
        BlockPos pos,
        YOffset yOffset,
        boolean drawGuideline
    ) {
        this.markerModel = new SpawnPointModel(texture.location, blockSize, 0.03d, yOffset);
        this.guidelineModel = drawGuideline ? new GuidelineModel() : null;
        this.pos = pos;
        this.color = color;
    }

    @Override
    public void draw(MarkerRenderer renderer) {
        if (renderer.renderManager().info == null)
            return;

        val viewerPos = renderer.renderManager().info.getProjectedView();
        val matrixStack = renderer.matrixStack();
        matrixStack.push();
        {
            color.setToColor4F(RenderSystem::color4f);
            matrixStack.translate(
                ((double) pos.getX()) - viewerPos.x,
                ((double) pos.getY()) - viewerPos.y - 1d, // 1ブロック下げる
                ((double) pos.getZ()) - viewerPos.z
            );
            markerModel.draw(renderer);

        }
        matrixStack.pop();

        if (guidelineModel == null)
            return;

        matrixStack.push();
        {
            color.setToColor4F(RenderSystem::color4f);
            matrixStack.translate(
                ((double) pos.getX()) - viewerPos.x,
                ((double) pos.getY()) - viewerPos.y,
                ((double) pos.getZ()) - viewerPos.z
            );
            guidelineModel.draw(renderer);
        }
        matrixStack.pop();
    }
}
