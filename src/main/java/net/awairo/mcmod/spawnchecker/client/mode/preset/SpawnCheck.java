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

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.CopiedLogics;
import net.awairo.mcmod.spawnchecker.client.mode.core.measuremententity.MeasurementEntities;
import net.awairo.mcmod.spawnchecker.client.model.CachedSupplier;
import net.awairo.mcmod.spawnchecker.client.model.SpawnPoint;

/**
 * スポーンチェック処理.
 * 
 * @author alalwww
 */
public abstract class SpawnCheck
{
    private final Minecraft game = Minecraft.getMinecraft();
    private final ConstantsConfig consts = ConstantsConfig.instance();

    /** コピーしてきたチェック処理. */
    protected final CopiedLogics copiedLogics = CopiedLogics.INSTANCE;

    /** 共通色設定. */
    public Mode.CommonColor color;

    /** オプション:無効. */
    public boolean disabled;
    /** オプション:マーカー. */
    public boolean marker;
    /** オプション:ガイドライン. */
    public boolean guideline;
    /** オプション:常に表示. */
    public boolean force;

    /** 計算した明るさの値. */
    protected int computedBrightness;

    /** 当たり判定用のエンティティ. */
    protected MeasurementEntities measureEntities;

    /** マーカーインスタンスのキャッシュ. */
    public final CachedSupplier<SpawnPoint> cache;
    /** 次に描画するマーカーたち. */
    public final ArrayList<SpawnPoint> markers;

    /** ワールド変更検出用の現在のワールド. */
    private World currentWorld;

    /**
     * Constructor.
     */
    public SpawnCheck()
    {
        cache = CachedSupplier.of(SpawnPoint.supplier());
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
        marker = options.contains(Options.MARKER);
        guideline = options.contains(Options.GUIDELINE);
        force = options.contains(Options.FORCE);
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
        private boolean slime;

        @Override
        protected void setOptionSet(OptionSet options)
        {
            super.setOptionSet(options);
            slime = options.contains(Options.SLIME);
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
                        .setPoint(x, y, z)
                        .showMarker(marker)
                        .showGuideline(guideline)
                        .setBrightness(computedBrightness)
                        .setColor(color.enderman()));
                return;
            }

            // スケルトンとかそのあたりはどうだろう？
            if (!copiedLogics.isColliding(x, y, z, measureEntities.standardSizeMob))
            {
                markers.add(cache.get()
                        .setPoint(x, y, z)
                        .showMarker(marker)
                        .showGuideline(guideline)
                        .setBrightness(computedBrightness)
                        .setColor(color.standardSizeMob()));
                return;
            }

            // クモなら、もしかして・・・？
            if (!copiedLogics.isColliding(x, y, z, measureEntities.spider))
            {
                markers.add(cache.get()
                        .setPoint(x, y, z)
                        .showMarker(marker)
                        .showGuideline(guideline)
                        .setBrightness(computedBrightness)
                        .setColor(color.spider()));
                return;
            }

        }

        @Override
        public void checkSubTarget(int x, int y, int z)
        {
            if (!slime) return;

            // TODO: スライムのスポーン判定
        }

    }
}
