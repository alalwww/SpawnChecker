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

import static com.google.common.base.Preconditions.*;
import static net.awairo.mcmod.spawnchecker.client.mode.preset.Options.*;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import net.awairo.mcmod.spawnchecker.client.common.ModeConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfigChild;

/**
 * プリセットモードの設定.
 * 
 * @author alalwww
 */
public class PresetModeConfigs
{
    private static PresetModeConfigs instance;

    /**
     * @return モードから参照する用
     */
    static PresetModeConfigs instance()
    {
        return instance;
    }

    public final SpawnCheckerConfig spawnCheckerMode;

    public PresetModeConfigs(ModeConfig config)
    {
        checkState(instance == null);
        instance = this;

        spawnCheckerMode = new SpawnCheckerConfig(config);
    }

    /**
     * SpawnCheckerモードの設定.
     * 
     * @author alalwww
     */
    static class SpawnCheckerConfig extends ModeConfigChild
    {
        private SpawnCheckerConfig(ModeConfig config)
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
                    OptionSet.of(MARKER, SLIME, GUIDELINE),
                    OptionSet.of(FORCE_MARKER, SLIME, GUIDELINE),
                    OptionSet.of(MARKER, SLIME, GUIDELINE, FORCE));
        }

        @Override
        protected OptionSet defaultSelectedOptionSet()
        {
            return OptionSet.of(MARKER);
        }

        @Override
        protected OptionSet createOptionSetBy(ImmutableList<String> ids)
        {
            final OptionSet.Builder builder = OptionSet.builder();

            for (String id : ids)
            {
                final Optional<Mode.Option> option = Options.valueOf(id);
                if (option.isPresent())
                    builder.add(option.get());
            }

            return builder.build();
        }
    }
}
