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

package net.awairo.mcmod.spawnchecker.presetmode;

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.awairo.mcmod.spawnchecker.client.common.SimpleOption;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * モードオプション
 * 
 * @author alalwww
 */
public final class Options
{
    /** 無効. */
    public static final Mode.Option DISABLED;
    /** マーカー. */
    public static final Mode.Option MARKER;
    /** ガイドライン. */
    public static final Mode.Option GUIDELINE;
    /** スライム. */
    public static final Mode.Option SLIME;
    /** ガスト. */
    public static final Mode.Option GHAST;
    /** 強制. */
    public static final Mode.Option FORCE;
    /** マーカー強制. */
    public static final Mode.Option FORCE_MARKER;
    /** ガイドライン強制. */
    public static final Mode.Option FORCE_GUIDELINE;
    /** スライムマーカー強制. */
    public static final Mode.Option FORCE_SLIME;

    /** スライムチャンクマーカー. */
    public static final Mode.Option SLIME_CHUNK;

    /** 非表示. */
    public static final Mode.Option SPAWNER_HIDDEN;
    /** スポーン範囲. */
    public static final Mode.Option SPAWNER_SPAWN_AREA;
    /** スポーン数制限範囲. */
    public static final Mode.Option SPAWNER_SPAWN_LIMIT_AREA;
    /** スポーン可能ポイント. */
    public static final Mode.Option SPAWNER_SPAWNABLE_POINT;
    /** スポーン不可能ポイント. */
    public static final Mode.Option SPAWNER_UNSPAWNABLE_POINT;
    /** スポーナー活性化範囲. */
    public static final Mode.Option SPAWNER_ACTIVATE_AREA;

    /** IDからオプションを取得するためのマップ. */
    public static final ImmutableMap<String, Optional<Mode.Option>> MAP;

    static
    {
        final Map<String, Optional<Mode.Option>> map = Maps.newHashMap();
        DISABLED = appendTo(map, "disable", 0);

        MARKER = appendTo(map, "marker", 10);
        GUIDELINE = appendTo(map, "guideline", 20);
        SLIME = appendTo(map, "slime", 30);
        GHAST = appendTo(map, "ghast", 40);
        FORCE = appendTo(map, "force", 50);
        FORCE_MARKER = appendTo(map, "force_marker", 60);
        FORCE_GUIDELINE = appendTo(map, "force_guideline", 70);
        FORCE_SLIME = appendTo(map, "force_slime", 80);

        SLIME_CHUNK = appendTo(map, "slime_chunk", 10);

        SPAWNER_HIDDEN = appendTo(map, "spawner_hidden", 10);
        SPAWNER_SPAWN_AREA = appendTo(map, "spawner_spawn_area", 20);
        SPAWNER_SPAWN_LIMIT_AREA = appendTo(map, "spawner_spawn_limit_area", 30);
        SPAWNER_SPAWNABLE_POINT = appendTo(map, "spawner_spawnable_point", 40);
        SPAWNER_UNSPAWNABLE_POINT = appendTo(map, "spawner_unspawnable_point", 50);
        SPAWNER_ACTIVATE_AREA = appendTo(map, "spawner_activate_area", 60);

        MAP = ImmutableMap.copyOf(map);
    }

    public static Optional<Mode.Option> valueOf(String id)
    {
        final Optional<Mode.Option> value = MAP.get(id);
        return value != null ? value : Optional.<Mode.Option> absent();
    }

    private static Mode.Option appendTo(Map<String, Optional<Mode.Option>> map, String id, int ordinal)
    {
        final Mode.Option option = SimpleOption.of(id, "spawnchecker.option." + id, ordinal);
        map.put(option.id(), Optional.of(option));
        return option;
    }

    private Options()
    {
    }
}
