package spawnchecker;

import static spawnchecker.constants.Constants.*;
import static spawnchecker.enums.Mode.Option.*;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_SpawnChecker;
import net.minecraft.util.ResourceLocation;
import spawnchecker.enums.Mode;
import spawnchecker.enums.Mode.Option;
import spawnchecker.enums.SpawnableEntity;
import spawnchecker.markers.ChunkMarker;
import spawnchecker.markers.MarkerBase;
import spawnchecker.markers.SpawnPointMarker;
import spawnchecker.markers.SpawnerMarker;
import spawnchecker.utils.EnablingItemsHelper;

/**
 * SpawnChecker render class.
 *
 * @author takuru/ale
 */
class RenderSpawnChecker extends Render
{
    private static final Minecraft game = ModLoader.getMinecraftInstance();

    private Sphere sphere = new Sphere();
    private float currentTick;

    public void drawSpawnChecker(EntitySpawnChecker entity, double x, double y, double z, float partialTickTime)
    {
        Settings settings = SpawnChecker.getSettings();
        Mode mode = settings.getCurrentMode();

        if (mode.getOption() == DISABLE)
        {
            return;
        }

        currentTick = entity.tickCount + partialTickTime;
        int brightness = BASE_BRIGHTNESS + settings.getBrightness() * BRIGHTNESS_RATIO;
        boolean marker = mode.hasOption(Option.MARKER);
        boolean guideline = mode.hasOption(Option.GUIDELINE);
//        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        RenderHelper.disableStandardItemLighting();

        switch (mode)
        {
            case SPAWABLE_POINT_CHECKER:
                renderSpawnablePoint(mode, marker, guideline, brightness);
                break;

            case SLIME_CHUNK_FINDER:
                renderSlimeChunkFinder(mode, marker, guideline, brightness);
                break;

            case SPAWNER_VISUALIZER:
                renderSpawnerVisualizer(mode, brightness);
                break;
        }

        RenderHelper.enableStandardItemLighting();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glPopMatrix();
    }

    private void renderSpawnablePoint(Mode mode, boolean marker, boolean guideline, int brightness)
    {
        if (!mode.hasOption(Mode.Option.FORCE) && !EnablingItemsHelper.hasEnablingItem())
        {
            return;
        }

        game.mcProfiler.startSection("spawnable point");
        boolean ghast = mode.hasOption(Option.GHAST);
        Settings settings = SpawnChecker.getSettings();

        for (SpawnPointMarker spm : settings.getRenderDataForSpawnPointList())
        {
            boolean isGhast = spm.spawnableEntity.equals(SpawnableEntity.GHAST);
            boolean drawingMarker = marker || (isGhast && ghast);
            boolean drawingGuideline;
            drawingGuideline = ghast ? isGhast && guideline : guideline;
            Color color = settings.getColorByMob(spm.spawnableEntity);
            drawSpawnCheckMarker(spm, color, brightness, drawingMarker, drawingGuideline);
        }

        game.mcProfiler.endSection();
    }

    private void renderSlimeChunkFinder(Mode mode, boolean marker, boolean guideline, int brightness)
    {
        game.mcProfiler.startSection("slime chunk finder");
        Settings settings = SpawnChecker.getSettings();
        Color slimeChunkColor = settings.getColorSlimeChunk();

        for (MarkerBase<?> m : settings.getRenderDataForChunkMarkerList())
        {
            if (m instanceof SpawnPointMarker)
            {
                SpawnPointMarker spm = (SpawnPointMarker) m;
                Color color = settings.getColorByMob(spm.spawnableEntity);
                drawSpawnCheckMarker(spm, color, brightness, marker, guideline);
                continue;
            }

            if (m instanceof ChunkMarker && mode.hasOption(CHUNK_MARKER))
            {
                ChunkMarker cm = (ChunkMarker) m;
                double minX = cm.minX - renderManager.viewerPosX;
                double minY = Math.max(cm.minY, WORLD_HEIGHT_MIN) - renderManager.viewerPosY;
                double minZ = cm.minZ - renderManager.viewerPosZ;
                double maxX = cm.maxX - renderManager.viewerPosX;
                double maxY = Math.min(cm.maxY, WORLD_HEIGHT_MAX) - renderManager.viewerPosY;
                double maxZ = cm.maxZ - renderManager.viewerPosZ;
                drawAreaSquares(minX, minY, minZ, maxX, maxY, maxZ,
                        SLIME_CHUNK_MERKER_INTERVAL, slimeChunkColor, brightness);
                continue;
            }
        }

        game.mcProfiler.endSection();
    }

