/*
 * SpawnChecker.
 * 
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.spawnchecker.client.model;

import java.util.ArrayList;

import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;

/**
 * CachedSupplier.
 * 
 * @author alalwww
 * 
 * @param <T> type of marker
 */
public class CachedSupplier<T extends Marker<T>> implements Supplier<T>
{
    private static final int DEFAULT_CAPACITY = Ints.saturatedCast(5L + 500 + (500 / 10));
    private final ArrayList<T> pool;
    private final Supplier<T> factory;

    private int usedMarker;

    /**
     * 新しい{@link CachedSupplier}を作成します.
     * 
     * @param factory 新しいインスタンスを生成する処理
     * @return インスタンス
     */
    public static <T extends Marker<T>> CachedSupplier<T> of(Supplier<T> factory)
    {
        return new CachedSupplier<T>(factory, new ArrayList<T>(DEFAULT_CAPACITY));
    }

    private CachedSupplier(Supplier<T> factory, ArrayList<T> pool)
    {
        this.factory = factory;
        this.pool = pool;
    }

    @Override
    public T get()
    {
        if (pool.size() > usedMarker)
            return pool.get(usedMarker++).reset();

        final T m = factory.get();
        pool.add(m);
        usedMarker++;
        return m;
    }

    /**
     * 使用済みマーカーを回収し再利用出来る状態にします.
     */
    public void recycle()
    {
        usedMarker = 0;
    }

    /**
     * キャッシュ済みのインスタンスを開放しキャッシュサイズをリセットします.
     */
    public void clearAll()
    {
        pool.clear();
        pool.ensureCapacity(DEFAULT_CAPACITY);
    }

}
