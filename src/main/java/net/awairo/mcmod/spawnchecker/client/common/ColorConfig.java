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

import java.awt.Color;

import com.google.common.base.Optional;

import net.awairo.mcmod.common.v1.util.Colors;
import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * 色設定.
 * 
 * @author alalwww
 */
public class ColorConfig extends ConfigCategory implements Mode.CommonColor
{
    /** 状態情報の文字色. */
    public final ColorSetting informationMessage;

    /** エンダーマン用マーカー色. */
    public final ColorSetting enderman;
    /** 一般的な大きさのMob用マーカー色. */
    public final ColorSetting standardSizeMob;
    /** スパイダー用マーカー色. */
    public final ColorSetting spider;
    /** スライム用マーカー色. */
    public final ColorSetting slime;
    /** ガスト用マーカー色. */
    public final ColorSetting ghast;

    /** スライムチャンクマーカーのライン色. */
    public final ColorSetting slimeChunk;

    /** スポーナー強調枠のライン色. */
    public final ColorSetting spawnerBorder;
    /** スポーナーの湧き範囲ライン色. */
    public final ColorSetting spawnerSpawnArea;
    /** スポーナーの湧き数上限チェック範囲ライン色. */
    public final ColorSetting spawnerSpawnLimitArea;
    /** スポーナーのスポーン可能マーカー色. */
    public final ColorSetting spawnerSpawnablePoint;
    /** スポーナーのスポーン不可能マーカー色. */
    public final ColorSetting spawnerUnspawnablePoint;
    /** スポーナーが起動する球体範囲マーカーの面の色. */
    public final ColorSetting spawnerActiveArea;
    /** スポーナーが起動する球体範囲マーカーのライン色. */
    public final ColorSetting spawnerActiveAreaLine;

    /**
     * Constructor.
     */
    public ColorConfig(Config config)
    {
        super(config);
        config.addCategoryComment("color settings");

        informationMessage = new ColorSetting("information", "#FFFFFF")
                .comment("information message color");

        enderman = new ColorSetting("enderman", "#6440FF00")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #6440FF00");

        standardSizeMob = new ColorSetting("standard_size_monster", "#64FFFF40")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #64FFFF40");

        spider = new ColorSetting("spider", "#644040FF")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #644040FF");

        slime = new ColorSetting("slime", "#6450E8C9")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #6450E8C9");

        ghast = new ColorSetting("ghast", "#644040FF")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #644040FF");

        slimeChunk = new ColorSetting("slime_chunk", "#6450E8C9")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #6450E8C9");

        spawnerBorder = new ColorSetting("spawner_border", "#FFFF0000")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #FFFF0000");

        spawnerSpawnArea = new ColorSetting("spawner_spawn_area", "#6400FF00")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #6400FF00");

        spawnerSpawnLimitArea = new ColorSetting("spawner_spawn_limit_area", "#640000FF")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #640000FF");

        spawnerSpawnablePoint = new ColorSetting("spawner_spawnable_point", "#40FFA080")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #40FFA080");

        spawnerUnspawnablePoint = new ColorSetting("spawner_unspawnable_point", "#40A0FFD6")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #40A0FFD6");

        spawnerActiveArea = new ColorSetting("spawner_active_area", "#40402010")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #40402010");

        spawnerActiveAreaLine = new ColorSetting("spawner_active_area_line", "#60FF8080")
                .comment("marker color #ARGB or #AARRGGBB\n(default: #60FF8080");

    }

    @Override
    protected String configurationCategory()
    {
        return "color";
    }

    @Override
    public Color informationMessage()
    {
        return informationMessage.get();
    }

    @Override
    public Color enderman()
    {
        return enderman.get();
    }

    @Override
    public Color standardSizeMob()
    {
        return standardSizeMob.get();
    }

    @Override
    public Color spider()
    {
        return spider.get();
    }

    @Override
    public Color slime()
    {
        return slime.get();
    }

    @Override
    public Color ghast()
    {
        return ghast.get();
    }

    @Override
    public Color slimeChunk()
    {
        return slimeChunk.get();
    }

    @Override
    public Color spawnerBorder()
    {
        return spawnerBorder.get();
    }

    @Override
    public Color spawnerSpawnArea()
    {
        return spawnerSpawnArea.get();
    }

    @Override
    public Color spawnerSpawnLimitArea()
    {
        return spawnerSpawnLimitArea.get();
    }

    @Override
    public Color spawnerSpawnablePoint()
    {
        return spawnerSpawnablePoint.get();
    }

    @Override
    public Color spawnerUnspawnablePoint()
    {
        return spawnerUnspawnablePoint.get();
    }

    @Override
    public Color spawnerActiveArea()
    {
        return spawnerActiveArea.get();
    }

    @Override
    public Color spawnerActiveAreaLine()
    {
        return spawnerActiveAreaLine.get();
    }

    /**
     * 色設定.
     * 
     * @author alalwww
     */
    public final class ColorSetting
    {
        private final Prop prop;
        private Color color;

        private ColorSetting(String key, String defaultValue)
        {
            this.prop = config.getValueOf(key, defaultValue);
            final Optional<Color> c = Colors.fromString(prop.getString());
            if (c.isPresent())
            {
                color = c.get();
                return;
            }

            // 設定がおかしいのでデフォ値
            color = Colors.fromString(defaultValue).get();
            prop.set(defaultValue);
        }

        private ColorSetting comment(String comment)
        {
            prop.comment("information message color");
            return this;
        }

        /**
         * 新しい色を設定します.
         * 
         * @param color 色
         */
        public void set(Color color)
        {
            this.color = color;
            prop.set(Colors.toString(color));
        }

        /**
         * 色を取得します.
         * 
         * @return 色
         */
        public Color get()
        {
            return color;
        }
    }
}
