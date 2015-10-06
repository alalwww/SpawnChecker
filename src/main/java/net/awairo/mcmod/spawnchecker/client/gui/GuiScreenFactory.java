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

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * Mod設定画面ファクトリ.
 * 
 * @author alalwww
 */
public class GuiScreenFactory implements IModGuiFactory
{

    // TODO: インゲーム設定画面がまともに動くようになったらちゃんと設定する
    //    private static final Set<RuntimeOptionCategoryElement> categories = ImmutableSet
    //            .of(new RuntimeOptionCategoryElement("PARENT", "CHILD"));
    private static final Set<RuntimeOptionCategoryElement> CATEGORIES = ImmutableSet
            .of(new RuntimeOptionCategoryElement("HELP", "SpawnChecker"));

    @Override
    public void initialize(Minecraft minecraft)
    {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return GuiConfigScreen.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return CATEGORIES;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element)
    {
        // TODO: 1060現在まだ呼び出し側が実装されてないっぽい？
        System.out.println("\n\n----------------------------------------------------\n"
                + "SpawnCheckerGuiScreenFactory getHandlerFor\n\n"
                + "----------------------------------------------------");
        return new RuntimeOptionGuiHandler()
        {
            @Override
            public void paint(int x, int y, int w, int h)
            {
            }

            @Override
            public void close()
            {
            }

            @Override
            public void addWidgets(List<Gui> widgets, int x, int y, int w, int h)
            {
                widgets.add(new GuiButton(100, x + 10, y + 10, "HELLO"));
            }

            @Override
            public void actionCallback(int actionId)
            {
            }
        };
    }

}
