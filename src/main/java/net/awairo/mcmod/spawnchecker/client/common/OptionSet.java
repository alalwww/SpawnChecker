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

package net.awairo.mcmod.spawnchecker.client.common;

import java.util.Arrays;
import java.util.LinkedHashSet;

import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * Option set.
 * 
 * @author alalwww
 */
public class OptionSet extends LinkedHashSet<Mode.Option>
{
    public static OptionSet of(final Mode.Option... options)
    {
        final OptionSet set = new OptionSet();
        set.addAll(Arrays.asList(options));
        return set;
    }
}
