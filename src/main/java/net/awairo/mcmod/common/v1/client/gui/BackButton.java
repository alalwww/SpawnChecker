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

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiScreen;

/**
 * BackButton.
 * 
 * @author alalwww
 */
public class BackButton extends Button
{
    private enum ClickHandler implements Button.ClickHandler
    {
        INSTANCE;

        @Override
        public void onClick(Button button)
        {
            ((BackButton) button).back();
        }
    };

    private final GuiScreen parent;

    /**
     * Constructor.
     * 
     * @param type
     * @param x
     * @param y
     */
    public BackButton(ButtonContext type, int x, int y, GuiScreen parent)
    {
        super(type, x, y, ClickHandler.INSTANCE);
        this.parent = parent;
    }

    void back()
    {
        FMLClientHandler.instance().showGuiScreen(parent);
    }
}
