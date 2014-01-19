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

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Colors.
 * 
 * @since 1.0
 * @version 1.0
 * @author alalwww
 */
public final class Colors
{
    static final Pattern COLOR_PATTERN = Pattern
            .compile("^#?(([0-9a-fA-F]){3,4}|([0-9a-fA-F]{2}){3,4})$");

    private static final String SHARP = "#";
    private static final String HEX = "0x";
    private static final int LENGTH_OF_RGB = 3;
    private static final int LENGTH_OF_ARGB = 4;
    private static final int LENGTH_OF_RRGGBB = 6;
    private static final int LENGTH_OF_AARRGGBB = 8;

    /**
     * RGB Color to String.
     * 
     * @param color
     * @return color string #RRGGBB
     */
    public static String toString(Color color)
    {
        return toString(color, false);
    }

    /**
     * Color to String.
     * 
     * @param color
     *            color
     * @param hasalpha
     *            true if the RGBA color
     * @return color string #AARRGGBB or #RRGGBB
     */
    public static String toString(Color color, boolean hasalpha)
    {
        return new StringBuilder(SHARP.length() + (hasalpha ? LENGTH_OF_AARRGGBB : LENGTH_OF_RRGGBB))
                .append(SHARP)
                .append(hasalpha ? hexToString(color.getAlpha()) : "")
                .append(hexToString(color.getRed()))
                .append(hexToString(color.getGreen()))
                .append(hexToString(color.getBlue()))
                .toString()
                .toUpperCase();
    }

    /**
     * merge int value of primary colors.
     * 
     * @param r
     *            red
     * @param g
     *            green
     * @param b
     *            blue
     * @return merged RGB color
     */
    public static int toIntColor(int r, int g, int b)
    {
        int color = b;
        color += g << 8;
        color += r << 16;
        return color;
    }

    /**
     * merge int value of primary colors and alpha.
     * 
     * @param r
     *            red
     * @param g
     *            green
     * @param b
     *            blue
     * @param a
     *            alpha
     * @return 0xAARRGGBB
     */
    public static int toIntColor(int r, int g, int b, int a)
    {
        int color = b;
        color += g << 8;
        color += r << 16;
        color += a << 24;
        return color;
    }

    /**
     * @return builder
     */
    public static ColorBuilder builder()
    {
        return new ColorBuilder();
    }

    /**
     * @return builder
     */
    public static Optional<Color> fromString(String colorString)
    {
        if (colorString == null || !isColorString(colorString))
            return Optional.absent();

        colorString = removeHeaderChar(colorString);

        if (hasAlpha(colorString))
            return Optional.of(builder().argb(colorString).build());

        return Optional.of(builder().rgb(colorString).build());
    }

    public static Color fromARGB(int alpha, int red, int green, int blue)
    {
        return builder().alpha(alpha).red(red).green(green).blue(blue).build();
    }

    public static Color fromRGB(int red, int green, int blue)
    {
        return builder().red(red).green(green).blue(blue).build();
    }

    private static String hexToString(int hexInt)
    {
        return Strings.padStart(Integer.toHexString(hexInt), 2, '0');
    }

    private static boolean isColorString(String colorString)
    {
        if (colorString == null)
            return false;

        return COLOR_PATTERN.matcher(colorString).matches();
    }

    private static boolean hasAlpha(String colorString)
    {
        final int length = colorString.length();
        return (length == LENGTH_OF_ARGB || length == LENGTH_OF_AARRGGBB);
    }

    private static String removeHeaderChar(String s)
    {
        if (s.startsWith(SHARP))
            return s.substring(SHARP.length(), s.length());

        if (s.startsWith(HEX))
            return s.substring(HEX.length(), s.length());

        return s;
    }

    /**
     * {@link Color} builder.
     * 
     * @since 1.0
     * @version 1.0
     * @author alalwww
     */
    public static final class ColorBuilder
    {
        private int r;
        private int g;
        private int b;
        private int a;

