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

import net.awairo.mcmod.common.v1.util.LimitedNumber;
import net.awairo.mcmod.spawnchecker.client.mode.Mode.CommonState;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;

/**
 * SpawnCheckerのいろいろな状態とか.
 * 
 * @author alalwww
 */
public final class State implements CommonState
{
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
        horizontalRange = LimitedNumber.ofInteger(config.horizontalRange)
                .min(1).max(32).step(1).build();

        verticalRange = LimitedNumber.ofInteger(config.verticalRange)
                .min(1).max(32).step(1).build();

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

    @Override
    public int computedBrightness()
    {
        return Brightness.compute(brightness.current());
    }
}
