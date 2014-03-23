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

package net.awairo.mcmod.spawnchecker.client.marker;

import com.google.common.base.Supplier;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.marker.model.MarkerModels;
import net.awairo.mcmod.spawnchecker.client.marker.model.SpawnPoint;

/**
 * スポーン場所を表すマーカー.
 * 
 * @author alalwww
 */
public final class SpawnPointMarker extends SkeletalMarker<SpawnPointMarker>
{
    // TODO: 定数化
    static final double GUIDELINE_LENGTH = 64d;

    private SpawnPoint model;

    private double topOffset;

    private boolean computed;
    private boolean showMarker;
    private boolean showGuideline;

    private double guidelineLength;

    public void doRender(long tickCount, float partialTick)
    {
        compute();

        if (showMarker)
        {
            model.setTopOffset(topOffset);
            render(model, tickCount, partialTick);
        }

        if (showGuideline)
        {
            MarkerModels.GUIDELINE.setLength(guidelineLength);
            if (model == MarkerModels.SLIME_SPAWN_POINT)
            {
                MarkerModels.GUIDELINE.setOffsetX(ConstantsConfig.instance().slimeMarkerOffset);
                MarkerModels.GUIDELINE.setOffsetZ(ConstantsConfig.instance().slimeMarkerOffset);
            }
            else
            {
                MarkerModels.GUIDELINE.setOffsetX(0);
                MarkerModels.GUIDELINE.setOffsetZ(0);
            }

            render(MarkerModels.GUIDELINE, tickCount, partialTick);
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

        model = MarkerModels.SPAWN_POINT;

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
            guidelineLength = Math.min(posY + GUIDELINE_LENGTH, ConstantsConfig.instance().worldHeightMax);

        computed = true;
    }

    /**
     * @return supplier
     */
    public static Supplier<SpawnPointMarker> supplier()
    {
        return new Supplier<SpawnPointMarker>()
        {
            @Override
            public SpawnPointMarker get()
            {
                return new SpawnPointMarker();
            }
        };
    }
}
