package net.awairo.mcmod.spawnchecker;

import net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase;
import net.awairo.mcmod.spawnchecker.client.mode.preset.config.PresetModeConfigs;

/**
 * Skeletal preset mode.
 * 
 * @author alalwww
 *
 * @param <M> preset mode type
 */
public abstract class PresetMode<M extends PresetMode<M>> extends ModeBase<M>
{
    /**
     * @return preset mode config
     */
    protected PresetModeConfigs configs()
    {
        return PresetModes.configs;
    }

    /**
     * Constructor.
     * 
     * @param id mode id
     */
    protected PresetMode(String id)
    {
        super(id);
    }
}
