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

import net.awairo.mcmod.spawnchecker.client.mode.ConditionalMode;

/**
 * 条件起動モードのコンテナー.
 * 
 * @author alalwww
 */
final class ConditionalModeContainer extends ModeContainer<ConditionalMode> implements OperatableModeContainer
{
    private ConditionalMode current;
    private boolean started;

    @Override
    public void startIfStopped()
    {
        if (current == null || started)
            return;

        current.start();
        started = true;
    }

    @Override
    public void stopIfStarted()
    {
        if (current == null || !started)
            return;

        current.stop();
        started = false;
    }

    @Override
    public void update()
    {
        if (current != null && started)
            current.update();
    }

    @Override
    public void renderGui(long tickCounts, float partialTicks)
    {
        if (current != null && started)
            current.renderGui(tickCounts, partialTicks);
    }

    @Override
    public void renderIngame(long tickCounts, float partialTicks)
    {
        if (current != null && started)
            current.renderIngame(tickCounts, partialTicks);
    }

    @Override
    public void onUpKeyPress(boolean shift, boolean alt)
    {
        if (current != null && started)
            current.onUpKeyPress(shift, alt);
    }

    @Override
    public void onDownKeyPress(boolean shift, boolean alt)
    {
        if (current != null && started)
            current.onDownKeyPress(shift, alt);
    }

    @Override
    public void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (current != null && started)
            current.onPlusKeyPress(ctrl, shift, alt);
    }

    @Override
    public void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (current != null && started)
            current.onMinusKeyPress(ctrl, shift, alt);
    }

    boolean enabled()
    {
        // 有効なモードがあるか検索
        for (ConditionalMode mode : modes())
        {
            if (mode == current)
            {
                // 現在モードが有効ならそれより優先度の低いモードは無視
                if (current.enabled())
                    return true;
            }

            if (mode.enabled())
            {
                // 有効になれるモードがあった
                stopIfStarted();
                current = mode;
                return true;
            }
        }

        // 有効になれるモードがない
        stopIfStarted();
        current = null;

        return false;
    }

}
