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

package net.awairo.mcmod.spawnchecker.presetmode;

import net.minecraft.client.Minecraft;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;

/**
 * Skeletal preset mode.
 * 
 * @author alalwww
 *
 * @param <M> preset mode type
 */
public abstract class SkeletalPresetMode<M extends SkeletalPresetMode<M>> extends ModeBase<M>
{
    protected final Minecraft game = Minecraft.getMinecraft();
    protected final ConstantsConfig consts = ConstantsConfig.instance();

    /**
     * Constructor.
     * 
     * @param id mode id
     */
    protected SkeletalPresetMode(String id)
    {
        super(id);
    }
}
