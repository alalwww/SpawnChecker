/*
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.v1.function;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Function;

/**
 * ConvertFunctions.
 * 
 * @author alalwww
 */
public final class ConvertFunctions
{

    public static final Function<String, Boolean> TO_BOOLEAN = new Function<String, Boolean>()
    {
        @Override
        public Boolean apply(String input)
        {
            return Boolean.valueOf(input);
        }
    };

    public static final Function<String, Character> TO_CHARACTER = new Function<String, Character>()
    {
        @Override
        public Character apply(String input)
        {
            checkArgument(input.length() == 1, "%s is not char value", input);
            return Character.valueOf(input.charAt(0));
        }
    };

    public static final Function<String, Byte> TO_BYTE = new Function<String, Byte>()
    {
        @Override
        public Byte apply(String input)
        {
            return Byte.valueOf(input);
        }
    };

    public static final Function<String, Short> TO_SHORT = new Function<String, Short>()
    {
        @Override
        public Short apply(String input)
        {
            return Short.valueOf(input);
        }
    };

    public static final Function<String, Integer> TO_INTEGER = new Function<String, Integer>()
    {
        @Override
        public Integer apply(String input)
        {
            return Integer.valueOf(input);
        }
    };

    public static final Function<String, Long> TO_LONG = new Function<String, Long>()
    {
        @Override
        public Long apply(String input)
        {
            return Long.valueOf(input);
        }
    };

    public static final Function<String, Float> TO_FLOAT = new Function<String, Float>()
    {
        @Override
        public Float apply(String input)
        {
            return Float.valueOf(input);
        }
    };

    public static final Function<String, Double> TO_DOUBLE = new Function<String, Double>()
    {
        @Override
        public Double apply(String input)
        {
            return Double.valueOf(input);
        }
    };

    private ConvertFunctions()
    {
        throw new InternalError();
    }
}
