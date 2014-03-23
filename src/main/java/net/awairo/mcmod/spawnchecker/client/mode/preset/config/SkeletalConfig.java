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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeConfig;
import net.awairo.mcmod.spawnchecker.client.mode.preset.Options;

/**
 * プリセットモード用の設定のスケルトン.
 * 
 * @author alalwww
 */
abstract class SkeletalConfig extends ModeConfig.SubCategory
{
    protected SkeletalConfig(ModeConfig config)
    {
        super(config);
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
