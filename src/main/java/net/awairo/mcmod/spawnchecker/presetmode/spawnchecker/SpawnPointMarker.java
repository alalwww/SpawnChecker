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

import com.google.common.base.Supplier;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.marker.SkeletalMarker;
import net.awairo.mcmod.spawnchecker.client.model.MarkerModel;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.YOffsetHelper;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.model.GuidelineModel;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.model.SlimeSpawnPoint;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.model.SpawnPoint;

/**
 * スポーン場所を表すマーカー.
 * 
 * @author alalwww
 */
public final class SpawnPointMarker extends SkeletalMarker<SpawnPointMarker>
{
    /** スポーンポイントマーカーモデル. */
    public static final SpawnPoint SPAWN_POINT = new SpawnPoint();
    /** スライムスポーンポイントマーカーモデル. */
    public static final SlimeSpawnPoint SLIME_SPAWN_POINT = new SlimeSpawnPoint();

    private static final GuidelineModel GUIDELINE = new GuidelineModel();

    // TODO: 定数化
    private final double guidelineMaxLengthDefault = 64d;

    private SpawnPoint model;

    private double topOffset;

    private boolean computed;
    private boolean showMarker;
    private boolean showGuideline;

    private double guidelineLength;

    @Override
    @Deprecated
    protected MarkerModel model()
    {
        throw new InternalError();
    }

    @Override
    public void doRender(long tickCounts, float partialTicks)
    {
        setTicks(tickCounts, partialTicks);
        compute();

        if (showMarker)
        {
            model.setTopOffset(topOffset);
            model.setColor(argbColor);
            render(model);
        }

        if (showGuideline)
        {
            GUIDELINE.setLength(guidelineLength);
            if (model == SLIME_SPAWN_POINT)
            {
                GUIDELINE.setOffsetX(ConstantsConfig.instance().slimeMarkerOffset);
                GUIDELINE.setOffsetZ(ConstantsConfig.instance().slimeMarkerOffset);
            }
            else
            {
                GUIDELINE.setOffsetX(0);
                GUIDELINE.setOffsetZ(0);
            }

            GUIDELINE.setColor(argbColor);
            render(GUIDELINE);
        }
    }

    /**
     * モデルを設定します.
     * 
     * @param model
     * @return このインスタンス
     */
    public SpawnPointMarker setModel(SpawnPoint model)
    {
        this.model = model;
        return this;
    }

    /**
     * マーカーを表示します.
     * 
     * @return このインスタンス
     */
    public SpawnPointMarker showMarker(boolean showMarker)
    {
        this.showMarker = showMarker;
        return this;
    }

    /**
     * ガイドラインを表示します.
     * 
     * @return このインスタンス
     */
    public SpawnPointMarker showGuideline(boolean showGuideline)
    {
        this.showGuideline = showGuideline;
        return this;
    }

    @Override
    public SpawnPointMarker reset()
    {
        super.reset();
        computed = false;

        model = SPAWN_POINT;

        showMarker = false;
        showGuideline = false;

        topOffset = 0;
        guidelineLength = 0;

        return this;
    }

    private void compute()
    {
        if (computed) return;

        if (showMarker)
            topOffset = YOffsetHelper.getYOffset(posX, posY, posZ);

        if (showGuideline)
            guidelineLength = Math.min(posY + guidelineMaxLengthDefault, ConstantsConfig.instance().worldHeightMax);

        computed = true;
    }

    /**
     * @return supplier
     */
    public static Supplier<SpawnPointMarker> supplier()
    {
        return SpawnPointMarkerSupplier.INSTANCE;
    }

    enum SpawnPointMarkerSupplier implements Supplier<SpawnPointMarker>
    {
        INSTANCE;

        @Override
        public SpawnPointMarker get()
        {
            return new SpawnPointMarker();
        }
    }
}
