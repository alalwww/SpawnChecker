package net.awairo.mcmod.spawnchecker.presetmode;

import net.minecraft.client.Minecraft;

import net.awairo.mcmod.spawnchecker.PresetModes;
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

    static PresetModes MOD;

    protected PresetModes mod()
    {
        return MOD;
    }

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
