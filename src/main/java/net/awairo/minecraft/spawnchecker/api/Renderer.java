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

package net.awairo.minecraft.spawnchecker.api;

import net.minecraft.client.renderer.Matrix4f;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public interface Renderer {
    void bindTexture(ResourceLocation texture);

    default Tessellator tessellator() {
        return Tessellator.getInstance();
    }

    default BufferBuilder buffer() {
        return tessellator().getBuffer();
    }

    default void beginPoints(VertexFormat format) {
        begin(GL11.GL_POINT, format);
    }

    default void beginLines(VertexFormat format) {
        begin(GL11.GL_LINES, format);
    }

    default void beginLineLoop(VertexFormat format) {
        begin(GL11.GL_LINE_LOOP, format);
    }

    default void beginTriangles(VertexFormat format) {
        begin(GL11.GL_TRIANGLES, format);
    }

    default void beginTriangleStrip(VertexFormat format) {
        begin(GL11.GL_TRIANGLE_STRIP, format);
    }

    default void beginTriangleFan(VertexFormat format) {
        begin(GL11.GL_TRIANGLE_FAN, format);
    }

    default void beginQuads(VertexFormat format) {
        begin(GL11.GL_QUADS, format);
    }

    default void beginQuadStrip(VertexFormat format) {
        begin(GL11.GL_QUAD_STRIP, format);
    }

    default void beginPolygon(VertexFormat format) {
        begin(GL11.GL_POLYGON, format);
    }

    default void begin(int glMode, VertexFormat format) {
        buffer().begin(glMode, format);
    }

    default void addVertex(double x, double y, double z) {
        buffer()
            .pos(x, y, z)
            .endVertex();
    }

    default void addVertex(double x, double y, double z, float u, float v) {
        buffer()
            .pos(x, y, z)
            .tex(u, v)
            .endVertex();
    }

    default void addVertex(Matrix4f m, double x, double y, double z, float u, float v) {
        buffer()
            .pos(m, (float)x, (float)y, (float)z)
            .tex(u, v)
            .endVertex();
    }

    default void addVertex(double x, double y, double z, Color color) {
        buffer()
            .pos(x, y, z)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .endVertex();
    }

    default void addVertex(double x, double y, double z, float u, float v, Color color) {
        buffer()
            .pos(x, y, z)
            .tex((float)u, (float)v)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .endVertex();
    }

    default void draw() {
        tessellator().draw();
    }

    float partialTicks();
}
