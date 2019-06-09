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

package net.awairo.minecraft.spawnchecker.hud;

import lombok.Getter;

public abstract class HudOffset {
    public static final int MIN_VALUE = -4_096;
    public static final int MAX_VALUE = 4_096;
    public static final int DEFAULT_VALUE = 0;

    public static X xOf(int value) { return new X(value);}

    public static Y yOf(int value) { return new Y(value);}

    @Getter
    private final int value;

    private HudOffset(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE)
            throw new IllegalArgumentException("Out of range. (" + value + ")");
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || getClass().isInstance(obj) && ((HudOffset) obj).value == value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + value + ")";
    }

    public static final class X extends HudOffset {
        public static final X DEFAULT = new X(DEFAULT_VALUE);

        private X(int value) { super(value); }
    }

    public static final class Y extends HudOffset {
        public static final Y DEFAULT = new Y(DEFAULT_VALUE);

        private Y(int value) { super(value); }
    }
}
