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

import static com.google.common.base.Preconditions.*;

import com.google.common.base.MoreObjects;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

/**
 * GuiButton wrapper.
 * 
 * @author alalwww
 */
public class Button extends GuiButton
{
    final ClickHandler function;
    final ButtonContext context;

    /**
     * Constructor.
     * 
     * @param context
     * @param x
     * @param y
     * @param function
     */
    public Button(ButtonContext context, int x, int y, ClickHandler function)
    {
        super(checkNotNull(context, "type").id, x, y, context.width, context.height,
                I18n.format(context.description));
        this.context = context;
        this.function = checkNotNull(function, "function");
    }

    void onClick()
    {
        function.onClick(this);
    }

    public interface ClickHandler
    {
        void onClick(Button button);
    }

    void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    ButtonContext context()
    {
        return context;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("context", context())
                .add("id", id)
                .add("x", x)
                .add("y", y)
                .add("width", width)
                .add("height", height)
                .add("label", displayString)
                .toString();
    }
}