    private void renderSpawnerVisualizer(Mode mode, int brightness)
    {
        Settings settings = SpawnChecker.getSettings();
        SpawnerMarker marker = settings.getRenderDataForSpawnerMarker();

        if (marker == null || !marker.visible)
        {
            return;
        }

        game.mcProfiler.startSection("spawner visualizer");

        if (mode.hasOption(SPAWNER))
        {
            double minX = marker.centerMinX - renderManager.viewerPosX;
            double minY = marker.centerMinY - renderManager.viewerPosY;
            double minZ = marker.centerMinZ - renderManager.viewerPosZ;
            double maxX = marker.centerMaxX - renderManager.viewerPosX;
            double maxY = marker.centerMaxY - renderManager.viewerPosY;
            double maxZ = marker.centerMaxZ - renderManager.viewerPosZ;
            drawBoxBorder(minX, minY, minZ, maxX, maxY, maxZ, settings.getColorSpawnerBorder(), brightness);
        }

        if (mode.hasOption(SPAWN_AREA))
        {
            double minX = marker.areaMinX - renderManager.viewerPosX + SPAWNAREA_AREA_OFFSET;
            double minY = marker.areaMinY - renderManager.viewerPosY;
            double minZ = marker.areaMinZ - renderManager.viewerPosZ + SPAWNAREA_AREA_OFFSET;
            double maxX = marker.areaMaxX - renderManager.viewerPosX - SPAWNAREA_AREA_OFFSET;
            double maxY = marker.areaMaxY - renderManager.viewerPosY;
            double maxZ = marker.areaMaxZ - renderManager.viewerPosZ - SPAWNAREA_AREA_OFFSET;
            Color areaColor = settings.getColorSpawnerSpawnArea();
            drawBoxBorder(minX, minY + SPAWNAREA_AREA_OFFSET, minZ,
                    maxX, maxY - SPAWNAREA_AREA_OFFSET, maxZ, areaColor, brightness);
            minX -= SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            minY -= SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            minZ -= SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            maxX += SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            maxY += SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            maxZ += SPAWNAREA_AREA_OFFSET + SPAWNAREA_AREA_OFFSET;
            drawBoxBorder(minX, minY - SPAWNAREA_AREA_OFFSET, minZ,
                    maxX, maxY + SPAWNAREA_AREA_OFFSET, maxZ, areaColor, brightness);
        }

        if (mode.hasOption(SPAWNABLE_POINT | UNSPAWNABLE_POINT))
        {
            float rotateAngle = currentTick * SPAWNER_POINT_MARKER_TICK_RATIO;
            boolean isDrawSpawnablePoint = mode.hasOption(SPAWNABLE_POINT);
            boolean isDrawUnspawnablePoint = mode.hasOption(UNSPAWNABLE_POINT);
            Color colorSpawnable = settings.getColorSpawnerSpawnablePoint();
            Color colorUnspawnable = settings.getColorSpawnerUnspawnablePoint();
            int index = 0;
            int maxX = marker.x + 4;
            int maxY = marker.y + 2;
            int maxZ = marker.z + 4;

            for (int ix = marker.x - 4; ix < maxX; ix++)
            {
                for (int iz = marker.z - 4; iz < maxZ; iz++)
                {
                    for (int iy = marker.y - 1; iy < maxY; iy++)
                    {
                        float a = (currentTick + marker.spawnablesOffset[index]) * 0.06f;
                        float yOffset = (float) Math.sin(a) * 0.02f;
                        // 中央にするため各座標+0.5d
                        double x = ix - renderManager.viewerPosX + 0.5d;
                        double y = iy - renderManager.viewerPosY + 0.5d + yOffset;
                        double z = iz - renderManager.viewerPosZ + 0.5d;

                        if (marker.spawnables[index++])
                        {
                            if (isDrawSpawnablePoint)
                            {
                                drawSpawnablePointMarker(x, y, z, rotateAngle, colorSpawnable, brightness);
                            }
                        }
                        else
                        {
                            if (isDrawUnspawnablePoint)
                            {
                                drawSpawnablePointMarker(x, y, z, rotateAngle, colorUnspawnable, brightness);
                            }
                        }
                    }
                }
            }
        }

        if (mode.hasOption(DUPLICATION_AREA))
        {
            double minX = marker.duplicationAreaMinX - renderManager.viewerPosX;
            double minY = marker.duplicationAreaMinY - renderManager.viewerPosY;
            double minZ = marker.duplicationAreaMinZ - renderManager.viewerPosZ;
            double maxX = marker.duplicationAreaMaxX - renderManager.viewerPosX;
            double maxY = marker.duplicationAreaMaxY - renderManager.viewerPosY;
            double maxZ = marker.duplicationAreaMaxZ - renderManager.viewerPosZ;
            Color areaColor = settings.getColorSpawnerDuplicationArea();
            drawBoxBorder(minX, minY, minZ, maxX, maxY, maxZ, areaColor, brightness);
            drawAreaSquares(minX, minY + SPAWNAREA_AREA_INTERVAL / 2d, minZ, maxX, maxY, maxZ,
                    SPAWNAREA_AREA_INTERVAL, areaColor, brightness);
        }

        if (mode.hasOption(ACTIVATE_AREA))
        {
            double centerX = marker.x + 0.5d - renderManager.viewerPosX;
            double centerY = marker.y + 0.5d - renderManager.viewerPosY;
            double centerZ = marker.z + 0.5d - renderManager.viewerPosZ;
            drawActivateArea(centerX, centerY, centerZ, currentTick / SPAWNER_ACTIVATE_AREA_LINE_SLICES, brightness);
        }

        game.mcProfiler.endSection();
    }

