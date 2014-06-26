/*
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.v1.util;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import net.awairo.mcmod.common.v1.util.config.Prop;

/**
 * 上限下限値を持った増減できる数値.
 * 
 * @author alalwww
 * @version 1.0
 */
public abstract class LimitedNumber<T extends Number & Comparable<T>>
{
    /**
     * 上限下限値を持った増減できる新しいInteger型を取得します.
     * 
     * @return Integer型の{@link LimitedNumber}
     */
    public static Builder<Integer> ofInteger(final Prop prop)
    {
        return new Builder<Integer>()
        {
            {
                current(prop.getInt());
                min(Integer.MIN_VALUE);
                max(Integer.MAX_VALUE);
                step(1);
            }

            @Override
            protected LimitedNumber<Integer> newInstance(Integer current, Integer min, Integer max, Integer step)
            {
                return new LimitedNumber<Integer>(current, min, max, step)
                {
                    @Override
                    protected Integer incrementValue(Integer current, Integer step)
                    {
                        return Ints.saturatedCast(current.longValue() + step.longValue());
                    }

                    @Override
                    protected Integer decrementValue(Integer current, Integer step)
                    {
                        return Ints.saturatedCast(current.longValue() - step.longValue());
                    }

                    @Override
                    protected void updated()
                    {
                        prop.set(current());
                    }
                };
            }
        };
    }

    /**
     * 上限下限値を持った増減できる新しいLong型を取得します.
     * 
     * @return Long型の{@link LimitedNumber}
     */
    public static Builder<Long> ofLong(final Prop prop, final long defaultValue)
    {
        return new Builder<Long>()
        {
            {
                current(Optional.fromNullable(Longs.tryParse(prop.getString())).or(defaultValue));
                min(Long.MIN_VALUE);
                max(Long.MAX_VALUE);
                step(1L);
            }

            @Override
            protected LimitedNumber<Long> newInstance(Long current, Long min, Long max, Long step)
            {
                return new LimitedNumber<Long>(current, min, max, step)
                {
                    @Override
                    protected Long incrementValue(Long current, Long step)
                    {
                        // Long上限突破する場合は上限を返す
                        return current > 0 && Long.MAX_VALUE - current < step
                                ? Long.MAX_VALUE : current + step;
                    }

                    @Override
                    protected Long decrementValue(Long current, Long step)
                    {
                        // Long下限突破する場合は下限を返す
                        return current < 0 && current - Long.MIN_VALUE < step
                                ? Long.MIN_VALUE : current - step;
                    }

                    @Override
                    protected void updated()
                    {
                        prop.set(current().toString());
                    }
                };
            }
        };
    }

    /**
     * limited number builder.
     * 
     * @author alalwww
     * 
     * @param <T> type of comparable number
     */
    public static abstract class Builder<T extends Number & Comparable<T>>
    {
        private T min;
        private T max;
        private T step;
        private T current;

        public Builder<T> current(T current)
        {
            this.current = checkNotNull(current);
            return this;
        }

        public Builder<T> min(T min)
        {
            this.min = checkNotNull(min);
            return this;
        }

        public Builder<T> max(T max)
        {
            this.max = checkNotNull(max);
            return this;
        }

        public Builder<T> step(T step)
        {
            this.step = checkNotNull(step);
            checkArgument(step.intValue() > 0, "negative value");
            return this;
        }

        public LimitedNumber<T> build()
        {
            checkState(current != null, "current is null");
            checkState(min != null, "min is null");
            checkState(max != null, "max is null");
            checkState(step != null, "step is null");

            checkState(min.compareTo(max) < 0,
                    "min(%s) is greater than max(%s)", min, max);

            checkState(current.compareTo(min) >= 0,
                    "current(%s) is lesser than min(%s)", current, min);
            checkState(current.compareTo(max) <= 0,
                    "current(%s) is greater than max(%s)", current, max);

            checkState(step.compareTo(max) < 0,
                    "step(%s) is greater than or equals max(%s)", step, max);

            return newInstance(current, min, max, step);
        }

        protected abstract LimitedNumber<T> newInstance(T current, T min, T max, T step);
    }

    private final T min;
    private final T max;
    private final T step;

    private T current;

    /**
     * Constructor.
     */
    protected LimitedNumber(T current, T min, T max, T step)
    {
        this.current = checkNotNull(current);
        this.min = checkNotNull(min);
        this.max = checkNotNull(max);
        this.step = checkNotNull(step);

        checkArgument(!min.equals(max));
        checkArgument(current.compareTo(min) >= 0);
        checkArgument(current.compareTo(max) <= 0);
        checkArgument(step.compareTo(max) < 0);
    }

    protected abstract T incrementValue(T current, T step);

    protected abstract T decrementValue(T current, T step);

    protected abstract void updated();

    public T current()
    {
        return current;
    }

    public boolean increment()
    {
        if (isMaxValue()) return false;

        final T newValue = incrementValue(current, step);
        current = newValue.compareTo(max) >= 0 ? max : newValue;
        updated();
        return true;
    }

    public boolean decrement()
    {
        if (isMinValue()) return false;

        final T newValue = decrementValue(current, step);
        current = newValue.compareTo(min) <= 0 ? min : newValue;
        updated();
        return true;
    }

    public boolean isMaxValue()
    {
        return current.compareTo(max) >= 0;
    }

    public boolean isMinValue()
    {
        return current.compareTo(min) <= 0;
    }
}
