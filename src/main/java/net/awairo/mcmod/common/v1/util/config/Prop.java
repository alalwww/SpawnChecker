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

package net.awairo.mcmod.common.v1.util.config;

import static com.google.common.base.Preconditions.*;

import java.util.Arrays;

import com.google.common.base.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Property;

/**
 * 独自の変更通知を行う拡張を行った{@link Property} のラッパー.
 * 
 * <p> {@link Property#hasChanged()}で変更確認はできるが、変更時に通知が出来ないので
 * 事前に前設定値を取得しておかないといけないため、ラップし通知機能を追加。
 * </p>
 * 
 * @author alalwww
 * @version 1.0
 */
public final class Prop
{
    private static final Logger LOG = LogManager.getLogger();

    private final Property property;
    private final ConfigCategory settings;

    Prop(Property property, ConfigCategory settings)
    {
        this.property = property;
        this.settings = settings;
    }

    public boolean getBoolean()
    {
        return property.getBoolean(true);
    }

    public boolean[] getBooleanList()
    {
        return property.getBooleanList();
    }

    public double getDouble()
    {
        return property.getDouble(0);
    }

    public double[] getDoubleList()
    {
        return property.getDoubleList();
    }

    public int getInt()
    {
        return property.getInt();
    }

    public int[] getIntList()
    {
        return property.getIntList();
    }

    public String getName()
    {
        return property.getName();
    }

    public String getString()
    {
        return property.getString();
    }

    public String[] getStringList()
    {
        return property.getStringList();
    }

    public Property.Type getType()
    {
        return property.getType();
    }

    public boolean hasChanged()
    {
        return property.hasChanged();
    }

    public boolean isBooleanList()
    {
        return property.isBooleanList();
    }

    public boolean isBooleanValue()
    {
        return property.isBooleanValue();
    }

    public boolean isDoubleList()
    {
        return property.isDoubleList();
    }

    public boolean isDoubleValue()
    {
        return property.isDoubleValue();
    }

    public boolean isIntList()
    {
        return property.isIntList();
    }

    public boolean isIntValue()
    {
        return property.isIntValue();
    }

    public boolean isList()
    {
        return property.isList();
    }

    public void setName(String name)
    {
        property.setName(name);
    }

    public void set(boolean value)
    {
        checkArgument(isBooleanValue(), "illegal type of value(%s)", value);
        set(Boolean.toString(value));
    }

    public void set(int value)
    {
        checkArgument(isIntValue(), "illegal type of value(%s)", value);
        set(Integer.toString(value));
    }

    public void set(double value)
    {
        checkArgument(isDoubleValue(), "illegal type of value(%s)", value);
        set(Double.toString(value));
    }

    public void set(String value)
    {
        if (Objects.equal(property.getString(), value))
            return;

        property.set(value);
        setChangedFlag();
    }

    public void setList(boolean... values)
    {
        checkArgument(isBooleanList(), "illegal type of values");

        final String[] strs = new String[values.length];
        for (int i = 0; i < strs.length; i++)
            strs[i] = Boolean.toString(values[i]);

        setList(strs);
    }

    public void setList(int... values)
    {
        checkArgument(isIntList(), "illegal type of values");

        final String[] strs = new String[values.length];
        for (int i = 0; i < strs.length; i++)
            strs[i] = Integer.toString(values[i]);

        setList(strs);
    }

    public void setList(double... values)
    {
        checkArgument(isDoubleList(), "illegal type of values");

        final String[] strs = new String[values.length];
        for (int i = 0; i < strs.length; i++)
            strs[i] = Double.toString(values[i]);

        setList(strs);
    }

    public void setList(String... values)
    {
        if (Arrays.equals(property.getStringList(), values))
            return;

        property.set(values);
        setChangedFlag();
    }

    public boolean wasRead()
    {
        return property.wasRead();
    }

    public Prop comment(String comment)
    {
        property.comment = comment;
        return this;
    }

    private void setChangedFlag()
    {
        settings.setChangedFlag();

        if (LOG.isDebugEnabled())
            LOG.debug("changed {}", toString());
    }

    @Override
    public String toString()
    {
        return String.format("prop(%s): %s", property.getName(),
                (property.isList() ? Arrays.toString(property.getStringList()) : property.getString()));
    }
}
