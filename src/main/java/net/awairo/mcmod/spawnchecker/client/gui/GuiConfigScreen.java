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

package net.awairo.mcmod.spawnchecker.client.gui;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.GuiScreen;

import net.awairo.mcmod.common.v1.client.gui.ButtonContext;
import net.awairo.mcmod.common.v1.client.gui.Label;
import net.awairo.mcmod.common.v1.client.gui.ToggleButton;

/**
 * SpawnChecker Configuration GUI.
 * 
 * @author alalwww
 */
public final class GuiConfigScreen extends ModGuiScreen
{
    public GuiConfigScreen(GuiScreen parent)
    {
        super(parent);
    }

    @Override
    protected String titleLabelKey()
    {
        return "spawnchecker.gui.title";
    }

    @Override
    public void initialize()
    {
        addEnableDisableButton();
    }

    private void addEnableDisableButton()
    {
        int x, y;

        // 左から30上から60
        x = 30;
        y = 60;
        addLabel(new Label(x, y, "spawnchecker.gui.toggletest"));

        // 右から20、上から60
        final int buttonWidth = 100;
        final int buttonHeight = 20;

        final ImmutableList<ButtonContext> enableButtonContexts = ImmutableList.of(
                new ButtonContext(buttonWidth, buttonHeight, "spawnchecker.gui.disable"),
                new ButtonContext(buttonWidth, buttonHeight, "spawnchecker.gui.enable"));

        x = width - buttonWidth - 20;
        y = 60;

        addButton(new ToggleButton(x, y, new ToggleButton.Handler()
        {
            @Override
            protected ImmutableList<ButtonContext> contexts()
            {
                return enableButtonContexts;
            }

            @Override
            protected int firstSelectedIndex()
            {
                final int enable = 0;
                final int disable = 1;
                return settings().common().enable.getBoolean() ? enable : disable;
            }

            @Override
            protected boolean handleOnClick(ToggleButton button)
            {
                return true;
            }
        }));
    }
}
