/*
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.v1.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * base of GuiScreen.
 * 
 * @author alalwww
 * @version 1.0
 */
public abstract class AbstractGuiScreen extends GuiScreen
{
    protected final GuiScreen parent;
    protected final List<Label> labels = Lists.newLinkedList();

    /**
     * Constructor.
     * 
     * @param parent parent GUI
     */
    public AbstractGuiScreen(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks)
    {
        drawDefaultBackground();

        for (Label label : labels)
        {
            if (label.centering)
                drawCenteredString(fontRendererObj, label.text, width / 2, label.y, label.color);
            else
                drawString(fontRendererObj, label.text, label.x, label.y, label.color);
        }

        super.drawScreen(mouseX, mouseY, renderPartialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        System.out.println(guiButton);
        cast(guiButton).onClick();
    }

    protected void addBackButton(ButtonContext context, int x, int y)
    {
        addButton(new BackButton(context, x, y, parent));
    }

    @SuppressWarnings("unchecked")
    protected void addButton(Button button)
    {
        buttonList.add(button);
    }

    protected void addLabel(Label label)
    {
        labels.add(label);
    }

    private static Button cast(GuiButton button)
    {
        return (Button) button;
    }
}
