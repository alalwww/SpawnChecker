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
 * 汎用的な設定.
 * 
 * @author alalwww
 */
public class CommonConfig extends ConfigCategory
{
    private final Logger logger = LogManager.getLogger(SpawnChecker.MOD_ID);

    /** 設定の自動保存の試行間隔. */
    public final Prop saveInterval;

    /** Modの有効無効フラグ. */
    public final Prop enable;

    /** 状態情報の表示時間. */
    public final Prop informationDuration;
    /** 状態情報のフェードアウト再生時間. */
    public final Prop informationFadeout;
    /** 状態情報のX軸オフセット. */
    public final Prop informationOffsetX;
    /** 状態情報のY軸オフセット. */
    public final Prop informationOffsetY;
    /** アイコンの表示可否. */
    public final Prop showInformationIcon;
    /** アイコン2倍サイズのフラグ. */
    public final Prop informationIconDoubleSize;

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
     */
    public CommonConfig(Config config)
    {
        super(config);
        setCategoryComment(""); // TODO: 

        // ----------

        saveInterval = getValueOf("save_interval", "3000")
                .comment("interval time of saving configuration.\n"
                        + "(min: 0(realtime), default: 3000)");
        if (saveInterval.getInt() < 0)
            saveInterval.set(3000);

        enable = getValueOf("enable", true)
                .comment("enable/disable spawnchecker."
                        + "\n(default: true)");

        // ----------

        informationDuration = getValueOf("information.duration", 5000)
                .comment("duration time of information message millisecond.\n"
                        + "(min: 0(disable), default: 5000)");
        if (informationDuration.getInt() < 0)
            informationDuration.set(5000);

        informationFadeout = getValueOf("information.fadeout", 500)
                .comment("duration time of information duration time of fadeout animation millisecond.\n"
                        + "(min: 0(disable), default: 500)");
        if (informationFadeout.getInt() < 0)
            informationFadeout.set(500);

        informationOffsetX = getValueOf("information.offset_x", 5)
                .comment("information message horizontal offset.\n"
                        + "(min: 0, max: 1000, default: 5)");
        if (informationOffsetX.getInt() < 0)
            informationOffsetX.set(30);
        if (informationOffsetX.getInt() > 1000)
            informationOffsetX.set(30);

        informationOffsetY = getValueOf("information.offset_y", -50)
                .comment("information message vertical percent offset. 0 is middle. \n"
                        + "(min: -100, max: 100, default: -50)");
        if (informationOffsetY.getInt() < -100)
            informationOffsetY.set(-50);
        if (informationOffsetY.getInt() > 100)
            informationOffsetY.set(-50);

        showInformationIcon = getValueOf("information.show_icon", true)
                .comment("show icon on information message\n"
                        + "(default: true)");

        informationIconDoubleSize = getValueOf("information.icon_double_size", false)
                .comment("double size information icon size\n"
                        + "(default: false)");

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
        return "common";
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