    /**
     * スポーン可能場所マーカーの描画.
     */
    private void drawSpawnCheckMarker(SpawnPointMarker marker, Color color, int brightness,
            boolean drawingMarker, boolean drawingGuideline)
    {
        if (marker == null || !(drawingMarker || drawingGuideline))
        {
            return;
        }

        game.mcProfiler.startSection("spawn check marker");
        double oMaxX = marker.outerMaxX - renderManager.viewerPosX;
        double oMaxY = marker.outerMaxY - renderManager.viewerPosY;
        double oMaxZ = marker.outerMaxZ - renderManager.viewerPosZ;
        double oMinX = marker.outerMinX - renderManager.viewerPosX;
        double oMinY = marker.outerMinY - renderManager.viewerPosY;
        double oMinZ = marker.outerMinZ - renderManager.viewerPosZ;
        double iMaxX = marker.innerMaxX - renderManager.viewerPosX;
        double iMaxY = marker.innerMaxY - renderManager.viewerPosY;
        double iMaxZ = marker.innerMaxZ - renderManager.viewerPosZ;
        double iMinX = marker.innerMinX - renderManager.viewerPosX;
        double iMinY = marker.innerMinY - renderManager.viewerPosY;
        double iMinZ = marker.innerMinZ - renderManager.viewerPosZ;

        if (drawingMarker)
        {
            Tessellator.instance.startDrawingQuads();
            setColorAndBrightness(color, brightness);
            // top
            Tessellator.instance.addVertex(iMinX, oMaxY, iMinZ);
            Tessellator.instance.addVertex(iMinX, oMaxY, iMaxZ);
            Tessellator.instance.addVertex(iMaxX, oMaxY, iMaxZ);
            Tessellator.instance.addVertex(iMaxX, oMaxY, iMinZ);
            // bottom
            Tessellator.instance.addVertex(iMinX, oMinY, iMinZ);
            Tessellator.instance.addVertex(iMaxX, oMinY, iMinZ);
            Tessellator.instance.addVertex(iMaxX, oMinY, iMaxZ);
            Tessellator.instance.addVertex(iMinX, oMinY, iMaxZ);
            // east
            Tessellator.instance.addVertex(iMinX, iMinY, oMinZ);
            Tessellator.instance.addVertex(iMinX, iMaxY, oMinZ);
            Tessellator.instance.addVertex(iMaxX, iMaxY, oMinZ);
            Tessellator.instance.addVertex(iMaxX, iMinY, oMinZ);
            // west
            Tessellator.instance.addVertex(iMinX, iMinY, oMaxZ);
            Tessellator.instance.addVertex(iMaxX, iMinY, oMaxZ);
            Tessellator.instance.addVertex(iMaxX, iMaxY, oMaxZ);
            Tessellator.instance.addVertex(iMinX, iMaxY, oMaxZ);
            // north
            Tessellator.instance.addVertex(oMinX, iMinY, iMinZ);
            Tessellator.instance.addVertex(oMinX, iMinY, iMaxZ);
            Tessellator.instance.addVertex(oMinX, iMaxY, iMaxZ);
            Tessellator.instance.addVertex(oMinX, iMaxY, iMinZ);
            // south
            Tessellator.instance.addVertex(oMaxX, iMinY, iMinZ);
            Tessellator.instance.addVertex(oMaxX, iMaxY, iMinZ);
            Tessellator.instance.addVertex(oMaxX, iMaxY, iMaxZ);
            Tessellator.instance.addVertex(oMaxX, iMinY, iMaxZ);
            Tessellator.instance.draw();
        }

        game.mcProfiler.endSection();

        if (drawingGuideline)
        {
            game.mcProfiler.startSection("guideline");
            // 半分にして上面マーカーの四角の中心座標を算出
            double x = (marker.innerMaxX + marker.innerMinX) / 2.0D - renderManager.viewerPosX;
            double z = (marker.innerMaxZ + marker.innerMinZ) / 2.0D - renderManager.viewerPosZ;
            double bottomY = marker.outerMaxY - renderManager.viewerPosY;
            double topY = Math.min(GUIDELINE_LENGTH + bottomY,
                    WORLD_HEIGHT_MAX - renderManager.viewerPosY);
            Tessellator.instance.startDrawing(GL11.GL_LINES);
            setColorAndBrightness(color, brightness);
            Tessellator.instance.addVertex(x, topY, z);
            Tessellator.instance.addVertex(x, bottomY, z);
            Tessellator.instance.draw();
            game.mcProfiler.endSection();
        }
    }

