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
import net.awairo.mcmod.spawnchecker.client.mode.Mode.Option;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SpawnerVisualizerMode;

/**
 * Spawner Visualizer モードの設定.
 * 
 * @author alalwww
 */
public class SpawnerVisualizerConfig extends SkeletalConfig
{
    SpawnerVisualizerConfig(ModeConfig config)
    {
        super(config);

        setCategoryComment("preset mode: SpawnerVisualizer configurations.");
    }

    @Override
    protected String configurationCategory()
    {
        return SpawnerVisualizerMode.ID;
    }

    @Override
    protected List<OptionSet> defaultOptionSetList()
    {
        return ImmutableList.of(
                OptionSet.of(SPAWNER_HIDDEN),
                OptionSet.of(SPAWNER_SPAWN_AREA),
                OptionSet.of(SPAWNER_SPAWN_AREA, SPAWNER_SPAWN_LIMIT_AREA),
                OptionSet.of(SPAWNER_SPAWN_AREA, SPAWNER_SPAWN_LIMIT_AREA,
                        SPAWNER_SPAWNABLE_POINT, SPAWNER_UNSPAWNABLE_POINT),
                OptionSet.of(SPAWNER_SPAWN_AREA, SPAWNER_SPAWN_LIMIT_AREA,
                        SPAWNER_SPAWNABLE_POINT, SPAWNER_UNSPAWNABLE_POINT, SPAWNER_ACTIVATE_AREA));
    }

    @Override
    protected Set<Option> allOptions()
    {
        return ImmutableSet.of(
                SPAWNER_HIDDEN,
                SPAWNER_SPAWN_AREA,
                SPAWNER_SPAWN_LIMIT_AREA,
                SPAWNER_SPAWNABLE_POINT,
                SPAWNER_UNSPAWNABLE_POINT,
                SPAWNER_ACTIVATE_AREA);
    }

    @Override
    protected OptionSet defaultSelectedOptionSet()
    {
        return OptionSet.of(SPAWNER_SPAWN_AREA);
    }

}
