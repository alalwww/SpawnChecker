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

package net.awairo.minecraft.spawnchecker;

import java.util.Objects;
import java.util.function.Supplier;

import net.awairo.minecraft.spawnchecker.util.LazyCachedSupplier;

import lombok.NonNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class SpawnCheckerException extends RuntimeException {

    private final LazyCachedSupplier<String> message;

    public SpawnCheckerException(@NonNull String message) {
        this(LazyCachedSupplier.withCached(message));
    }

    public SpawnCheckerException(@NonNull String format, @NonNull Object... args) {
        this(() -> String.format(format, (Object[]) args));
    }

    public SpawnCheckerException(Throwable cause, @NonNull String format, @NonNull Object... args) {
        this(() -> String.format(format, (Object[]) args), cause);
    }

    public SpawnCheckerException(@NonNull Supplier<String> message) {
        this(message, null);
    }

    public SpawnCheckerException(@NonNull Supplier<String> message, Throwable cause) {
        this(LazyCachedSupplier.of(message), cause);
    }

    private SpawnCheckerException(LazyCachedSupplier<String> message, Throwable cause) {
        this.message = message;
        if (Objects.nonNull(cause))
            initCause(cause);
    }

    @Override
    public String getMessage() {
        return message.get();
    }
}
