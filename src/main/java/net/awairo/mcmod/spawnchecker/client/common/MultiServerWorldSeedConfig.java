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

import static com.google.common.base.Preconditions.*;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;
import net.awairo.mcmod.spawnchecker.SpawnChecker;

/**
 * マルチのシード値設定.
 * 
 * @author alalwww
 */
public class MultiServerWorldSeedConfig extends ConfigCategory
{
    private static final Logger LOGGER = LogManager.getLogger(SpawnChecker.MOD_ID);

    // @see ServerAddress
    private static final Integer DEFAULT_PORT = 25565;

    private static MultiServerWorldSeedConfig instance;

    /**
     * @return seed configurations.
     */
    public static MultiServerWorldSeedConfig instance()
    {
        return instance;
    }

    private final Prop worldSeedsProp;

    /**
     * 行キーにホスト名またはIPアドレス文字列、列キーにポート、値にシード値を持った不変のテーブル.
     */
    public final ImmutableTable<String, Integer, Long> serverWorldSeedMap;

    /**
     * Constructor.
     * 
     * @param config
     */
    public MultiServerWorldSeedConfig(Config config)
    {
        super(config);
        checkState(instance == null);
        instance = this;

        setCategoryComment("server world seed configurations.");

        worldSeedsProp = getListOf("seeds", new String[] {})
                .comment("multiserver world seed configurations.\n"
                        + "port number is optional. default is 25565 if you do not specify.\n"
                        + "pattern: \"[SERVER ADDRESS]=[WORLD SEED]\" (* need double quotes *)\n"
                        + "ex)\n"
                        + "S:seeds <\n"
                        + "    \"example.com:25565=123456789123456789\"\n"
                        + "    \"localhost:25565=-987654321987654321\"\n"
                        + "    \"192.168.0.1:25565=000000000000000000\"\n"
                        + ">");

        serverWorldSeedMap = load(worldSeedsProp.getStringList());
        if (!serverWorldSeedMap.isEmpty())
            LOGGER.info("world seed configuration loaded: {}", serverWorldSeedMap);

        worldSeedsProp.setList(Collections2
                .transform(serverWorldSeedMap.cellSet(), new Function<Table.Cell<String, Integer, Long>, String>()
                {
                    @Override
                    public String apply(Cell<String, Integer, Long> input)
                    {
                        return String.format("\"%s:%s=%s\"",
                                input.getRowKey(),
                                input.getColumnKey(),
                                input.getValue());
                    }
                })
                .toArray(new String[serverWorldSeedMap.size()]));
    }

    private static ImmutableTable<String, Integer, Long> load(String[] worldSeeds)
    {
        final Pattern pattern = Pattern.compile("^\"([^\"]+)\"$");
        final Splitter keyValue = Splitter.on('=');
        final Splitter hostPort = Splitter.on(':');

        final ImmutableTable.Builder<String, Integer, Long> builder = ImmutableTable.builder();
        for (String seedConfig : worldSeeds)
        {
            final Matcher m = pattern.matcher(seedConfig);
            if (!m.matches())
            {
                LOGGER.warn("removed {} from world seed configurations. (illegal format)", seedConfig);
                continue;
            }

            final Iterator<String> kv = keyValue.split(m.group(1)).iterator();

            if (!kv.hasNext())
            {
                LOGGER.warn("removed {} from world seed configurations. (illegal address)", seedConfig);
                continue;
            }

            final Iterator<String> hp = hostPort.split(kv.next()).iterator();
            if (!kv.hasNext())
            {
                LOGGER.warn("removed {} from world seed configurations. (illegal seed)", seedConfig);
                continue;
            }
            if (!hp.hasNext())
            {
                LOGGER.warn("removed {} from world seed configurations. (illegal address)", seedConfig);
                continue;
            }

            final Long seed = Longs.tryParse(kv.next());
            if (seed == null)
            {
                LOGGER.warn("removed {} from world seed configurations. (illegal seed value)", seedConfig);
                continue;
            }

            final String host = hp.next();
            final Integer port = hp.hasNext() ? Ints.tryParse(hp.next()) : DEFAULT_PORT;

            builder.put(host, port, seed);
        }

        return builder.build();
    }

    @Override
    protected String configurationCategory()
    {
        return "world-seeds";
    }
}
