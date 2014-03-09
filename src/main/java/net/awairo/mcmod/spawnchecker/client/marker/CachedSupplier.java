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

package net.awairo.mcmod.spawnchecker.client.marker;

import java.util.ArrayList;

import com.google.common.base.Supplier;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;

/**
 * CachedSupplier.
 * 
 * <p>
 * 毎フレームごとに使用する、マーカーインスタンスの生成コストを抑えるために、
 * 作成済みのマーカーインスタンスを保持し再利用するための、プール機能を提供します。
 * </p>
 * 
 * @author alalwww
 * 
 * @param <T> type of marker
 */
public class CachedSupplier<T extends Marker<T>> implements Supplier<T>
{
    /**
     * 新しい{@link CachedSupplier}を作成します.
     * 
     * @param factory 新しいインスタンスを生成する処理
     * @return インスタンス
     */
    public static <T extends Marker<T>> CachedSupplier<T> of(Supplier<T> factory)
    {
        return new CachedSupplier<T>(defaultSize(), factory);
    }

    /**
     * 新しい{@link CachedSupplier}を作成します.
     * 
     * @param defaultSize プールの初期サイズ
     * @param factory 新しいインスタンスを生成する処理
     * @return インスタンス
     */
    public static <T extends Marker<T>> CachedSupplier<T> of(int defaultSize, Supplier<T> factory)
    {
        return new CachedSupplier<T>(defaultSize, factory);
    }

    /** @return 初期サイズ */
    private static int defaultSize()
    {
        return ConstantsConfig.instance().defaultCachedSupplierSize;
    }

    // ------------------------------------

    private final int defaultSize;
    private final ArrayList<T> pool;
    private final Supplier<T> factory;

    private int usedMarker;

    /**
     * Constructor.
     * 
     * @param factory 新しいマーカーの生成処理
     * @param pool プール用のリスト
     */
    private CachedSupplier(int defaultSize, Supplier<T> factory)
    {
        this.defaultSize = defaultSize;
        this.factory = factory;
        this.pool = new ArrayList<T>(defaultSize);
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
     * キャッシュ済みのインスタンスを開放し、キャッシュサイズをリセットします.
     */
    public void clearAll()
    {
        pool.clear();
        pool.ensureCapacity(defaultSize);
    }

}
