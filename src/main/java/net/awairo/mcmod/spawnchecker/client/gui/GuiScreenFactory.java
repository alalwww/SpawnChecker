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

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * Mod設定画面ファクトリ.
 * 
 * @author alalwww
 */
public class GuiScreenFactory implements IModGuiFactory
{

    private static final Set<RuntimeOptionCategoryElement> CATEGORIES = Collections.emptySet();

    @Override
    public void initialize(Minecraft minecraft)
    {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(final GuiScreen parentScreen) {
        return new GuiConfigScreen(parentScreen);
    }

    @Override
    @Deprecated
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return null;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return CATEGORIES;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) { return null; }

}
