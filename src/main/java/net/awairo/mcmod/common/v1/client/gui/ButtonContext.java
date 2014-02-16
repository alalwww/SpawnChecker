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

/**
 * button context.
 * 
 * @author alalwww
 */
public final class ButtonContext
{
    public static final ButtonContext DONE = new ButtonContext(200, 20, "gui.done");

    private static int idCounter;

    public final int id;
    public final int width;
    public final int height;
    public final String description;

    public ButtonContext(int width, int height, String description)
    {
        id = idCounter++;
        this.width = width;
        this.height = height;
        this.description = description;
    }

}
