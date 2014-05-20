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

package net.awairo.mcmod.spawnchecker.client.mode.core;

import static com.google.common.base.Preconditions.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * 各モードコンテナの抽象クラス.
 * 
 * @author alalwww
 */
abstract class ModeContainer<M extends Mode>
{
    private Settings settings;

    /** 派生コンテナで追加された全てのモードのマップ. */
    static final Map<String, Mode> idToModeMap = Maps.newHashMap();

    /** コンテナに格納されているモードの一覧. */
    private final List<M> modes = Lists.newArrayList();

    /**
     * 停止していたら開始する.
     */
    abstract void startIfStopped();

    /**
     * 開始していたら停止する.
     */
    abstract void stopIfStarted();

    /**
     * 開始していたら更新する.
     */
    abstract void update();

    /**
     * 開始していたらGUIを描画する.
     * 
     * @param tickCount tick counts
     * @param partialTick partial ticks of rendering
     */
    abstract void renderGui(long tickCount, float partialTick);

    /**
     * 開始していたらゲーム(ワールド)内に描画する.
     * 
     * @param tickCounts tick counts
     * @param partialTicks partial ticks of rendering
     */
    abstract void renderIngame(long tickCount, float partialTick);

    /**
     * コンテナを初期化します.
     * 
     * @param settings 設定
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    void initialize(Settings settings)
    {
        this.settings = checkNotNull(settings);

        if (modes.isEmpty()) return;

        if (modes.get(0) instanceof Comparable)
            Collections.sort((List<? extends Comparable>) modes);

        for (M mode : modes)
            mode.initialize();
    }

    Settings settings()
    {
        checkState(settings != null, "not initialized");
        return settings;
    }

    boolean isEmpty()
    {
        return modes.isEmpty();
    }

    int size()
    {
        return modes.size();
    }

    @SuppressWarnings("unchecked")
    M getById(String id)
    {
        return (M) idToModeMap.get(id);
    }

    M getByIndex(int index)
    {
        return modes.get(index);
    }

    int indexOf(M mode)
    {
        return modes.indexOf(mode);
    }

    Iterable<M> modes()
    {
        return modes;
    }

    boolean add(M mode)
    {
        checkNotNull(mode, "mode");
        checkArgument(!idToModeMap.containsKey(mode.id()), "mode id %s is duplicate.", mode.id());

        idToModeMap.put(mode.id(), mode);
        return modes.add(mode);
    }

}
