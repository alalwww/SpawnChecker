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

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.presetmode.Options;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.SpawnPointMarker;

/**
 * 通常世界のスポーン判定.
 * 
 * @author alalwww
 */
public class SurfaceSpawnCheck extends SkeletalWorldSpawnCheck
{
    private final SlimeSpawnChecker slimeSpawnChecker = SlimeSpawnChecker.newCheckerOfCurrentWorld();

    private boolean marker;
    private boolean slime;
    private boolean guideline;
    private boolean force;
    private boolean forceMarker;
    private boolean forceGuideline;
    private boolean forceSlime;

    private boolean hasEnableItem;

    /**
     * Constructor.
     * 
     * @param mode モード
     */
    public SurfaceSpawnCheck(ModeBase<?> mode)
    {
        super(mode);
    }

    @Override
    protected void setOptionSet(OptionSet options)
    {
        super.setOptionSet(options);
        force = options.contains(Options.FORCE);
        forceMarker = options.contains(Options.FORCE_MARKER);
        forceGuideline = options.contains(Options.FORCE_GUIDELINE);
        forceSlime = options.contains(Options.FORCE_SLIME);

        marker = force || forceMarker || options.contains(Options.MARKER);
        guideline = force || forceGuideline || options.contains(Options.GUIDELINE);

        slime = forceSlime || options.contains(Options.SLIME);

        hasEnableItem = hasEnableItem();
    }

    @Override
    public boolean enable()
    {
        if (super.enable())
        {
            // 強制表示中か有効化アイテム持ちで
            if (force || forceMarker || forceSlime || forceGuideline || hasEnableItem)

                // マーカー、スライムスポーンマーカー、ガイドラインのどれかが有効なら判定する
                return marker || guideline || slime;

        }

        return false;
    }

    @Override
    public void check(int x, int y, int z)
    {
        spawnCheckAt(x, y, z);
        slimeSpawnCheckAt(x, y, z);
    }

    /**
     * スポーンチェック.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void spawnCheckAt(int x, int y, int z)
    {
        // この場所スポーンできるかな？
        if (!copiedLogics.canSpawnAtLocation(x, y, z)) return;

        // ここに光はあるのかな？
        if (!copiedLogics.canSpawnByLightLevel(x, y, z, consts.spawnableLightLevel)) return;

        // エンダーさんは入るかな？
        if (!copiedLogics.isColliding(x, y, z, measureEntities.enderman))
        {
            markers.add(cache.get()
                    .setModel(SpawnPointMarker.SPAWN_POINT)
                    .setPoint(x, y, z)
                    .showMarker(force || forceMarker || (hasEnableItem && marker))
                    .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                    .setColorAndBrightness(color.enderman(), computedBrightness));
            return;
        }

        // スケルトンとかそのあたりはどうだろう？
        if (!copiedLogics.isColliding(x, y, z, measureEntities.standardSizeMob))
        {
            markers.add(cache.get()
                    .setModel(SpawnPointMarker.SPAWN_POINT)
                    .setPoint(x, y, z)
                    .showMarker(force || forceMarker || (hasEnableItem && marker))
                    .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                    .setColorAndBrightness(color.standardSizeMob(), computedBrightness));
            return;
        }

        // クモなら、もしかして・・・？
        if (!copiedLogics.isColliding(x, y, z, measureEntities.spider))
        {
            markers.add(cache.get()
                    .setModel(SpawnPointMarker.SPAWN_POINT)
                    .setPoint(x, y, z)
                    .showMarker(force || forceMarker || (hasEnableItem && marker))
                    .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                    .setColorAndBrightness(color.spider(), computedBrightness));
            return;
        }

    }

    public void slimeSpawnCheckAt(int x, int y, int z)
    {
        if (!slime) return;

        // スポーン可能？
        if (!slimeSpawnChecker.isSpawnable(x, y, z)) return;

        // 地面がある？
        if (!copiedLogics.canSpawnAtLocation(x, y, z)) return;

        // 接触しないならスポーン可能
        if (!copiedLogics.isColliding(x, y, z, measureEntities.slime))
        {
            markers.add(cache.get()
                    .setModel(SpawnPointMarker.SLIME_SPAWN_POINT)
                    .setPoint(x, y, z)
                    .showMarker(force || forceSlime || (hasEnableItem && slime))
                    .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                    .setColorAndBrightness(color.slime(), computedBrightness));
        }

    }

    /** @return true は有効化するアイテムを持ってる */
    private boolean hasEnableItem()
    {
        final ItemStack stack = game.thePlayer.inventory.getCurrentItem();

        return stack != null
                ? mode.enablingItems().contains(Block.getBlockFromItem(stack.getItem()))
                : false;
    }
}
