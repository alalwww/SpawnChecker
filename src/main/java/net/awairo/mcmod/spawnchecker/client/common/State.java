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

package net.awairo.mcmod.spawnchecker.client.common;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.World;

import net.awairo.mcmod.common.v1.util.LimitedNumber;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode.CommonState;

/**
 * SpawnCheckerのいろいろな状態とか.
 * 
 * @author alalwww
 */
public final class State implements CommonState
{
    /**
     * key direction.
     * 
     * @author alalwww
     */
    public enum Direction
    {
        UP, DOWN;
    }

    private final ModeConfig config;

    private World currentWorld;

    /** 現在モード. */
    private Mode currentMode;

    /** 変更の予約リスト. */
    final List<Direction> newModeDirections = Lists.newLinkedList();

    private final LimitedNumber<Integer> horizontalRange;
    private final LimitedNumber<Integer> verticalRange;
    private final LimitedNumber<Integer> brightness;

    /**
     * Constructor.
     * 
     * @param config モードの設定
     */
    public State(ModeConfig config)
    {
        this.config = checkNotNull(config, "config");

        horizontalRange = LimitedNumber.ofInteger(config.horizontalRange)
                .min(0).max(32).step(1).build();

        verticalRange = LimitedNumber.ofInteger(config.verticalRange)
                .min(0).max(32).step(1).build();

        brightness = LimitedNumber.ofInteger(config.brightness)
                .min(-5).max(5).step(1).build();
    }

    @Override
    public LimitedNumber<Integer> horizontalRange()
    {
        return horizontalRange;
    }

    @Override
    public LimitedNumber<Integer> verticalRange()
    {
        return verticalRange;
    }

    @Override
    public LimitedNumber<Integer> brightness()
    {
        return brightness;
    }

    /**
     * @return 現在のワールド
     */
    public World currentWorld()
    {
        return currentWorld;
    }

    /**
     * @param newWorld 新しい現在のワールド
     */
    public void setWorld(World newWorld)
    {
        currentWorld = newWorld;
    }

    /**
     * @return true はモードの初期化が済んでいる事を意味します
     */
    public boolean modeInitialized()
    {
        return currentMode != null;
    }

    /**
     * @return 現在のモード
     */
    public Mode currentMode()
    {
        return currentMode;
    }

    /**
     * 指定方向へのモード変更の計画を追加します.
     * 
     * @param direction モードの変更方向
     */
    public void changeMode(Direction direction)
    {
        newModeDirections.add(checkNotNull(direction));
    }

    /**
     * 予定されたモード変更計画を全て破棄し、指定のモードに変更します.
     */
    public void setNewMode(Mode newMode)
    {
        newModeDirections.clear();
        currentMode = newMode;
        config.selectedMode.set(currentMode.id());
    }

    /**
     * @return true はモード変更を予定している
     */
    public boolean modeChangeScheduled()
    {
        return !newModeDirections.isEmpty();
    }

    /**
     * @return 計画されたモード変更方向
     */
    public Iterable<Direction> directions()
    {
        return newModeDirections;
    }
}