    /**
     * スライムチャンク枠などの描画。
     * 地面と平行でintervals分の間隔をあけ垂直に重ねた面に、四角形を描画するカンジ.
     */
    private void drawAreaSquares(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
            double intervals, Color color, int brightness)
    {
        game.mcProfiler.startSection("squares");
        Tessellator.instance.startDrawing(GL11.GL_LINES);
        setColorAndBrightness(color, brightness);

        for (double y = minY; y <= maxY; y += intervals)
        {
            // north
            Tessellator.instance.addVertex(minX, y, minZ);
            Tessellator.instance.addVertex(minX, y, maxZ);
            // west
            Tessellator.instance.addVertex(minX, y, maxZ);
            Tessellator.instance.addVertex(maxX, y, maxZ);
            // south
            Tessellator.instance.addVertex(maxX, y, maxZ);
            Tessellator.instance.addVertex(maxX, y, minZ);
            // east
            Tessellator.instance.addVertex(maxX, y, minZ);
            Tessellator.instance.addVertex(minX, y, minZ);
        }

        Tessellator.instance.draw();
        game.mcProfiler.endSection();
    }

    /**
     * 立方体の枠の描画.
     */
    private void drawBoxBorder(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
            Color color, int brightness)
    {
        game.mcProfiler.startSection("box border");
        Tessellator.instance.startDrawing(GL11.GL_LINES);
        setColorAndBrightness(color, brightness);
        // 下面の四角形
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(minX, minY, minZ);
        // 側面の縦線 4本
        Tessellator.instance.addVertex(minX, minY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(minX, minY, maxZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, minY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        // 上面の四角形
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, maxZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(maxX, maxY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(minX, maxY, minZ);
        Tessellator.instance.addVertex(minX, maxY, maxZ);
        Tessellator.instance.draw();
        game.mcProfiler.endSection();
    }

    /**
     * スポーナーのスポーン可否表示用のマーカー描画.
     */
    private void drawSpawnablePointMarker(double x, double y, double z,
            float rotateAngle, Color color, int brightness)
    {
        if (color.getAlpha() <= 0)
        {
            return;
        }

        game.mcProfiler.startSection("spawnable point marker");
        GL11.glPushMatrix();
        setGLColorAndBrightness(color, brightness);
        GL11.glTranslated(x, y, z);
        // 頂点を上に
        GL11.glRotatef(90, 1, 0, 0);
        // 引数の値を元に回転させる
        GL11.glRotatef(rotateAngle, 0, 0, 1);
        // 中心の小さいの
        sphere.setDrawStyle(GLU.GLU_FILL);
        sphere.draw(SPAWNER_POINT_RADIUS_INNER, SPAWNER_POINT_SLICES_INNER, SPAWNER_POINT_STACKS_INNER);
        // 外側の面
        sphere.setDrawStyle(GLU.GLU_FILL);
        sphere.draw(SPAWNER_POINT_RADIUS_OUTER, SPAWNER_POINT_SLICES_OUTER, SPAWNER_POINT_STACKS_OUTER);
        // 外側の線
        sphere.setDrawStyle(GLU.GLU_LINE);
        sphere.draw(SPAWNER_POINT_RADIUS_OUTER, SPAWNER_POINT_SLICES_OUTER, SPAWNER_POINT_STACKS_OUTER);
        GL11.glPopMatrix();
        game.mcProfiler.endSection();
    }

    /**
     * スポーナー活性化範囲球体の描画.
     */
    private void drawActivateArea(double x, double y, double z, float rotateAngle, int brightness)
    {
        game.mcProfiler.startSection("activate area");
        Settings settings = SpawnChecker.getSettings();
        boolean drawingFillSphere = settings.getColorSpawnerActivateArea().getAlpha() > 0;

        if (settings.getColorSpawnerActivateAreaLine().getAlpha() > 0)
        {
            GL11.glPushMatrix();
            setGLColorAndBrightness(settings.getColorSpawnerActivateAreaLine(), brightness);
            GL11.glTranslated(x, y, z);
            // 線の集まる部分が左右なのはなんかかっちょわるから、90度回転し上下に集まるようにする
            GL11.glRotatef(90, 1, 0, 0);
            // 回す
            GL11.glRotatef(rotateAngle, 0, 0, 1);
            sphere.setDrawStyle(GLU.GLU_LINE);
            // 名前が長くて改行しちゃうのが嫌なのでローカル変数に保管してる
            int slices = SPAWNER_ACTIVATE_AREA_LINE_SLICES;
            int stacks = SPAWNER_ACTIVATE_AREA_LINE_STACKS;

            // 面描画する場合は面の内側と外側両方に線を描画
            if (drawingFillSphere)
            {
                // 塗りつぶし球体の内側と外側に描画する
                sphere.draw(SPAWNER_ACTIVATE_AREA_RADIUS + 0.05f, slices, stacks);
                sphere.draw(SPAWNER_ACTIVATE_AREA_RADIUS - 0.05f, slices, stacks);
            }
            else
            {
                sphere.draw(SPAWNER_ACTIVATE_AREA_RADIUS, slices, stacks);
            }

            GL11.glPopMatrix();
        }

        // 2つの線の間に面を塗りつぶした球を描画
        if (drawingFillSphere)
        {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            setGLColor(settings.getColorSpawnerActivateArea());
            GL11.glTranslated(x, y, z);
            sphere.setDrawStyle(GLU.GLU_FILL);
            sphere.draw(SPAWNER_ACTIVATE_AREA_RADIUS, SPAWNER_ACTIVATE_AREA_SLICES, SPAWNER_ACTIVATE_AREA_STACKS);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }

        game.mcProfiler.endSection();
    }

    private void setColorAndBrightness(Color color, int brightness)
    {
        Tessellator.instance.setBrightness(brightness);
        Tessellator.instance.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    // てっせれーたー使わない場合のブライトネス反映は、
    // ライトマップテクスチャに明るさを設定するのではなく、直接ブライトネスを色に反映して実現してる
    // Tessellator のあのあたりの処理よく分かってないので自前で解決…
    private void setGLColorAndBrightness(Color color, int brightness)
    {
        int nowBrightness = Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
        float ratio = (float) brightness / (float) nowBrightness;
        int r = (int)(color.getRed() * ratio);
        int g = (int)(color.getGreen() * ratio);
        int b = (int)(color.getBlue() * ratio);
        setGLColor(new Color(r, g, b, color.getAlpha()));
    }

    private void setGLColor(Color c)
    {
        GL11.glColor4ub((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue(), (byte) c.getAlpha());
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        if (mod_SpawnChecker.DEBUG_MODE)
        {
            game.mcProfiler.endSection();
            game.mcProfiler.endSection();
            game.mcProfiler.endSection();
            game.mcProfiler.endSection();
            game.mcProfiler.startSection("SpawnChecker");
            game.mcProfiler.startSection("render");
        }

        drawSpawnChecker((EntitySpawnChecker) entity, x, y, z, partialTickTime);

        if (mod_SpawnChecker.DEBUG_MODE)
        {
            game.mcProfiler.endSection();
            game.mcProfiler.endSection();
            game.mcProfiler.startSection("gameRenderer");
            game.mcProfiler.startSection("level");
            game.mcProfiler.startSection("entities");
            game.mcProfiler.startSection("entities");
        }
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        // nothing
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
