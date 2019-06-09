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

package net.awairo.minecraft.spawnchecker.mode;

import java.util.concurrent.ThreadLocalRandom;

import net.awairo.minecraft.spawnchecker.api.ScanRange;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Vertical;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScanRangeTest {

    @Test
    void testNext() {
        val value = ThreadLocalRandom.current().nextInt(ScanRange.Vertical.MIN_VALUE, ScanRange.Vertical.MAX_VALUE);
        assertEquals(Vertical.of(value).next(), Vertical.of(value + 1));
    }

    @Test
    void testPrev() {
        val value = ThreadLocalRandom.current().nextInt(ScanRange.Vertical.MIN_VALUE, ScanRange.Vertical.MAX_VALUE) + 1;
        assertEquals(Vertical.of(value).prev(), Vertical.of(value - 1));
    }

}
