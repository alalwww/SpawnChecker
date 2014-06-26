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

package net.awairo.mcmod.spawnchecker.presetmode.slimechunkvisualizer;

import static net.awairo.mcmod.spawnchecker.presetmode.Options.*;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.awairo.mcmod.common.v1.util.config.Prop;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.Mode.Option;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;
import net.awairo.mcmod.spawnchecker.presetmode.SkeletalConfig;

/**
 * スライムチャンク可視化モードの設定.
 * 
 * @author alalwww
 */
public final class SlimeChunkVisualizerModeConfig extends SkeletalConfig
{
    private final Prop chunkScanRange;
    private final Prop chunkUpdateFrequency;

    private final Prop chunkMarkerHeight;
    private final Prop chunkMarkerIntarval;
    private final Prop chunkMarkerAnimationCycle;

    /**
     * Constructor.
     * 
     * @param config モード設定
     */
    public SlimeChunkVisualizerModeConfig(ModeConfig config)
    {
        super(config);

        setCategoryComment("preset mode: SlimeChunkVisualizer configurations.");

        // チャンクマーカーの検索範囲
        chunkScanRange = getValueOf("chunk_scan_range", 7)
                .comment("chunks of scan range for slime chunk.\n"
                        + "\n(min: 1, max: 15, default: 7)");

        if (chunkScanRange.getInt() < 1 || chunkScanRange.getInt() > 15)
            chunkScanRange.set(7);

        // チャンクマーカーの更新頻度
        chunkUpdateFrequency = getValueOf("chunk_update_frequency", 3000)
                .comment("minimum time of update frequency milliseconds for slime chunk markers.\n"
                        + "\n(min: 1, default: 3000)");

        if (chunkUpdateFrequency.getInt() < 1)
            chunkUpdateFrequency.set(3000);

        // チャンクマーカーの高さ
        chunkMarkerHeight = getValueOf("chunk_marker_height", 64d)
                .comment("height of chunk marker.\n"
                        + "\n(min: 8, max: 255, default: 64)");

        if (chunkMarkerHeight.getDouble() < 8 || chunkMarkerHeight.getDouble() > 255)
            chunkMarkerHeight.set(64d);

        // チャンクマーカーの間隔
        chunkMarkerIntarval = getValueOf("chunk_marker_intarval", 1.5d)
                .comment("interval of chunk marker lines.\n"
                        + "\n(min: 0.5, max: 10, default: 1.5)");

        if (chunkMarkerIntarval.getDouble() < 0.5 || chunkMarkerIntarval.getDouble() > 10)
            chunkMarkerIntarval.set(1.5d);

        // チャンクマーカーのアニメーションの周期
        chunkMarkerAnimationCycle = getValueOf("chunk_marker_animation_cycle", 200)
                .comment("tick of one cycle for animation. 0 is stop animation.\n"
                        + "\n(min: 0, max: 10000, default: 200)");

        if (chunkMarkerAnimationCycle.getInt() < 0 || chunkMarkerAnimationCycle.getInt() > 10000)
            chunkMarkerAnimationCycle.set(200);

    }

    @Override
    protected String configurationCategory()
    {
        return SlimeChunkVisualizerMode.ID;
    }

    @Override
    protected List<OptionSet> defaultOptionSetList()
    {
        return ImmutableList.of(
                OptionSet.of(DISABLED),
                OptionSet.of(SLIME_CHUNK),
                OptionSet.of(FORCE_SLIME),
                OptionSet.of(FORCE_SLIME, FORCE_GUIDELINE),
                OptionSet.of(SLIME_CHUNK, FORCE_SLIME),
                OptionSet.of(SLIME_CHUNK, FORCE_SLIME, FORCE_GUIDELINE)
                );
    }

    @Override
    protected Set<Option> allOptions()
    {
        return ImmutableSet.of(
                DISABLED,
                SLIME_CHUNK,
                SLIME,
                GUIDELINE,
                FORCE_SLIME,
                FORCE_GUIDELINE
                );
    }

    @Override
    protected OptionSet defaultSelectedOptionSet()
    {
        return OptionSet.of(SLIME_CHUNK);
    }

    /** @return チャンクマーカーの検索範囲(チャンク). */
    public int chunkScanRange()
    {
        return chunkScanRange.getInt();
    }

    /** @return チャンク情報の更新間隔. */
    public int chunkUpdateFrequency()
    {
        return chunkUpdateFrequency.getInt();
    }

    /** @return チャンクマーカーの高さ. */
    public double chunkMarkerHeight()
    {
        return chunkMarkerHeight.getDouble();
    }

    /** @return チャンクマーカー線の間隔. */
    public double chunkMarkerIntarval()
    {
        return chunkMarkerIntarval.getDouble();
    }

    /** @return チャンクマーカー線の間隔. */
    public int chunkMarkerAnimationCycle()
    {
        return chunkMarkerAnimationCycle.getInt();
    }

}
