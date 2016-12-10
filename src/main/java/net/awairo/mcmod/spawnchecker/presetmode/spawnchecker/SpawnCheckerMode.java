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

package net.awairo.mcmod.spawnchecker.presetmode.spawnchecker;

import com.google.common.primitives.Ints;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;

import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;
import net.awairo.mcmod.spawnchecker.presetmode.SkeletalPresetMode;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.SurfaceSpawnCheck;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.WorldSpawnCheck;

/**
 * スポーンチェッカーモード.
 * 
 * @author alalwww
 */
public final class SpawnCheckerMode extends SkeletalPresetMode<SpawnCheckerMode> implements SelectableMode
{
    /** mode id. */
    public static final String ID = "spawnchecker";

    private final SpawnCheckerModeConfig config;
    private WorldSpawnCheck spawnCheck;

    /**
     * Constructor.
     * 
     * @param config 設定
     */
    public SpawnCheckerMode(SpawnCheckerModeConfig config)
    {
        super(ID);
        this.config = config;
    }

    @Override
    protected String modeNameKey()
    {
        return "spawnchecker.mode.spawnchecker";
    }

    @Override
    public int ordinal()
    {
        return 10;
    }

    @Override
    public int compareTo(SelectableMode o)
    {
        return SelectableMode.Comparator.compare(this, o);
    }

    @Override
    public String iconResourceName()
    {
        return "spawnchecker:icon/spawn_checker.png";
    }

    @Override
    protected SpawnCheckerModeConfig config()
    {
        return config;
    }

    @Override
    public void onStart()
    {
        // TODO: ディメンションごとの切り替え
        spawnCheck = new SurfaceSpawnCheck(this);
    }

    @Override
    public void onStop()
    {
        spawnCheck = null;
    }

    @Override
    public void onUpdate()
    {
        spawnCheck.reset(options());

        if (!spawnCheck.enable())
            return;

        spawnCheck.setBrightness(commonState().computedBrightness());

        // TODO: このあたりのリファクタリング、したい

        final EntityPlayerSP p = game.player;

        final int px = MathHelper.floor(p.posX);
        final int pz = MathHelper.floor(p.posZ);
        final int py = MathHelper.floor(p.posY) + (int) Math.floor(p.height);

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
                    spawnCheck.check(x, y, z);
                }
            }
        }
    }

    @Override
    public void renderIngame(long tickCounts, float partialTicks)
    {
        for (SpawnPointMarker marker : spawnCheck.markers())
        {
            marker.doRender(tickCounts, partialTicks);
        }
    }

}
