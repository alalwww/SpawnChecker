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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.GuiScreen;

import net.awairo.mcmod.common.v1.client.gui.AbstractGuiScreen;
import net.awairo.mcmod.common.v1.client.gui.ButtonContext;
import net.awairo.mcmod.common.v1.client.gui.Label;
import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.Settings;

/**
 * 設定画面用GUIの抽象クラス.
 * 
 * @author alalwww
 */
abstract class ModGuiScreen extends AbstractGuiScreen
{
    final Logger logger = LogManager.getLogger(SpawnChecker.MOD_ID);

    /**
     * Constructor.
     * 
     * @param parent
     */
    protected ModGuiScreen(GuiScreen parent)
    {
        super(parent);
    }

    /**
     * @return このスクリーンのタイトルのラベルキー
     */
    protected abstract String titleLabelKey();

    /**
     * ボタンの配置やコントロール処理の追加を行います.
     */
    protected abstract void initialize();

    @Override
    public final void initGui()
    {
        super.initGui();
        addTitleLabel();
        initialize();
        addBackButton();
    }

    protected void addTitleLabel()
    {
        // センタリング 上から15
        final int y = 15;
        addLabel(new Label(y, titleLabelKey()));
    }

    protected void addBackButton()
    {
        // センタリング、下から38
        final int x = (width - ButtonContext.DONE.width) / 2;
        final int y = height - 38;
        super.addBackButton(ButtonContext.DONE, x, y);
    }

    protected final GuiManager manager()
    {
        return GuiManager.instance();
    }

    protected final Settings settings()
    {
        return manager().settings();
    }

}
