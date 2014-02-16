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

import com.google.common.collect.ImmutableList;

import net.minecraft.client.resources.I18n;

/**
 * ToggleButton.
 * 
 * @author alalwww
 */
public class ToggleButton extends Button
{
    private final ImmutableList<ButtonContext> contexts;
    private ButtonContext currentContext;
    private int nextIndex;

    public static abstract class Handler implements ClickHandler
    {
        protected abstract ImmutableList<ButtonContext> contexts();

        protected abstract boolean handleOnClick(ToggleButton button);

        protected abstract int firstSelectedIndex();

        @Override
        public final void onClick(Button button)
        {
            if (handleOnClick((ToggleButton) button))
                ((ToggleButton) button).toggle();
        }
    }

    /**
     * Constructor.
     */
    public ToggleButton(int x, int y, final ToggleButton.Handler handler)
    {
        super(getFirstFrom(handler), x, y, handler);
        currentContext = context;
        this.contexts = handler.contexts();
        nextIndex = handler.firstSelectedIndex() + 1;
    }

    protected final void toggle()
    {
        currentContext = nextContext();
        id = currentContext.id;
        width = currentContext.width;
        height = currentContext.height;
        displayString = I18n.format(currentContext.description);
    }

    private ButtonContext nextContext()
    {
        if (nextIndex >= contexts.size())
            nextIndex = 0;

        return contexts.get(nextIndex++);
    }

    @Override
    ButtonContext context()
    {
        return currentContext;
    }

    private static ButtonContext getFirstFrom(Handler handler)
    {
        final ImmutableList<ButtonContext> context = handler.contexts();
        checkElementIndex(0, context.size());

        final int selected = handler.firstSelectedIndex();
        checkPositionIndex(selected, context.size());

        return context.get(selected);
    }

}
