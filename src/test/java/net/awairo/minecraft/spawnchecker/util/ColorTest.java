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

package net.awairo.minecraft.spawnchecker.util;

import java.util.concurrent.ThreadLocalRandom;

import net.awairo.minecraft.spawnchecker.api.Color;

import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void constantValuesTest() {
        var c = Color.ofColorCode("#000");
        assertEquals(0, c.intRed());
        assertEquals(0, c.intGreen());
        assertEquals(0, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#000000", c.toStringValue());
        c = Color.ofColorCode("#000000");
        assertEquals(0, c.intRed());
        assertEquals(0, c.intGreen());
        assertEquals(0, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#000000", c.toStringValue());

        c = Color.ofColorCode("#999");
        assertEquals(153, c.intRed());
        assertEquals(153, c.intGreen());
        assertEquals(153, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#999999", c.toStringValue());
        c = Color.ofColorCode("#999999");
        assertEquals(153, c.intRed());
        assertEquals(153, c.intGreen());
        assertEquals(153, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#999999", c.toStringValue());

        c = Color.ofColorCode("#fff");
        assertEquals(255, c.intRed());
        assertEquals(255, c.intGreen());
        assertEquals(255, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#ffffff", c.toStringValue());
        c = Color.ofColorCode("#ffffff");
        assertEquals(255, c.intRed());
        assertEquals(255, c.intGreen());
        assertEquals(255, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#ffffff", c.toStringValue());
    }

    @Test
    void constantValuesTestWithAlpha() {
        var c = Color.ofColorCode("#0000");
        assertEquals(0, c.intRed());
        assertEquals(0, c.intGreen());
        assertEquals(0, c.intBlue());
        assertEquals(0, c.intAlpha());
        assertEquals("#00000000", c.toStringValue());
        c = Color.ofColorCode("#00000000");
        assertEquals(0, c.intRed());
        assertEquals(0, c.intGreen());
        assertEquals(0, c.intBlue());
        assertEquals(0, c.intAlpha());
        assertEquals("#00000000", c.toStringValue());

        c = Color.ofColorCode("#9999");
        assertEquals(153, c.intRed());
        assertEquals(153, c.intGreen());
        assertEquals(153, c.intBlue());
        assertEquals(153, c.intAlpha());
        assertEquals("#99999999", c.toStringValue());
        c = Color.ofColorCode("#99999999");
        assertEquals(153, c.intRed());
        assertEquals(153, c.intGreen());
        assertEquals(153, c.intBlue());
        assertEquals(153, c.intAlpha());
        assertEquals("#99999999", c.toStringValue());

        // Alpha が最大値の場合は文字列値には出力されない
        c = Color.ofColorCode("#ffff");
        assertEquals(255, c.intRed());
        assertEquals(255, c.intGreen());
        assertEquals(255, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#ffffff", c.toStringValue());
        c = Color.ofColorCode("#ffffffff");
        assertEquals(255, c.intRed());
        assertEquals(255, c.intGreen());
        assertEquals(255, c.intBlue());
        assertEquals(255, c.intAlpha());
        assertEquals("#ffffff", c.toStringValue());
    }

    @Test
    void randomValueParseTest() {
        val ri = ThreadLocalRandom.current().nextInt(0, 256);
        val gi = ThreadLocalRandom.current().nextInt(0, 256);
        val bi = ThreadLocalRandom.current().nextInt(0, 256);
        val ai = ThreadLocalRandom.current().nextInt(0, 255); // not a max alpha

        val lowerCase = String.format("#%02x%02x%02x", ri, gi, bi);
        val upperCase = lowerCase.toUpperCase();
        val lowerCaseWithAlpha = String.format("#%02x%02x%02x%02x", ri, gi, bi, ai);
        val upperCaseWithAlpha = lowerCaseWithAlpha.toUpperCase();

        assertEquals(lowerCase, Color.of(ri, gi, bi).toStringValue());
        assertEquals(lowerCase, Color.ofColorCode(lowerCase).toStringValue());
        assertEquals(upperCase, Color.of(ri, gi, bi).toStringValue().toUpperCase());
        assertEquals(upperCase, Color.ofColorCode(upperCase).toStringValue().toUpperCase());

        assertEquals(lowerCaseWithAlpha, Color.of(ri, gi, bi, ai).toStringValue());
        assertEquals(lowerCaseWithAlpha, Color.ofColorCode(lowerCaseWithAlpha).toStringValue());
        assertEquals(upperCaseWithAlpha, Color.of(ri, gi, bi, ai).toStringValue().toUpperCase());
        assertEquals(upperCaseWithAlpha, Color.ofColorCode(upperCaseWithAlpha).toStringValue().toUpperCase());
    }

    @Test
    void floatValueTest() {
        val ri = ThreadLocalRandom.current().nextInt(0, 256);
        val gi = ThreadLocalRandom.current().nextInt(0, 256);
        val bi = ThreadLocalRandom.current().nextInt(0, 256);
        val ai = ThreadLocalRandom.current().nextInt(0, 255); // not a max alpha

        val minValues = Color.of(0, 0, 0, 0);
        val maxValues = Color.of(255, 255, 255, 255);
        val randomValues = Color.of(ri, gi, bi, ai);

        assertEquals(0f, minValues.red());
        assertEquals(0f, minValues.green());
        assertEquals(0f, minValues.blue());
        assertEquals(0f, minValues.alpha());

        assertEquals(1f, maxValues.red());
        assertEquals(1f, maxValues.green());
        assertEquals(1f, maxValues.blue());
        assertEquals(1f, maxValues.alpha());

        assertEquals(ri / 255f, randomValues.red());
        assertEquals(gi / 255f, randomValues.green());
        assertEquals(bi / 255f, randomValues.blue());
        assertEquals(ai / 255f, randomValues.alpha());
    }

    @Test
    void withIntX() {
        var c = Color.of(1, 2, 3, 4);
        assertEquals(1, c.intRed());
        assertEquals(2, c.intGreen());
        assertEquals(3, c.intBlue());
        assertEquals(4, c.intAlpha());
        c = c.withRed(5);
        assertEquals(5, c.intRed());
        assertEquals(2, c.intGreen());
        assertEquals(3, c.intBlue());
        assertEquals(4, c.intAlpha());
        c = c.withGreen(6);
        assertEquals(5, c.intRed());
        assertEquals(6, c.intGreen());
        assertEquals(3, c.intBlue());
        assertEquals(4, c.intAlpha());
        c = c.withBlue(7);
        assertEquals(5, c.intRed());
        assertEquals(6, c.intGreen());
        assertEquals(7, c.intBlue());
        assertEquals(4, c.intAlpha());
        c = c.withAlpha(8);
        assertEquals(5, c.intRed());
        assertEquals(6, c.intGreen());
        assertEquals(7, c.intBlue());
        assertEquals(8, c.intAlpha());
        c = c.withRGB(9, 10, 11);
        assertEquals(9, c.intRed());
        assertEquals(10, c.intGreen());
        assertEquals(11, c.intBlue());
        assertEquals(8, c.intAlpha());
    }

    @Test
    void withFloatX() {

        var c = Color.of(0.1f, 0.2f, 0.3f, 0.4f);
        assertEquals(round(0.1f), c.red());
        assertEquals(round(0.2f), c.green());
        assertEquals(round(0.3f), c.blue());
        assertEquals(round(0.4f), c.alpha());
        c = c.withRed(0.5f);
        assertEquals(round(0.5f), c.red());
        assertEquals(round(0.2f), c.green());
        assertEquals(round(0.3f), c.blue());
        assertEquals(round(0.4f), c.alpha());
        c = c.withGreen(0.6f);
        assertEquals(round(0.5f), c.red());
        assertEquals(round(0.6f), c.green());
        assertEquals(round(0.3f), c.blue());
        assertEquals(round(0.4f), c.alpha());
        c = c.withBlue(0.7f);
        assertEquals(round(0.5f), c.red());
        assertEquals(round(0.6f), c.green());
        assertEquals(round(0.7f), c.blue());
        assertEquals(round(0.4f), c.alpha());
        c = c.withAlpha(0.8f);
        assertEquals(round(0.5f), c.red());
        assertEquals(round(0.6f), c.green());
        assertEquals(round(0.7f), c.blue());
        assertEquals(round(0.8f), c.alpha());
        c = c.withRGB(0.1f, 0.2f, 0.3f);
        assertEquals(round(0.1f), c.red());
        assertEquals(round(0.2f), c.green());
        assertEquals(round(0.3f), c.blue());
        assertEquals(round(0.8f), c.alpha());
    }

    @Test
    void equalsAndHashCode() {
        val c1 = Color.of(0x00, 0x80, 0xC0, 0xFF);
        val c2 = Color.ofColorCode("#0080C0FF");
        assertEquals(c1.hashCode(), c2.hashCode());
        assertEquals(c1, c2);
        assertSame(c1, c2);
    }

    @Test
    void setToColor4F() {

    }

    // 一度intにしてから再度floatにする
    private static float round(float in) {
        return ((int) (in * 255f) / 255f);
    }
}
