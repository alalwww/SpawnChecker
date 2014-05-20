/*
 * SpawnChecker.
 * 
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.spawnchecker.client.mode.preset;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.MathHelper;

import net.awairo.mcmod.spawnchecker.PresetMode;
import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.CoordHelper;
import net.awairo.mcmod.spawnchecker.client.marker.ChunkMarker;
import net.awairo.mcmod.spawnchecker.client.marker.Marker;
import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;
import net.awairo.mcmod.spawnchecker.client.mode.preset.checker.SlimeSpawnChecker;
import net.awairo.mcmod.spawnchecker.client.mode.preset.checker.SurfaceSpawnCheck;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.SlimeChunkVisualizerConfig;

/**
 * スライムチャンク可視化モード.
 * 
 * @author alalwww
 */
public class SlimeChunkVisualizerMode extends PresetMode<SlimeChunkVisualizerMode> implements SelectableMode
{
    public static final String ID = "slimechunkvisualizer";

    private final Minecraft game = Minecraft.getMinecraft();
    private final ConstantsConfig consts = ConstantsConfig.instance();

    private SlimeSpawnChecker slimeSpawnChecker;
    private SurfaceSpawnCheck spawnCheck;
    private ArrayList<Marker<?>> chunkMarkers;

    private long nextChunkMarkerUpdateTime;

    /**
     * Constructor.
     */
    public SlimeChunkVisualizerMode()
    {
        super(ID);
        setNameKey("spawnchecker.mode.slimechunk_visualizer");
    }

    @Override
    public int ordinal()
    {
        return 20;
    }

    @Override
    public int compareTo(SelectableMode o)
    {
        return SelectableMode.Comparator.compare(this, o);
    }

    @Override
    public String iconResourceName()
    {
        return "spawnchecker:icon/slime_chunk_visualizer.png";
    }

    @Override
    protected SlimeChunkVisualizerConfig config()
    {
        return configs().slimeChunkVisualizerMode;
    }

    @Override
    protected void onStart()
    {
        slimeSpawnChecker = SlimeSpawnChecker.newCheckerOfCurrentWorld();
        spawnCheck = new SurfaceSpawnCheck(this);
        chunkMarkers = Lists.newArrayListWithCapacity(consts.slimeChunkMarkerCacheInitSize);
    }

    @Override
    protected void onStop()
    {
        slimeSpawnChecker = null;
        spawnCheck = null;
        chunkMarkers = null;
    }

    @Override
    protected void onUpdate()
    {
        spawnCheck.reset(options());

        if (options().contains(Options.DISABLED))
        {
            chunkMarkers.clear();
            return;
        }

        spawnCheck.setBrightness(commonState().brightness().current());

        final EntityClientPlayerMP p = game.thePlayer;

        final int px = MathHelper.floor_double(p.posX);
        final int py = MathHelper.floor_double(p.posY) + (int) Math.floor(p.height);
        final int pz = MathHelper.floor_double(p.posZ);

        if (options().contains(Options.SLIME_CHUNK))
        {
            if (isChunkMarkerUpdatePhase())
            {
                // TODO: 前回チェック時のチャンク座標をメモしておいて、同じならスキップするようにする
                checkSlimeChunk(px, py, pz);
            }
        }
        else
        {
            chunkMarkers.clear();
        }

        if (options().contains(Options.SLIME) || options().contains(Options.GUIDELINE)
                || options().contains(Options.FORCE_SLIME) || options().contains(Options.FORCE_GUIDELINE))
        {
            checkSlimeSpawn(px, py, pz);
        }
    }

    private boolean isChunkMarkerUpdatePhase()
    {
        final long current = Minecraft.getSystemTime();

        if (nextChunkMarkerUpdateTime > current)
            return false;

        nextChunkMarkerUpdateTime = current + config().chunkUpdateFrequency();

        return true;
    }

    private void checkSlimeChunk(int playerX, int playerY, int playerZ)
    {
        chunkMarkers.clear();

        final int centerX = CoordHelper.toChunkCoord(playerX);
        final int centerZ = CoordHelper.toChunkCoord(playerZ);

        final int scanRange = config().chunkScanRange();
        final int mincx = centerX - scanRange;
        final int mincz = centerZ - scanRange;
        final int maxcx = centerX + scanRange;
        final int maxcz = centerZ + scanRange;

        // TODO: 重複コードの共通化、スポーンチェックのスケルトンクラスにも同じ処理がある
        final int computedBrightness = consts.baseBrightness
                + commonState().brightness().current() * consts.brightnessRatio;

        final int y = Math.max(0, playerY - 16);

        for (int x = mincx; x <= maxcx; x++)
        {
            for (int z = mincz; z <= maxcz; z++)
            {
                int worldX = x << 4;
                int worldZ = z << 4;
                if (slimeSpawnChecker.isSlimeChunk(worldX, worldZ))
                {
                    chunkMarkers.add(new ChunkMarker()
                            .setPoint(worldX, y, worldZ)
                            .setHeight(config().chunkMarkerHeight())
                            .setIntervals(config().chunkMarkerIntarval())
                            .setBrightness(computedBrightness)
                            .setColor(commonColor().slimeChunk())
                            );
                }
            }
        }

    }

    private void checkSlimeSpawn(int px, int py, int pz)
    {
        // TODO: 周囲スキャンの重複コードの削除 スポーンチェッカーモードにも同一処理がある

        final int xRange = commonState().horizontalRange().current();
        final int zRange = xRange;
        final int yRange = commonState().verticalRange().current();

        final int firstX = Ints.saturatedCast((long) px - (long) xRange);
        final int lastX = Ints.saturatedCast((long) px + (long) xRange);

        final int firstZ = Ints.saturatedCast((long) pz - (long) zRange);
        final int lastZ = Ints.saturatedCast((long) pz + (long) zRange);

        final int fisstY = Math.min(
                Ints.saturatedCast((long) py + (long) yRange),
                consts.scanRangeLimitMaxY);

        final int lastY = Math.max(
                Ints.saturatedCast((long) py - (long) yRange),
                consts.scanRangeLimitMinY);

        for (int x = firstX; x <= lastX; x++)
        {
            for (int z = firstZ; z <= lastZ; z++)
            {
                for (int y = fisstY; y >= lastY; y--)
                {
                    spawnCheck.slimeSpawnCheckAt(x, y, z);
                }
            }
        }
    }

    @Override
    public void renderIngame(long tickCount, float partialTick)
    {
        for (Marker<?> marker : chunkMarkers)
            marker.doRender(tickCount, partialTick);

        for (Marker<?> marker : spawnCheck.markers())
            marker.doRender(tickCount, partialTick);
    }

}
