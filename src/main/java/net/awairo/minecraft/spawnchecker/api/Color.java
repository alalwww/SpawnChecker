/*
 * SpawnChecker
 * Copyright (C) 2019 alalwww
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.awairo.minecraft.spawnchecker.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.val;

public class Color {
    private final int intRed;
    private final int intGreen;
    private final int intBlue;
    private final int intAlpha;

    private final float floatRed;
    private final float floatGreen;
    private final float floatBlue;
    private final float floatAlpha;

    private final int hashCode;
    private final String toStringValue;

    private Color(int red, int green, int blue, int alpha) {
        this.intRed = red;
        this.intGreen = green;
        this.intBlue = blue;
        this.intAlpha = alpha;

        this.floatRed = toColorFloat(red, "red");
        this.floatGreen = toColorFloat(green, "green");
        this.floatBlue = toColorFloat(blue, "blue");
        this.floatAlpha = toColorFloat(alpha, "alpha");

        this.hashCode = intToHashCode(red, green, blue, alpha);
        this.toStringValue = alpha == DEFAULT_ALPHA_INT
            ? String.format(PREFIX + "%02x%02x%02x", red, green, blue)
            : String.format(PREFIX + "%02x%02x%02x%02x", red, green, blue, alpha);
    }

    public float red() { return floatRed; }

    public int intRed() { return intRed; }

    public float green() { return floatGreen; }

    public int intGreen() { return intGreen; }

    public float blue() { return floatBlue; }

    public int intBlue() { return intBlue; }

    public float alpha() { return floatAlpha; }

    public int intAlpha() { return intAlpha; }

    public int toInt() {
        return hashCode();
    }

    public void setToColor4F(Color4FConsumer consumer) {
        consumer.apply(floatRed, floatGreen, floatBlue, floatAlpha);
    }

    public Color withRed(float red) {
        return of(red, floatGreen, floatBlue, floatAlpha);
    }

    public Color withRed(int red) {
        return of(red, intGreen, intBlue, intAlpha);
    }

    public Color withGreen(float green) {
        return of(floatRed, green, floatBlue, floatAlpha);
    }

    public Color withGreen(int green) {
        return of(intRed, green, intBlue, intAlpha);
    }

    public Color withBlue(float blue) {
        return of(floatRed, floatGreen, blue, floatAlpha);
    }

    public Color withBlue(int blue) {
        return of(intRed, intGreen, blue, intAlpha);
    }

    public Color withAlpha(float alpha) {
        return of(floatRed, floatGreen, floatBlue, alpha);
    }

    public Color withAlpha(int alpha) {
        return of(intRed, intGreen, intBlue, alpha);
    }

    public Color withRGB(int red, int green, int blue) {
        return of(red, green, blue, intAlpha);
    }

    public Color withRGB(float red, float green, float blue) {
        return of(red, green, blue, floatAlpha);
    }

    public boolean isTransparent() {
        return intAlpha == 0;
    }

    public String toStringValue() {
        return toStringValue;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Color
            && intRed == ((Color) obj).intRed
            && intGreen == ((Color) obj).intGreen
            && intBlue == ((Color) obj).intBlue
            && intAlpha == ((Color) obj).intAlpha;
    }

    @Override
    public String toString() {
        return String.format("Color(r=%s, g=%s, b=%s, a=%s)", intRed, intGreen, intBlue, intAlpha);
    }

    private static final ConcurrentMap<String, Color> STRING_TO_COLOR = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, Color> INT_TO_COLOR = new ConcurrentHashMap<>();

    public static Color of(float red, float green, float blue, float alpha) {
        return of(
            toColorInt(red, "red"),
            toColorInt(green, "green"),
            toColorInt(blue, "blue "),
            toColorInt(alpha, "alpha")
        );
    }

    public static Color of(int red, int green, int blue) {
        return of(red, green, blue, DEFAULT_ALPHA_INT);
    }

    public static Color of(int red, int green, int blue, int alpha) {
        val hash = intToHashCode(red, green, blue, alpha);
        return INT_TO_COLOR.computeIfAbsent(hash, k -> {
            val color = new Color(red, green, blue, alpha);
            STRING_TO_COLOR.putIfAbsent(normalize(color.toStringValue()), color);
            return color;
        });
    }

    public static Color ofColorCode(@NonNull String colorCode) {
        return STRING_TO_COLOR.computeIfAbsent(normalize(colorCode), key -> {
            val color = parse(key);
            INT_TO_COLOR.putIfAbsent(color.toInt(), color);
            return color;
        });
    }

    private static final int MIN_INT = 0x00;
    private static final int MAX_INT = 0xFF;
    private static final int DEFAULT_ALPHA_INT = MAX_INT;
    private static final String DEFAULT_ALPHA_STRING = String.format("%02x", MAX_INT);
    private static final float MIN_FLOAT = 0.0f;
    private static final float MAX_FLOAT = 1.0f;
    private static final String PREFIX = "#";
    private static final int RGB_LENGTH = 4;
    private static final int RGBA_LENGTH = 5;
    @SuppressWarnings("SpellCheckingInspection")
    private static final int RRGGBB_LENGTH = 7;
    @SuppressWarnings("SpellCheckingInspection")
    private static final int RRGGBBAA_LENGTH = 9;
    private static final int RADIX_HEX = 16;
    @SuppressWarnings("SpellCheckingInspection")
    private static final Set<Integer> LOWER_HEX_CHARS = "0123456789abcdef".chars().boxed().collect(Collectors.toSet());
    private static final Pattern RGBA_PATTERN =
        Pattern.compile("^#(?<r>[0-9a-f])(?<g>[0-9a-f])(?<b>[0-9a-f])(?<a>[0-9a-f])?$");
    @SuppressWarnings("SpellCheckingInspection")
    private static final Pattern RRGGBBAA_PATTERN =
        Pattern.compile("^#(?<r>[0-9a-f]{2})(?<g>[0-9a-f]{2})(?<b>[0-9a-f]{2})(?<a>[0-9a-f]{2})$");

    private static String normalize(String colorCode) {
        val code = colorCode.toLowerCase();
        if (!code.startsWith("#") || code.length() - 1 != code.chars().skip(1).filter(LOWER_HEX_CHARS::contains).count())
            throw new IllegalArgumentException(String.format("Illegal color format. colorCode='%s'", colorCode));

        final String r, g, b, a;
        final Matcher matcher;
        switch (code.length()) {
            case RGB_LENGTH:
                matcher = RGBA_PATTERN.matcher(code);
                if (!matcher.matches()) break;
                r = matcher.group("r");
                g = matcher.group("g");
                b = matcher.group("b");
                return "#" + r + r + g + g + b + b + DEFAULT_ALPHA_STRING;

            case RGBA_LENGTH:
                matcher = RGBA_PATTERN.matcher(code);
                if (!matcher.matches()) break;
                r = matcher.group("r");
                g = matcher.group("g");
                b = matcher.group("b");
                a = matcher.group("a");
                return "#" + r + r + g + g + b + b + a + a;

            case RRGGBB_LENGTH:
                return code + DEFAULT_ALPHA_STRING;

            case RRGGBBAA_LENGTH:
                return code;

            default:
                break;
        }

        throw new IllegalArgumentException(
            String.format("Illegal color format. colorCode='%s'", colorCode));
    }

    private static Color parse(String normalizedCode) {
        if (normalizedCode.length() != RRGGBBAA_LENGTH)
            throw new IllegalArgumentException(normalizedCode);

        RuntimeException cause = null;
        try {
            val matcher = RRGGBBAA_PATTERN.matcher(normalizedCode);
            if (matcher.matches()) {
                int r = Integer.parseInt(matcher.group("r"), RADIX_HEX);
                int g = Integer.parseInt(matcher.group("g"), RADIX_HEX);
                int b = Integer.parseInt(matcher.group("b"), RADIX_HEX);
                int a = Integer.parseInt(matcher.group("a"), RADIX_HEX);
                return new Color(r, g, b, a);
            }
        } catch (RuntimeException e) {
            cause = e;
        }

        throw new IllegalArgumentException(
            String.format("Illegal color format. colorCode='%s'", normalizedCode),
            cause
        );
    }

    private static float toColorFloat(int value, String name) {
        if (value >= MIN_INT && value <= MAX_INT)
            return (float) value / (float) MAX_INT;

        throw new IllegalArgumentException(
            String.format("A %s should be between 0x00 and 0xff. (%s)", name, value)
        );
    }

    private static int toColorInt(float value, String name) {
        if (value >= MIN_FLOAT && value <= MAX_FLOAT)
            return (int) (value * MAX_INT);

        throw new IllegalArgumentException(
            String.format("A %s should be between 0.0f and 1.0f. (%s)", name, value)
        );
    }

    private static int intToHashCode(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @FunctionalInterface
    public interface Color4FConsumer {
        void apply(float x, float y, float z, float a);
    }
}
