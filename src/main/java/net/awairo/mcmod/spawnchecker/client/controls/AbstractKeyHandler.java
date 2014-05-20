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

package net.awairo.mcmod.spawnchecker.client.controls;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * キーハンドラー抽象クラス.
 * 
 * @author alalwww
 */
abstract class AbstractKeyHandler
{
    static final List<AbstractKeyHandler> handlerList = Lists.newArrayList();

    final KeyBindingWrapper key;

    AbstractKeyHandler(KeyBindingWrapper key)
    {
        this.key = key;
        handlerList.add(this);
    }

    abstract void onKeyPress(boolean ctrl, boolean shift, boolean alt);
}
