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

package net.awairo.mcmod.spawnchecker.client.mode.preset;

import net.awairo.mcmod.spawnchecker.client.common.SimpleOption;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * モードオプション
 * 
 * @author alalwww
 */
public final class Options
{
    public static final Mode.Option DISABLED = SimpleOption.of("disable", "spawnchecker.option.disabled");
    public static final Mode.Option MARKER = SimpleOption.of("marker", "spawnchecker.option.marker");
    public static final Mode.Option GUIDELINE = SimpleOption.of("guideline", "spawnchecker.option.guideline");
    public static final Mode.Option SLIME = SimpleOption.of("slime", "spawnchecker.option.slime");
    public static final Mode.Option GHAST = SimpleOption.of("ghast", "spawnchecker.option.ghast");
    public static final Mode.Option FORCE = SimpleOption.of("force", "spawnchecker.option.force");

    private Options()
    {
    }
}
