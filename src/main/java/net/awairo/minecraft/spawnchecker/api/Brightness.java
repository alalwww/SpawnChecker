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

import lombok.Getter;

public enum Brightness {
    MINUS_5(-5),
    MINUS_4(-4),
    MINUS_3(-3),
    MINUS_2(-2),
    MINUS_1(-1),
    ZERO(0),
    PLUS_1(1),
    PLUS_2(2),
    PLUS_3(3),
    PLUS_4(4),
    PLUS_5(5);

    public static final Brightness DEFAULT = ZERO;

    @Getter
    private final int value;

    Brightness(int value) {
        this.value = value;
    }

    public Brightness next() {
        switch (this) {
            case MINUS_5:
                return MINUS_4;
            case MINUS_4:
                return MINUS_3;
            case MINUS_3:
                return MINUS_2;
            case MINUS_2:
                return MINUS_1;
            case MINUS_1:
                return ZERO;
            case ZERO:
                return PLUS_1;
            case PLUS_1:
                return PLUS_2;
            case PLUS_2:
                return PLUS_3;
            case PLUS_3:
                return PLUS_4;
            case PLUS_4:
            case PLUS_5:
                return PLUS_5;
            default:
                throw new InternalError("Unreachable code");
        }
    }

    public Brightness prev() {
        switch (this) {
            case MINUS_5:
            case MINUS_4:
                return MINUS_5;
            case MINUS_3:
                return MINUS_4;
            case MINUS_2:
                return MINUS_3;
            case MINUS_1:
                return MINUS_2;
            case ZERO:
                return MINUS_1;
            case PLUS_1:
                return ZERO;
            case PLUS_2:
                return PLUS_1;
            case PLUS_3:
                return PLUS_2;
            case PLUS_4:
                return PLUS_3;
            case PLUS_5:
                return PLUS_4;
            default:
                throw new InternalError("Unreachable code");
        }
    }
}
