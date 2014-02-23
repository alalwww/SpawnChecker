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
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
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
    public static OptionSet of(Mode.Option... options)
    {
        return copyOf(Arrays.asList(options));
    }

    public static OptionSet copyOf(Iterable<Mode.Option> options)
    {
        final OptionSet set = new OptionSet();

        for (Mode.Option option : options)
            set.add(option);

        set.freeze = true;
        return set;
    }

    public static Builder builder()
    {
        return new Builder();
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

    /**
     * OptionSetビルダー.
     * 
     * @author alalwww
     */
    public static class Builder
    {
        private final Set<Mode.Option> set = Sets.newLinkedHashSet();

        private Builder()
        {
        }

        /**
         * オプションを追加します.
         * 
         * @param e モードオプション
         * @return ビルダー
         */
        public Builder add(Mode.Option e)
        {
            set.add(checkNotNull(e, "e is null"));
            return this;
        }

        /**
         * オプションを追加します.
         * 
         * @param c モードオプション
         * @return ビルダー
         */
        public Builder addAll(Collection<Mode.Option> c)
        {
            checkArgument(!checkNotNull(c, "c is null").isEmpty(), "empty");
            set.addAll(c);
            return this;
        }

        /**
         * オプションを追加します.
         * 
         * @param ite モードオプション
         * @return ビルダー
         */
        public Builder addAll(Iterable<Mode.Option> ite)
        {
            boolean empty = true;

            for (Mode.Option option : checkNotNull(ite, "ite is null"))
            {
                add(option);
                empty = false;
            }

            checkArgument(!empty, "empty");

            return this;
        }

        /**
         * オプションを追加します.
         * 
         * @param a モードオプション
         * @return ビルダー
         */
        public Builder addAll(Mode.Option... a)
        {
            checkArgument(checkNotNull(a, "a is null").length > 0, "empty");
            return addAll(Arrays.asList(a));
        }

        public OptionSet build()
        {
            checkState(!set.isEmpty(), "empty");
            return copyOf(set);
        }
    }
}
