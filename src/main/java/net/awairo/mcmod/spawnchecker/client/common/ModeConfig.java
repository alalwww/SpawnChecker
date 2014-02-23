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

import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;
import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.mode.preset.SpawnCheckerMode;

/**
 * モード共通設定.
 * 
 * @author alalwww
 */
public class ModeConfig extends ConfigCategory
{
    private final Logger logger = LogManager.getLogger(SpawnChecker.MOD_ID);

    /** 選択しているモード. */
    public final Prop selectedMode;

    /** 水平範囲. */
    public final Prop horizontalRange;
    /** 垂直範囲. */
    public final Prop verticalRange;
    /** 明るさ. */
    public final Prop brightness;

    /** 更新の頻度(milliseconds). */
    public final Prop updateFrequency;

    /** 有効にするアイテム. */
    private final Prop enablingItemsProp;
    private final Map<String, Block> enablingItemMap = Maps.newHashMap();

    /**
     * Constructor.
     * 
     * @param config
     */
    protected ModeConfig(Config config)
    {
        super(config);

        setCategoryComment("mode common configuration.");

        // ----------

        selectedMode = getValueOf("selected_mode", SpawnCheckerMode.ID)
                .comment("last selected mode.");

        // ----------

        horizontalRange = getValueOf("scanrange.horizontal", 10)
                .comment("scan range of horizontal.\n"
                        + "(min: 1, max: 32, default: 10)");
        if (horizontalRange.getInt() < -1)
            horizontalRange.set(10);
        if (horizontalRange.getInt() > 32)
            horizontalRange.set(10);

        verticalRange = getValueOf("scanrange.vertical", 5)
                .comment("scan range of vertical.\n"
                        + "(min: 1, max: 32, default: 10)");
        if (verticalRange.getInt() < -1)
            verticalRange.set(10);
        if (verticalRange.getInt() > 32)
            verticalRange.set(10);

        brightness = getValueOf("marker_brightness", 0)
                .comment("marker brightness.\n"
                        + "(min -5, max+5, default: 0)");
        if (brightness.getInt() < -5)
            brightness.set(0);
        if (brightness.getInt() > 5)
            brightness.set(0);

        // ----------

        updateFrequency = getValueOf("update_frequency", 200)
                .comment("minimum time of update frequency millisecond.\n"
                        + "(min: 1, default: 200)");
        if (updateFrequency.getInt() < 1)
            updateFrequency.set(200);

        // ----------

        enablingItemsProp = getListOf("enabling_item", "torch", "lit_pumpkin")
                .comment("item id list.\n"
                        + "(default: torch, lit_pumpkin)");
    }

    @Override
    protected String configurationCategory()
    {
        return "mode";
    }

    /**
     * 有効化アイテムの集合を取得します.
     * 
     * <p>このメソッドは非スレッドセーフです.</p>
     * 
     * @return 有効化アイテムのブロック集合
     */
    public final Collection<Block> enablingItems()
    {
        enablingItemMap.clear();

        final String[] ids = enablingItemsProp.getStringList();
        for (String id : ids)
        {
            final Block block = (Block) Block.blockRegistry.getObject(id);

            if (block == null)
            {
                logger.warn("enabling item id(%s) is unknown block id.", id);
                continue;
            }

            enablingItemMap.put(id, block);
        }

        if (ids.length != enablingItemMap.size())
            enablingItemsProp.setList(enablingItemMap.keySet().toArray(new String[enablingItemMap.size()]));

        return enablingItemMap.values();
    }

}
