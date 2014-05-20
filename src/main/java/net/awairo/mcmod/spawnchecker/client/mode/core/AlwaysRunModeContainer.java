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

package net.awairo.mcmod.spawnchecker.client.mode.core;

import net.awairo.mcmod.spawnchecker.client.mode.AlwaysRunMode;

/**
 * 常に実行されるモードのコンテナー.
 * 
 * @author alalwww
 */
final class AlwaysRunModeContainer extends ModeContainer<AlwaysRunMode>
{
    private boolean started;

    @Override
    void startIfStopped()
    {
        if (started)
            return;

        for (AlwaysRunMode mode : modes())
            mode.start();

        started = true;
    }

    @Override
    void stopIfStarted()
    {
        if (!started)
            return;

        for (AlwaysRunMode mode : modes())
            mode.stop();

        started = false;
    }

    @Override
    void update()
    {
        if (!started)
            return;

        for (AlwaysRunMode mode : modes())
            mode.update();
    }

    @Override
    void renderGui(long tickCounts, float partialTicks)
    {
        if (!started)
            return;

        for (AlwaysRunMode mode : modes())
            mode.renderGui(tickCounts, partialTicks);
    }

    @Override
    void renderIngame(long tickCounts, float partialTicks)
    {
        if (!started)
            return;

        for (AlwaysRunMode mode : modes())
            mode.renderIngame(tickCounts, partialTicks);
    }

}
