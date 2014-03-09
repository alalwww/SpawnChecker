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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.marker.CachedSupplier;
import net.awairo.mcmod.spawnchecker.client.marker.SpawnPointMarker;
import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModels;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.CopiedLogics;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.client.mode.core.measuremententity.MeasurementEntities;

/**
 * スポーンチェック処理.
 * 
 * @author alalwww
 */
public abstract class SpawnCheck
{
    protected final Minecraft game = Minecraft.getMinecraft();
    protected final ConstantsConfig consts = ConstantsConfig.instance();

    /** このチェック処理を保持するモード. */
    protected final ModeBase<?> mode;

    /** コピーしてきたチェック処理. */
    protected final CopiedLogics copiedLogics = CopiedLogics.INSTANCE;

    /** 共通色設定. */
    public Mode.CommonColor color;

    /** オプション:無効. */
    public boolean disabled;

    /** 計算した明るさの値. */
    protected int computedBrightness;

    /** 当たり判定用のエンティティ. */
    protected MeasurementEntities measureEntities;

    /** マーカーインスタンスのキャッシュ. */
    public final CachedSupplier<SpawnPointMarker> cache;
    /** 次に描画するマーカーたち. */
    public final ArrayList<SpawnPointMarker> markers;

    /** ワールド変更検出用の現在のワールド. */
    private World currentWorld;

    /**
     * Constructor.
     */
    public SpawnCheck(ModeBase<?> mode)
    {
        this.mode = mode;
        cache = CachedSupplier.of(SpawnPointMarker.supplier());
        markers = Lists.newArrayListWithExpectedSize(consts.defaultSpawnCheckerMarkerListSize);
        currentWorld = game.theWorld;
        measureEntities = MeasurementEntities.of(currentWorld);
    }

    /**
     * 状態をリセットし、次の更新処理に備えます.
     * 
     * マーカーキャッシュと描画予定のマーカー一覧をリセットします。
     * ワールドが変更されていた場合、インスタンスキャッシュのリサイズ、
     * マーカー保持用リストのりサイズ、測定用エンティティの再生成も行います。
     * 
     * @param options オプションセット
     */
    public void reset(OptionSet options)
    {
        cache.recycle();
        markers.clear();

        // TODO: いらんかも モードマネージャーがワールド変更時stop/startするはず。
        if (currentWorld != game.theWorld)
        {
            currentWorld = game.theWorld;
            measureEntities = MeasurementEntities.of(currentWorld);
            cache.clearAll();
            markers.ensureCapacity(consts.defaultSpawnCheckerMarkerListSize);
        }

        setOptionSet(options);
    }

    /**
     * 現在のモードオプションを設定します.
     * 
     */
    protected void setOptionSet(OptionSet options)
    {
        disabled = options.contains(Options.DISABLED);
    }

    /**
     * このチェック処理が実施可能かを判定します
     * 
     * @return trueはこのチェック処理が有効である事を示す
     */
    public boolean enable()
    {
        // 無効が混じってたらチェックしない
        return !disabled;
    }

    /**
     * 現在のマーカーの明るさ設定値を設定します.
     * 
     * @param brightness 明るさ設定値(-5～5)
     */
    public void setBrightness(int brightness)
    {
        computedBrightness = consts.baseBrightness + brightness * consts.brightnessRatio;
    }

    /**
     * メインターゲットのスポーン判定.
     * 
     * @param x 座標
     * @param y 座標
     * @param z 座標
     */
    public abstract void checkMainTarget(int x, int y, int z);

    /**
     * スライムスポーン判定など追加判定.
     * 
     * @param x 座標
     * @param y 座標
     * @param z 座標
     */
    public abstract void checkSubTarget(int x, int y, int z);

    /**
     * 通常世界のスポーン判定.
     * 
     * @author alalwww
     */
    public static final class Surface extends SpawnCheck
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
        public Surface(ModeBase<?> mode)
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
        public void checkMainTarget(int x, int y, int z)
        {
            // この場所スポーンできるかな？
            if (!copiedLogics.canSpawnAtLocation(x, y, z)) return;

            // ここに光はあるのかな？
            if (!copiedLogics.canSpawnByLightLevel(x, y, z, consts.spawnableLightLevel)) return;

            // エンダーさんは入るかな？
            if (!copiedLogics.isColliding(x, y, z, measureEntities.enderman))
            {
                markers.add(cache.get()
                        .setModel(MarkerModels.SPAWN_POINT)
                        .setPoint(x, y, z)
                        .showMarker(force || forceMarker || (hasEnableItem && marker))
                        .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                        .setBrightness(computedBrightness)
                        .setColor(color.enderman()));
                return;
            }

            // スケルトンとかそのあたりはどうだろう？
            if (!copiedLogics.isColliding(x, y, z, measureEntities.standardSizeMob))
            {
                markers.add(cache.get()
                        .setModel(MarkerModels.SPAWN_POINT)
                        .setPoint(x, y, z)
                        .showMarker(force || forceMarker || (hasEnableItem && marker))
                        .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                        .setBrightness(computedBrightness)
                        .setColor(color.standardSizeMob()));
                return;
            }

            // クモなら、もしかして・・・？
            if (!copiedLogics.isColliding(x, y, z, measureEntities.spider))
            {
                markers.add(cache.get()
                        .setModel(MarkerModels.SPAWN_POINT)
                        .setPoint(x, y, z)
                        .showMarker(force || forceMarker || (hasEnableItem && marker))
                        .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                        .setBrightness(computedBrightness)
                        .setColor(color.spider()));
                return;
            }

        }

        @Override
        public void checkSubTarget(int x, int y, int z)
        {
            if (!slime) return;

            // スポーン可能？
            if (!slimeSpawnChecker.isSpawnable(x, y, z)) return;

            // 接触しないならスポーン可能
            if (!copiedLogics.isColliding(x, y, z, measureEntities.slime))
            {
                markers.add(cache.get()
                        .setModel(MarkerModels.SLIME_SPAWN_POINT)
                        .setPoint(x, y, z)
                        .showMarker(force || forceSlime || (hasEnableItem && slime))
                        .showGuideline(force || forceGuideline || (hasEnableItem && guideline))
                        .setBrightness(computedBrightness)
                        .setColor(color.slime()));
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
}
