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

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.common.SimpleInformation;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.information.InformationManager;

/**
 * abstract mode.
 * 
 * @author alalwww
 * @param <T> type of child
 */
public abstract class ModeBase<T extends ModeBase<T>> implements Mode
{
    private static final Logger LOG = LogManager.getLogger(SpawnChecker.MOD_ID);

    private final String id;
    private final int ordinal;
    private String nameKey;

    private final List<OptionSet> optionSetList = Lists.newArrayList();
    private int cursor;

    protected final CopiedLogics copiedLogics = new CopiedLogics(ConstantsConfig.instance());

    /**
     * Constructor.
     * 
     * @param id モードID
     * @param ordinal モードの序数
     */
    protected ModeBase(String id, int ordinal)
    {
        this.id = id;
        this.ordinal = ordinal;
    }

    /**
     * @see Mode#begin()
     */
    protected abstract void start();

    /**
     * @see Mode#update()
     */
    protected abstract void onUpdate();

    /**
     * @see Mode#end()
     */
    protected abstract void stop();

    @Override
    public final String id()
    {
        return id;
    }

    @Override
    public final int ordinal()
    {
        return ordinal;
    }

    @Override
    public String iconResourceName()
    {
        return null;
    }

    @Override
    public String name()
    {
        return nameKey == null ? id() : I18n.format(nameKey);
    }

    @Override
    public boolean enabled()
    {
        return true;
    }

    @Override
    public void begin()
    {
        resetInformationForModeChange();
        start();
    }

    @Override
    public void update()
    {
        onUpdate();
    }

    @Override
    public void end()
    {
        stop();
    }

    @Override
    public void renderGui(long tickCount, float partialTick)
    {
        // 描く必要がなければ何もしない
    }

    /*
     * 上キー操作.
     */
    @Override
    public void onUpKeyPress(boolean shift, boolean alt)
    {
        // TODO: オプションのループ設定
        if (cursor == 0)
            return;

        cursor--;

        resetInformationForModeChange();
    }

    /*
     * 下キー操作.
     */
    @Override
    public void onDownKeyPress(boolean shift, boolean alt)
    {
        // TODO: オプションのループ設定
        if (cursor + 1 >= optionSetList.size())
            return;

        cursor++;

        resetInformationForModeChange();
    }

    /*
     * テンキー「+」の操作.
     * <ul>
     * <li>そのまま押したら水平方向の範囲拡大</li>
     * <li>SHIFT+かATL+で垂直方向の範囲拡大</li>
     * <li>CTRL+で明るさUP</li>
     * </ul>
     */
    @Override
    public void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        final boolean changed;
        final int current;

        if (ctrl)
        {
            changed = commonState().brightness().increment();
            current = commonState().brightness().current();
            LOG.debug("brightness: changed={}, current={}", changed, current);
            resetInformationForBrightnessChange();
            return;
        }

        if (alt || shift)
        {
            changed = commonState().verticalRange().increment();
            current = commonState().verticalRange().current();
            LOG.debug("verticalRange: changed={}, current={}", changed, current);
            resetInformationForRangeChange();
            return;
        }

        changed = commonState().horizontalRange().increment();
        current = commonState().horizontalRange().current();
        LOG.debug("horizontalRange: changed={}, current={}", changed, current);
        resetInformationForRangeChange();
    }

    /*
     * テンキー「－」の操作.
     * <ul>
     * <li>そのまま押したら水平方向の範囲縮小</li>
     * <li>SHIFT+かATL+で垂直方向の範囲縮小</li>
     * <li>CTRL+で明るさDOWN</li>
     * </ul>
     */
    @Override
    public void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        final boolean changed;
        final int current;

        if (ctrl)
        {
            changed = commonState().brightness().decrement();
            current = commonState().brightness().current();
            LOG.debug("brightness: changed={}, current={}", changed, current);
            resetInformationForBrightnessChange();
            return;
        }

        if (alt || shift)
        {
            changed = commonState().verticalRange().decrement();
            current = commonState().verticalRange().current();
            LOG.debug("verticalRange: changed={}, current={}", changed, current);
            resetInformationForRangeChange();
            return;
        }

        changed = commonState().horizontalRange().decrement();
        current = commonState().horizontalRange().current();
        LOG.debug("horizontalRange: changed={}, current={}", changed, current);
        resetInformationForRangeChange();
    }

    // --------------------------------------------------

    protected void setNameKey(String nameKey)
    {
        this.nameKey = checkNotNull(nameKey, "nameKey");
    }

    protected void addOptionSet(OptionSet optionSet)
    {
        optionSetList.add(optionSet);
    }

    protected void setSelectedOption(int cursor)
    {
        this.cursor = cursor;
    }

    /**
     * @return 現在のオプションの一覧
     */
    protected OptionSet options()
    {
        return optionSetList.get(cursor);
    }

    protected void resetInformationForModeChange()
    {
        information().clear();
        information().add(SimpleInformation.of(name())
                .setColor(commonColor().informationMessage())
                .setIconResourceName(iconResourceName()));

        for (Mode.Option option : options())
            information().add(SimpleInformation.of(option.name())
                    .setColor(commonColor().informationMessage()));
    }

    protected void resetInformationForRangeChange()
    {
        information().clear();
        information().add(SimpleInformation.of(name())
                .setColor(commonColor().informationMessage())
                .setIconResourceName(iconResourceName()));

        information().add(SimpleInformation.of(
                I18n.format("spawnchecker.info.horizontal_range", commonState().horizontalRange().current()))
                .setColor(commonColor().informationMessage())
                .setIconResourceName("spawnchecker:icon/horizontal_range.png")
                .setOffsetX(5));

        information().add(SimpleInformation.of(
                I18n.format("spawnchecker.info.vertical_range", commonState().verticalRange().current()))
                .setColor(commonColor().informationMessage())
                .setIconResourceName("spawnchecker:icon/vertical_range.png")
                .setOffsetX(5));
    }

    protected void resetInformationForBrightnessChange()
    {
        information().clear();
        information().add(SimpleInformation.of(name())
                .setColor(commonColor().informationMessage())
                .setIconResourceName(iconResourceName()));

        information().add(SimpleInformation.of(
                I18n.format("spawnchecker.info.brightness", commonState().brightness().current()))
                .setColor(commonColor().informationMessage())
                .setIconResourceName("spawnchecker:icon/brightness.png")
                .setOffsetX(5));
    }

    // --------------------------------------------------

    protected static final Collection<Block> enablingItems()
    {
        return ModeManager.instance().settings().common().enablingItems();
    }

    protected static final Mode.CommonState commonState()
    {
        return ModeManager.instance().settings().state();
    }

    protected static final Mode.CommonColor commonColor()
    {
        return ModeManager.instance().settings().color();
    }

    protected static final InformationManager information()
    {
        return InformationManager.instance();
    }

    // --------------------------------------------------

    @Override
    public final int compareTo(Mode o)
    {
        final int result = Ints.compare(ordinal(), o.ordinal());
        if (result != 0) return result;
        return id().compareTo(o.id());
    }

    @Override
    public final int hashCode()
    {
        return id().hashCode();
    }

    @Override
    public final boolean equals(Object obj)
    {
        if (this == obj) return true;
        return getClass().equals(obj.getClass()) && Objects.equal(id(), getClass().cast(obj).id());
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(getClass())
                .add("mode id", id())
                .add("name", name())
                .add("ordinal", ordinal())
                .toString();
    }
}
