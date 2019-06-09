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

import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class UpdateTimer {

    private final SpawnCheckerConfig config;

    private long lastUpdate;

    boolean canUpdate(long now) {
        val ret = now - lastUpdate > config.modeConfig().checkInterval().milliSeconds;
        if (ret)
            lastUpdate = now;
        return ret;
    }

    @Value
    public static final class Interval {
        public static final int MIN_VALUE = 50;
        public static final int MAX_VALUE = 5_000;
        public static final Interval DEFAULT = new Interval(500);

        public static Interval ofMilliSeconds(int milliSeconds) {
            return milliSeconds == DEFAULT.milliSeconds ? DEFAULT : new Interval(milliSeconds);
        }

        private final int milliSeconds;

        private Interval(int milliSeconds) {
            if (milliSeconds < MIN_VALUE || milliSeconds > MAX_VALUE)
                throw new IllegalArgumentException("Out of range. (" + milliSeconds + ")");
            this.milliSeconds = milliSeconds;
        }
    }
}
