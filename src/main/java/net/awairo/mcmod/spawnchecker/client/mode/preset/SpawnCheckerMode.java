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

import com.google.common.primitives.Ints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.MathHelper;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.marker.SpawnPointMarker;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.client.mode.preset.PresetModeConfigs.SpawnCheckerConfig;

/**
 * スポーンチェッカーモード.
 * 
 * @author alalwww
 */
public final class SpawnCheckerMode extends ModeBase<SpawnCheckerMode>
{
    public static final String ID = "spawnchecker";

    private final Minecraft game = Minecraft.getMinecraft();
    private final ConstantsConfig consts = ConstantsConfig.instance();

    private SpawnCheck surfaceCheck;

    /**
     * Constructor.
     */
    public SpawnCheckerMode()
    {
        super(ID, 10);
        setNameKey("spawnchecker.mode.spawnchecker");
    }

    @Override
    public String iconResourceName()
    {
        return "spawnchecker:icon/spawn_checker.png";
    }

    @Override
    protected SpawnCheckerConfig config()
    {
        return PresetModeConfigs.instance().spawnCheckerMode;
    }

    @Override
    public void start()
    {
        // TODO: ディメンションごとの切り替え
        surfaceCheck = new SpawnCheck.Surface(this);
        surfaceCheck.color = commonColor();
    }

    @Override
    public void stop()
    {
        surfaceCheck = null;
    }

    @Override
    public void onUpdate()
    {
        surfaceCheck.reset(options());

        if (!surfaceCheck.enable())
            return;

        surfaceCheck.setBrightness(commonState().brightness().current());

        // TODO: このあたりのリファクタリング、したい

        final EntityClientPlayerMP p = game.thePlayer;

        final int px = MathHelper.floor_double(p.posX);
        final int pz = MathHelper.floor_double(p.posZ);
        final int py = MathHelper.floor_double(p.posY) + (int) Math.floor(p.height);

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
                    surfaceCheck.checkMainTarget(x, y, z);
                    surfaceCheck.checkSubTarget(x, y, z);
                }
            }
        }
    }

    @Override
    public void renderIngame(long tickCount, float partialTick)
    {
        for (SpawnPointMarker marker : surfaceCheck.markers)
        {
            marker.doRender(tickCount, partialTick);
        }
    }

}
