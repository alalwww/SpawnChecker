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

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;

/**
 * 汎用的な設定.
 * 
 * @author alalwww
 */
public class CommonConfig extends ConfigCategory
{

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

    /**
     * Constructor.
     */
    public CommonConfig(Config config)
    {
        super(config);
        setCategoryComment("common configurations.");

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
    }

    @Override
    protected String configurationCategory()
    {
        return "common";
    }

}
