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

import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

/**
 * label.
 * 
 * @author alalwww
 */
public class Label extends Gui
{
    public final int x;
    public final int y;
    public final int color;
    public final String text;
    public final boolean centering;

    /** センタリングラベル. */
    public Label(int y, String labelKey)
    {
        this(y, labelKey, 0xFFFFFF);
    }

    /** センタリングラベル. */
    public Label(int y, String labelKey, int color)
    {
        this(0, y, labelKey, color, true);
    }

    /** 左上からの絶対位置指定ラベル. */
    public Label(int x, int y, String labelKey)
    {
        this(x, y, labelKey, 0xFFFFFF);
    }

    /** 左上からの絶対位置指定ラベル. */
    public Label(int x, int y, String labelKey, int color)
    {
        this(x, y, labelKey, color, false);
    }

    private Label(int x, int y, String labelKey, int color, boolean centering)
    {
        this.x = x;
        this.y = y;
        this.text = I18n.format(labelKey);
        this.color = color;
        this.centering = centering;
    }
}
