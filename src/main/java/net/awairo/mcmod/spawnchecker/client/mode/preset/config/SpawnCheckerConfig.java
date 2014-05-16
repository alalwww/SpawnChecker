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

package net.awairo.mcmod.spawnchecker.client.mode.preset.config;

import static net.awairo.mcmod.spawnchecker.client.mode.preset.Options.*;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SpawnCheckerMode;

/**
 * SpawnCheckerモードの設定.
 * 
 * @author alalwww
 */
public final class SpawnCheckerConfig extends SkeletalConfig
{
    SpawnCheckerConfig(ModeConfig config)
    {
        super(config);

        setCategoryComment("preset mode: SpawnChecker configurations.");
    }

    @Override
    protected String configurationCategory()
    {
        return SpawnCheckerMode.ID;
    }

    @Override
    protected List<OptionSet> defaultOptionSetList()
    {
        return ImmutableList.of(
                OptionSet.of(DISABLED),
                OptionSet.of(MARKER),
                OptionSet.of(FORCE_MARKER),
                OptionSet.of(MARKER, GUIDELINE),
                OptionSet.of(FORCE_MARKER, GUIDELINE),
                OptionSet.of(MARKER, GUIDELINE, FORCE),
                OptionSet.of(MARKER, SLIME),
                OptionSet.of(FORCE_MARKER, SLIME),
                OptionSet.of(MARKER, FORCE_SLIME),
                OptionSet.of(MARKER, SLIME, GUIDELINE),
                OptionSet.of(FORCE_MARKER, SLIME, GUIDELINE),
                OptionSet.of(MARKER, FORCE_SLIME, GUIDELINE),
                OptionSet.of(MARKER, SLIME, GUIDELINE, FORCE)
                );
    }

    @Override
    protected Set<Mode.Option> allOptions()
    {
        return ImmutableSet.of(
                DISABLED,
                MARKER,
                GUIDELINE,
                SLIME,
                FORCE,
                FORCE_MARKER,
                FORCE_SLIME,
                FORCE_GUIDELINE
                );
    }

    @Override
    protected OptionSet defaultSelectedOptionSet()
    {
        return OptionSet.of(MARKER);
    }

}
