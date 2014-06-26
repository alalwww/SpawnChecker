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

import com.google.common.primitives.Ints;

import net.minecraft.client.resources.I18n;

import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode.Option;

/**
 * Simple mode option.
 * 
 * @author alalwww
 */
public class SimpleOption implements Mode.Option
{
    /**
     * 新しい{@link SimpleOption}を生成します.
     * 
     * @param id オプション識別子
     * @param nameKey オプション名キー
     * @return 新しいSimpleOption
     */
    public static SimpleOption of(String id, String nameKey)
    {
        return of(id, nameKey, id.hashCode());
    }

    /**
     * 新しい{@link SimpleOption}を生成します.
     * 
     * @param id オプション識別子
     * @param nameKey オプション名キー
     * @param ordinal 順序
     * @return 新しいSimpleOption
     */
    public static SimpleOption of(String id, String nameKey, int ordinal)
    {
        return new SimpleOption(id, nameKey, ordinal);
    }

    // ---------------------------------

    private final String id;
    private final String nameKey;
    private final int ordinal;

    /**
     * Constructor.
     * 
     * @param id option id
     * @param nameKey option name resource key
     * @param ordinal ordinal
     */
    protected SimpleOption(String id, String nameKey, int ordinal)
    {
        this.id = checkNotNull(id, "id");
        this.nameKey = checkNotNull(nameKey, "nameKey");
        this.ordinal = ordinal;
    }

    @Override
    public String id()
    {
        return id;
    }

    @Override
    public String name()
    {
        return I18n.format(nameKey);
    }

    @Override
    public int compareTo(Option o)
    {
        if (o instanceof SimpleOption)
        {
            final int result = Ints.compare(ordinal, ((SimpleOption) o).ordinal);
            if (result != 0) return result;
        }

        return id().compareTo(o.id());
    }

    @Override
    public int hashCode()
    {
        return nameKey.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;

        if (obj instanceof SimpleOption)
        {
            final SimpleOption o = (SimpleOption) obj;
            return nameKey.equals(o.nameKey);
        }

        return false;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
