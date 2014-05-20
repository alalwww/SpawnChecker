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

import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;

/**
 * 選択起動モードのコンテナー.
 * 
 * @author alalwww
 */
final class SelectableModeContainer extends ModeContainer<SelectableMode> implements OperatableModeContainer
{
    private int selectedModeCursor;
    private int cursorShiftCounter;

    private boolean started;

    @Override
    void initialize(Settings settings)
    {
        super.initialize(settings);

        final SelectableMode lastMode = getById(settings.mode().selectedMode.getString());
        selectedModeCursor = indexOf(lastMode);

        if (selectedModeCursor < 0)
            selectedModeCursor = 0;
    }

    @Override
    public void startIfStopped()
    {
        if (started) return;

        current().start();
        started = true;
    }

    @Override
    public void stopIfStarted()
    {
        if (!started) return;

        current().stop();
        started = false;
    }

    @Override
    public void update()
    {
        if (started)
            current().update();
    }

    @Override
    public void renderGui(long tickCounts, float partialTicks)
    {
        if (started)
            current().renderGui(tickCounts, partialTicks);
    }

    @Override
    public void renderIngame(long tickCounts, float partialTicks)
    {
        if (started)
            current().renderIngame(tickCounts, partialTicks);
    }

    @Override
    public void onUpKeyPress(boolean shift, boolean alt)
    {
        if (started)
            current().onUpKeyPress(shift, alt);
    }

    @Override
    public void onDownKeyPress(boolean shift, boolean alt)
    {
        if (started)
            current().onDownKeyPress(shift, alt);
    }

    @Override
    public void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (started)
            current().onPlusKeyPress(ctrl, shift, alt);
    }

    @Override
    public void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (started)
            current().onMinusKeyPress(ctrl, shift, alt);
    }

    void next()
    {
        cursorShiftCounter++;
    }

    void prev()
    {
        cursorShiftCounter--;
    }

    boolean modeChangeScheduled()
    {
        return cursorShiftCounter != 0;
    }

    void changeNewModeIfScheduled()
    {
        // 予定がないなら何もしない
        if (!modeChangeScheduled())
            return;

        int newCursor = selectedModeCursor + cursorShiftCounter;

        final int size = size();

        // TODO: モード選択にループ機能を設けてもいいかも

        if (newCursor < 0)
            newCursor = 0;

        if (newCursor >= size)
            newCursor = size - 1;

        stopIfStarted();

        selectedModeCursor = newCursor;
        cursorShiftCounter = 0;

        settings().mode().selectedMode.set(current().id());
    }

    private SelectableMode current()
    {
        return getByIndex(selectedModeCursor);
    }
}
