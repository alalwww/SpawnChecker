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

import net.awairo.mcmod.spawnchecker.client.ClientManager;
import net.awairo.mcmod.spawnchecker.client.common.Settings;

/**
 * GuiManager.
 * 
 * @author alalwww
 */
public final class GuiManager extends ClientManager
{
    private static final GuiManager instance = new GuiManager();

    public static void load()
    {
    }

    private GuiManager()
    {
    }

    static GuiManager instance()
    {
        return instance;
    }

    @Override
    protected Settings settings()
    {
        return super.settings();
    }

}
