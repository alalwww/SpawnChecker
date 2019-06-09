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

import java.util.function.Supplier;

import lombok.NonNull;
import lombok.val;

public class LazyCachedSupplier<T> implements Supplier<T> {

    public static <T> LazyCachedSupplier<T> of(@NonNull Supplier<? extends T> supplier) {
        if (!(supplier instanceof LazyCachedSupplier))
            return new LazyCachedSupplier<>(supplier);

        @SuppressWarnings("unchecked")
        val ret = (LazyCachedSupplier<T>) supplier;
        return ret;
    }

    public static <T> LazyCachedSupplier<T> withCached(@NonNull T value) {
        return new LazyCachedSupplier<>(dummySupplier(), value);
    }

    private final Supplier<? extends T> underlying;
    private volatile Object cache;

    private LazyCachedSupplier(Supplier<? extends T> supplier) {
        this(supplier, unset());
    }

    private LazyCachedSupplier(Supplier<? extends T> supplier, T value) {
        this.underlying = supplier;
        this.cache = value;
    }

    @Override
    public T get() {
        if (cache == UNSET)
            synchronized (this) {
                if (cache == UNSET)
                    cache = underlying.get();
            }

        @SuppressWarnings("unchecked")
        T ret = (T) cache;
        return ret;
    }

    @Override
    public String toString() {
        return String.format(
            "LazyCachedSupplier(cache=%s, underlying=%s)",
            (cache == UNSET ? "none" : "'" + cache + "'"), underlying
        );
    }

    private static final Object UNSET = new Object();
    private static final Supplier<?> DUMMY_SUPPLIER = () -> {
        throw new UnsupportedOperationException("DUMMY_SUPPLIER");
    };

    @SuppressWarnings("unchecked")
    private static <T> T unset() {
        return (T) UNSET;
    }

    @SuppressWarnings("unchecked")
    private static <T> Supplier<T> dummySupplier() {
        return (Supplier<T>) DUMMY_SUPPLIER;
    }
}
