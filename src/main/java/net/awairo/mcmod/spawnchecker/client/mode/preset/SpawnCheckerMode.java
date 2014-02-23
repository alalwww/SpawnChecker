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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.client.mode.core.measuremententity.MeasurementEntities;
import net.awairo.mcmod.spawnchecker.client.model.CachedSupplier;
import net.awairo.mcmod.spawnchecker.client.model.SpawnPoint;

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

    private CachedSupplier<SpawnPoint> cache;
    private ArrayList<SpawnPoint> markers;

    private MeasurementEntities measureEntities;

    private World currentWorld;

    private int computedBrightness;

    private boolean marker;
    private boolean guideline;
    private boolean force;

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
    public void initialize()
    {
        setModeConfig(PresetModeConfigs.instance().spawnCheckerMode);
    }

    @Override
    public void start()
    {
        cache = CachedSupplier.of(SpawnPoint.supplier());
        markers = Lists.newArrayListWithExpectedSize(consts.defaultSpawnCheckerMarkerListSize);
        currentWorld = game.theWorld;
        measureEntities = MeasurementEntities.of(currentWorld);
    }

    @Override
    public void stop()
    {
        cache = null;
        markers = null;
        currentWorld = null;
        measureEntities = null;
    }

    @Override
    public void onUpdate()
    {
        cache.recycle();
        markers.clear();

        if (currentWorld != game.theWorld)
        {
            currentWorld = game.theWorld;
            measureEntities = MeasurementEntities.of(currentWorld);
            cache.clearAll();
            markers.ensureCapacity(consts.defaultSpawnCheckerMarkerListSize);
        }

        final OptionSet options = options();

        // 無効は排他
        if (options.contains(Options.DISABLED)) return;

        marker = options.contains(Options.MARKER);
        guideline = options.contains(Options.GUIDELINE);
        force = options.contains(Options.FORCE);

        if (!force && !hasEnableItem()) return;
        if (!marker && !guideline) return;

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

        computedBrightness = consts.baseBrightness + commonState().brightness().current() * consts.brightnessRatio;

        for (int x = firstX; x <= lastX; x++)
        {
            for (int z = firstZ; z <= lastZ; z++)
            {
                for (int y = fisstY; y >= lastY; y--)
                {
                    check(x, y, z);

                    // TODO: スライムチャンク判定の追加
                }
            }

        }
    }

    /** @return true は有効化するアイテムを持ってる */
    private boolean hasEnableItem()
    {
        final ItemStack stack = game.thePlayer.inventory.getCurrentItem();

        return stack != null
                ? enablingItems().contains(Block.getBlockFromItem(stack.getItem()))
                : false;
    }

    // TODO: 判定に問題がないか再確認
    // TODO: ディメンション毎の判定処理の実装方針をきめる。対応する。
    private void check(int x, int y, int z)
    {
        if (!copiedLogics.canSpawnAtLocation(x, y, z)) return;

        if (!copiedLogics.canSpawnByLightLevel(x, y, z, 8)) return;

        if (!copiedLogics.isColliding(x, y, z, measureEntities.enderman))
        {
            markers.add(cache.get()
                    .setPoint(x, y, z)
                    .showGuideline(guideline)
                    .setBrightness(computedBrightness)
                    .setColor(commonColor().enderman()));
            return;
        }

        if (!copiedLogics.isColliding(x, y, z, measureEntities.standardSizeMob))
        {
            markers.add(cache.get()
                    .setPoint(x, y, z)
                    .showGuideline(guideline)
                    .setBrightness(computedBrightness)
                    .setColor(commonColor().standardSizeMob()));
            return;
        }

        if (!copiedLogics.isColliding(x, y, z, measureEntities.spider))
        {
            markers.add(cache.get()
                    .setPoint(x, y, z)
                    .showGuideline(guideline)
                    .setBrightness(computedBrightness)
                    .setColor(commonColor().spider()));
            return;
        }
    }

    @Override
    public void renderIngame(long tickCount, float partialTick)
    {
        for (SpawnPoint marker : markers)
        {
            marker.doRender(tickCount, partialTick);
        }
    }

}
