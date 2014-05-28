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

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.marker.CachedSupplier;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.presetmode.Options;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.measuremententity.MeasurementEntities;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.SpawnPointMarker;

/**
 * スポーンチェック処理.
 * 
 * @author alalwww
 */
public abstract class SkeletalWorldSpawnCheck implements WorldSpawnCheck
{
    protected final Minecraft game = Minecraft.getMinecraft();
    protected final ConstantsConfig consts = ConstantsConfig.instance();

    /** このチェック処理を保持するモード. */
    protected final ModeBase<?> mode;

    /** コピーしてきたチェック処理. */
    protected final CopiedLogics copiedLogics = CopiedLogics.INSTANCE;

    /** 当たり判定用のエンティティ. */
    protected final MeasurementEntities measureEntities = MeasurementEntities.INSTANCE;

    /** 共通色設定. */
    public Mode.CommonColor color;

    /** オプション:無効. */
    public boolean disabled;

    /** 計算した明るさの値. */
    protected int computedBrightness;

    /** マーカーインスタンスのキャッシュ. */
    public final CachedSupplier<SpawnPointMarker> cache;
    /** 次に描画するマーカーたち. */
    public final ArrayList<SpawnPointMarker> markers;

    /** ワールド変更検出用の現在のワールド. */
    private World currentWorld;

    /**
     * Constructor.
     */
    protected SkeletalWorldSpawnCheck(ModeBase<?> mode)
    {
        this.mode = mode;
        color = mode.commonColor();
        cache = CachedSupplier.of(SpawnPointMarker.supplier());
        markers = Lists.newArrayListWithExpectedSize(consts.defaultSpawnCheckerMarkerListSize);
        currentWorld = game.theWorld;
    }

    /**
     * 現在のモードオプションを設定します.
     */
    protected void setOptionSet(OptionSet options)
    {
        disabled = options.contains(Options.DISABLED);
    }

    @Override
    public boolean enable()
    {
        // 無効が混じってたらチェックしない
        return !disabled;
    }

    @Override
    public Iterable<SpawnPointMarker> markers()
    {
        return markers;
    }

    @Override
    public void setBrightness(int brightness)
    {
        computedBrightness = brightness;
    }

    @Override
    public void reset(OptionSet options)
    {
        cache.recycle();
        markers.clear();

        // TODO: いらんかも モードマネージャーがワールド変更時stop/startするはず。
        if (currentWorld != game.theWorld)
        {
            currentWorld = game.theWorld;
            cache.clearAll();
            markers.ensureCapacity(consts.defaultSpawnCheckerMarkerListSize);
        }

        setOptionSet(options);
    }
}
