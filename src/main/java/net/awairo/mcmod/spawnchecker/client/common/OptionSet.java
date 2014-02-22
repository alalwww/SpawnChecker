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

package net.awairo.mcmod.spawnchecker.client.common;

import static com.google.common.base.Preconditions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode.Option;

/**
 * Option set.
 * 
 * <p>変更不可能な{@link LinkedHashSet}です。</p>
 * 
 * @author alalwww
 */
public final class OptionSet extends LinkedHashSet<Mode.Option>
{
    /**
     * 新しいオプションセットを生成します.
     * 
     * @param options オプションの一覧 重複を含む場合はその要素は無視される
     * @return オプションセット
     */
    public static OptionSet of(final Mode.Option... options)
    {
        checkArgument(options.length > 0, "empty");

        final OptionSet set = new OptionSet();
        set.addAll(Arrays.asList(options));
        set.freeze = true;
        return set;
    }

    /**
     * 変更禁止フラグ.
     */
    private boolean freeze;

    private OptionSet()
    {
    }

    @Override
    @Deprecated
    public boolean add(Option e)
    {
        if (freeze)
            throw new UnsupportedOperationException();
        return super.add(e);
    }

    @Override
    @Deprecated
    public boolean addAll(Collection<? extends Option> c)
    {
        if (freeze)
            throw new UnsupportedOperationException();
        return super.addAll(c);
    }

    @Override
    @Deprecated
    public void clear()
    {
        if (freeze)
            throw new UnsupportedOperationException();
        super.clear();
    }

    @Override
    @Deprecated
    public boolean remove(Object o)
    {
        if (freeze)
            throw new UnsupportedOperationException();
        return super.remove(o);
    }

    @Override
    @Deprecated
    public boolean removeAll(Collection<?> c)
    {
        if (freeze)
            throw new UnsupportedOperationException();
        return super.removeAll(c);
    }

    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c)
    {
        if (freeze)
            throw new UnsupportedOperationException();
        return super.retainAll(c);
    }

    @Override
    public UnmodifiableIterator<Option> iterator()
    {
        return Iterators.unmodifiableIterator(super.iterator());
    }
}