        /**
         * Constructor.
         */
        public ColorBuilder()
        {
            rgb(0);
        }

        /**
         * @return new color
         */
        public Color build()
        {
            return new Color(r, g, b, a);
        }

        /**
         * @param alpha 0-255 alpha value
         * @return builder
         */
        public ColorBuilder alpha(int alpha)
        {
            a = checkRange(alpha, "alpha");
            return this;
        }

        /**
         * @param red 0-255 color value
         * @return builder
         */
        public ColorBuilder red(int red)
        {
            r = checkRange(red, "red");
            return this;
        }

        /**
         * @param green 0-255 color value
         * @return builder
         */
        public ColorBuilder green(int green)
        {
            g = checkRange(green, "green");
            return this;
        }

        /**
         * @param blue 0-255 color value
         * @return builder
         */
        public ColorBuilder blue(int blue)
        {
            b = checkRange(blue, "blue");
            return this;
        }

        /**
         * @param rgb RGB value
         * @return builder
         */
        public ColorBuilder rgb(int rgb)
        {
            return argb(rgb).alpha(0xFF);
        }

        /**
         * @param argb ARGB value
         * @return builder
         */
        public ColorBuilder argb(int argb)
        {
            return alpha((argb >> 24) & 0xFF)
                    .red((argb >> 16) & 0xFF)
                    .green((argb >> 8) & 0xFF)
                    .blue((argb >> 0) & 0xFF);
        }

        public ColorBuilder rgb(String rgb)
        {
            checkColorString(rgb, "%s is not RGC color code", rgb);

            rgb = removeHeaderChar(checkNotNull(rgb, "Argument 'rgb' must not be null."));
            final int l = rgb.length();
            checkArgument(l == LENGTH_OF_RGB || l == LENGTH_OF_RRGGBB);
            final int[] values = parseColor(rgb, false);
            return red(values[0]).green(values[1]).blue(values[2]);
        }

        public ColorBuilder argb(String argb)
        {
            checkColorString(argb, "%s is not ARGC color code", argb);

            argb = removeHeaderChar(checkNotNull(argb, "Argument 'argb' must not be null."));
            final int l = argb.length();
            checkArgument(l == LENGTH_OF_ARGB || l == LENGTH_OF_AARRGGBB);
            final int[] values = parseColor(argb, true);
            return alpha(values[0]).red(values[1]).green(values[2]).blue(values[3]);
        }

        private static int checkRange(int value, String name)
        {
            checkArgument(value >= 0, "%s(%s) is less than zero.", name, value);
            return value;
        }

        private static void checkColorString(String colorString, String format, Object... args)
        {
            checkArgument(isColorString(colorString), format, args);
        }

        static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,2}$");
        private static final int HEX_RADIX = 16;

        private static int[] parseColor(String colorString, boolean hasalpha)
        {
            final int loopEnd = hasalpha ? LENGTH_OF_ARGB : LENGTH_OF_RGB;
            final int length = colorString.length(); // 3, 4, 6 or 8
            final int step = computeStep(length, loopEnd);

            final int[] colorValueStack = new int[loopEnd];

            int cursor = 0;

            for (int loopCount = 0; loopCount < loopEnd; loopCount++)
            {
                final int endIndex = cursor + step;
                String s = colorString.substring(cursor, endIndex);
                cursor = endIndex;

                if (s.length() == 1)
                    s = s.concat(s); // avoid using StringBuilder

                if (HEX_PATTERN.matcher(s).matches())
                {
                    colorValueStack[loopCount] = Integer.parseInt(s, HEX_RADIX);
                    continue;
                }

                System.err.printf("illegal color value. (%s) replace to FF", s);
                colorValueStack[loopCount] = 0xFF;
            }

            return colorValueStack;
        }

        private static int computeStep(int length, int loopCount)
        {
            if (length == loopCount)
                return 1;

            if (length == loopCount + loopCount)
                return 2;

            throw new InternalError("unreachable code.");
        }
    }

    private Colors()
    {
        throw new InternalError();
    }
}
