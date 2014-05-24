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

package net.awairo.mcmod.common.v1.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class ColorsTest
{
    @Test
    public void testToStringColor()
    {
        assertThat(Colors.toString(new Color(0, 0, 0)), is("#000000"));
        assertThat(Colors.toString(new Color(0xff, 0, 0)), is("#FF0000"));
        assertThat(Colors.toString(new Color(0, 0xFF, 0)), is("#00FF00"));
        assertThat(Colors.toString(new Color(0, 0, 0xFF)), is("#0000FF"));
    }

    @Test
    public void testToStringColorBoolean()
    {
        assertThat(Colors.toString(new Color(0, 0, 0), false), is("#000000"));
        assertThat(Colors.toString(new Color(0xff, 0, 0), false), is("#FF0000"));
        assertThat(Colors.toString(new Color(0, 0xFF, 0), false), is("#00FF00"));
        assertThat(Colors.toString(new Color(0, 0, 0xFF), false), is("#0000FF"));

        assertThat(Colors.toString(new Color(0, 0, 0, 0), false), is("#000000"));
        assertThat(Colors.toString(new Color(0xff, 0, 0, 0), false), is("#FF0000"));
        assertThat(Colors.toString(new Color(0, 0xFF, 0, 0), false), is("#00FF00"));
        assertThat(Colors.toString(new Color(0, 0, 0xFF, 0), false), is("#0000FF"));

        assertThat(Colors.toString(new Color(0, 0, 0), true), is("#FF000000"));
        assertThat(Colors.toString(new Color(0xff, 0, 0), true), is("#FFFF0000"));
        assertThat(Colors.toString(new Color(0, 0xFF, 0), true), is("#FF00FF00"));
        assertThat(Colors.toString(new Color(0, 0, 0xFF), true), is("#FF0000FF"));

        assertThat(Colors.toString(new Color(0, 0, 0, 0), true), is("#00000000"));
        assertThat(Colors.toString(new Color(0xff, 0, 0, 0), true), is("#00FF0000"));
        assertThat(Colors.toString(new Color(0, 0xFF, 0, 0), true), is("#0000FF00"));
        assertThat(Colors.toString(new Color(0, 0, 0xFF, 0), true), is("#000000FF"));
    }

    @Test
    public void testToIntColorIntIntInt()
    {
        assertThat(Colors.toIntColor(0x00, 0x00, 0x00), is(0x000000));
        assertThat(Colors.toIntColor(0xff, 0x00, 0x00), is(0xff0000));
        assertThat(Colors.toIntColor(0x00, 0xff, 0x00), is(0x00ff00));
        assertThat(Colors.toIntColor(0x00, 0x00, 0xff), is(0x0000ff));
    }

    @Test
    public void testToIntColorIntIntIntInt()
    {
        assertThat(Colors.toIntColor(0x00, 0x00, 0x00, 0x00), is(0x00000000));
        assertThat(Colors.toIntColor(0xff, 0x00, 0x00, 0x00), is(0x00ff0000));
        assertThat(Colors.toIntColor(0x00, 0xff, 0x00, 0x00), is(0x0000ff00));
        assertThat(Colors.toIntColor(0x00, 0x00, 0xff, 0x00), is(0x000000ff));
        assertThat(Colors.toIntColor(0x00, 0x00, 0x00, 0xFF), is(0xFF000000));
    }

    @Test
    public void testFromString()
    {
        assertThat(Colors.fromString("#F00").get(), is(new Color(0xFF, 0x00, 0x00)));
        assertThat(Colors.fromString("#00E000").get(), is(new Color(0x00, 0xE0, 0x00)));
        assertThat(Colors.fromString("00D").get(), is(new Color(0x00, 0x00, 0xDD)));
        assertThat(Colors.fromString("C00000").get(), is(new Color(0xC0, 0x00, 0x00)));
        assertThat(Colors.fromString("#A0B0").get(), is(new Color(0x00, 0xBB, 0x00, 0xAA)));
        assertThat(Colors.fromString("#A00000A0").get(), is(new Color(0x00, 0x00, 0xA0, 0xA0)));
        assertThat(Colors.fromString("A000").get(), is(new Color(0x00, 0x00, 0x00, 0xAA)));
        assertThat(Colors.fromString("A0000000").get(), is(new Color(0x00, 0x00, 0x00, 0xA0)));

        assertThat(Colors.fromString("#f00").get(), is(new Color(0xFF, 0x00, 0x00)));
        assertThat(Colors.fromString("#00e000").get(), is(new Color(0x00, 0xE0, 0x00)));
        assertThat(Colors.fromString("00d").get(), is(new Color(0x00, 0x00, 0xDD)));
        assertThat(Colors.fromString("c00000").get(), is(new Color(0xC0, 0x00, 0x00)));
        assertThat(Colors.fromString("#a0b0").get(), is(new Color(0x00, 0xBB, 0x00, 0xAA)));
        assertThat(Colors.fromString("#a00000a0").get(), is(new Color(0x00, 0x00, 0xA0, 0xA0)));
        assertThat(Colors.fromString("a000").get(), is(new Color(0x00, 0x00, 0x00, 0xAA)));
        assertThat(Colors.fromString("a0000000").get(), is(new Color(0x00, 0x00, 0x00, 0xA0)));
    }

    @Test
    public void testFromString_illegalValue()
    {
        assertThat(Colors.fromString(null).orNull(), is(nullValue()));
        assertThat(Colors.fromString("").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#00G").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#000G").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#00000G").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#0000000G").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#00").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#00000").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#0000000").orNull(), is(nullValue()));
        assertThat(Colors.fromString("#000000000").orNull(), is(nullValue()));
    }

    @Test
    public void testFromARGB()
    {
        assertThat(Colors.fromARGB(0x00, 0x00, 0x00, 0x00), is(new Color(0x00, 0x00, 0x00, 0x00)));
        assertThat(Colors.fromARGB(0xFF, 0x00, 0x00, 0x00), is(new Color(0x00, 0x00, 0x00, 0xFF)));
        assertThat(Colors.fromARGB(0x00, 0xFF, 0x00, 0x00), is(new Color(0xFF, 0x00, 0x00, 0x00)));
        assertThat(Colors.fromARGB(0x00, 0x00, 0xFF, 0x00), is(new Color(0x00, 0xFF, 0x00, 0x00)));
        assertThat(Colors.fromARGB(0x00, 0x00, 0x00, 0xFF), is(new Color(0x00, 0x00, 0xFF, 0x00)));
    }

    @Test
    public void testFromARGB_IAE()
    {
        testFromARGB_IAE(0xff + 0x01, 0, 0, 0);
        testFromARGB_IAE(0x00 - 0x01, 0, 0, 0);
        testFromARGB_IAE(0, 0xff + 0x01, 0, 0);
        testFromARGB_IAE(0, 0x00 - 0x01, 0, 0);
        testFromARGB_IAE(0, 0, 0xff + 0x01, 0);
        testFromARGB_IAE(0, 0, 0x00 - 0x01, 0);
        testFromARGB_IAE(0, 0, 0, 0xff + 0x01);
        testFromARGB_IAE(0, 0, 0, 0x00 - 0x01);
    }

    private static void testFromARGB_IAE(int a, int r, int g, int b)
    {
        try
        {
            Colors.fromARGB(a, r, g, b);
            fail("例外発生しなかった");
        }
        catch (IllegalArgumentException e)
        {
        }
    }

    @Test
    public void testFromRGB()
    {
        assertThat(Colors.fromRGB(0x00, 0x00, 0x00), is(new Color(0x00, 0x00, 0x00)));
        assertThat(Colors.fromRGB(0xFF, 0x00, 0x00), is(new Color(0xFF, 0x00, 0x00)));
        assertThat(Colors.fromRGB(0x00, 0xFF, 0x00), is(new Color(0x00, 0xFF, 0x00)));
        assertThat(Colors.fromRGB(0x00, 0x00, 0xFF), is(new Color(0x00, 0x00, 0xFF)));
    }

    @Test
    public void testFromRGB_IAE()
    {
        testFromRGB_IAE(0xff + 0x01, 0, 0);
        testFromRGB_IAE(0x00 - 0x01, 0, 0);
        testFromRGB_IAE(0, 0xff + 0x01, 0);
        testFromRGB_IAE(0, 0x00 - 0x01, 0);
        testFromRGB_IAE(0, 0, 0xff + 0x01);
        testFromRGB_IAE(0, 0, 0x00 - 0x01);
    }

    @Test
    public void testApplyBrightnessTo()
    {
        // 最大の明るさが0 ARGB #ff000000 のケース
        assertThat(Colors.applyBrightnessTo(new Color(0), 50),
                is(new Color(50, 50, 50, 255).getRGB()));

        // Rが最大
        assertThat(Colors.applyBrightnessTo(new Color(50, 30, 40, 200), 100),
                is(new Color(100, 60, 80, 200).getRGB()));
        // Gが最大
        assertThat(Colors.applyBrightnessTo(new Color(40, 50, 30, 210), 100),
                is(new Color(80, 100, 60, 210).getRGB()));
        // Bが最大
        assertThat(Colors.applyBrightnessTo(new Color(30, 40, 50, 220), 100),
                is(new Color(60, 80, 100, 220).getRGB()));
    }

    private static void testFromRGB_IAE(int r, int g, int b)
    {
        try
        {
            Colors.fromRGB(r, g, b);
            fail("例外発生しなかった");
        }
        catch (IllegalArgumentException e)
        {
        }
    }

}
