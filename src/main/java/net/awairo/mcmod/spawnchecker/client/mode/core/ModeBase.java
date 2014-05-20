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

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Objects;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.client.common.SimpleInformation;
import net.awairo.mcmod.spawnchecker.client.mode.ConditionalMode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.OperatableMode;
import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;
import net.awairo.mcmod.spawnchecker.client.mode.information.InformationManager;

/**
 * abstract mode.
 * 
 * @author alalwww
 * @param <T> type of child
 */
public abstract class ModeBase<T extends ModeBase<T>> implements OperatableMode
{
    private static final Logger LOG = LogManager.getLogger(SpawnChecker.MOD_ID);

    private final String id;

    private List<OptionSet> optionSetList;
    private int cursor;

    protected final CopiedLogics copiedLogics = CopiedLogics.INSTANCE;

    /**
     * Constructor.
     * 
     * @param id モードID
     */
    protected ModeBase(String id)
    {
        this.id = id;
    }

    /**
     * @return モードの名称のリソースキーまたはnull
     */
    protected abstract String modeNameKey();

    /**
     * @return モードのコンフィグレーション.
     */
    protected abstract ModeConfig.SubCategory config();

    /**
     * @see Mode#start()
     */
    protected abstract void onStart();

    /**
     * @see Mode#update()
     */
    protected abstract void onUpdate();

    /**
     * @see Mode#stop()
     */
    protected abstract void onStop();

    @Override
    public final String id()
    {
        return id;
    }

    @Override
    public String iconResourceName()
    {
        return null;
    }

    @Override
    public String name()
    {
        return modeNameKey() == null ? id() : I18n.format(modeNameKey());
    }

    @Override
    public void initialize()
    {
        optionSetList = config().getOptionSetList();
        cursor = optionSetList.indexOf(config().selectedOptionSet());
    }

    @Override
    public void start()
    {
        resetInformationForModeChange();
        onStart();
    }

    @Override
    public void update()
    {
        onUpdate();
    }

    @Override
    public void stop()
    {
        onStop();
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
        if (optionSetList == null)
            return;

        // TODO: オプションのループ設定
        if (cursor == 0)
            return;

        cursor--;

        config().setSelectedOptionSet(options());

        resetInformationForModeChange();
    }

    /*
     * 下キー操作.
     */
    @Override
    public void onDownKeyPress(boolean shift, boolean alt)
    {
        if (optionSetList == null)
            return;

        // TODO: オプションのループ設定
        if (cursor + 1 >= optionSetList.size())
            return;

        cursor++;

        config().setSelectedOptionSet(options());

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

    /**
     * 有効化アイテム一覧の設定値を取得します.
     * 
     * @return 有効化アイテムの設定
     */
    public final Collection<Block> enablingItems()
    {
        return ModeManager.instance().settings().mode().enablingItems();
    }

    /**
     * モードで使用できる共通の状態値を取得します.
     * 
     * @return 共通ステート
     */
    public final Mode.CommonState commonState()
    {
        return ModeManager.instance().settings().state();
    }

    /**
     * モードで使用できる共通の色設定を取得します.
     * 
     * @return 共通の色設定
     */
    public final Mode.CommonColor commonColor()
    {
        return ModeManager.instance().settings().color();
    }

    /**
     * 画面に表示する情報の管理オブジェクトを取得します.
     * 
     * <p>このメソッドは{@code InformationManager.instance()}のシンタックスシュガーです。</p>
     * 
     * @return 情報表示マネージャ
     */
    public final InformationManager information()
    {
        return InformationManager.instance();
    }

    // --------------------------------------------------

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
        final Objects.ToStringHelper helper = Objects.toStringHelper(getClass())
                .add("mode id", id())
                .add("name", name());

        if (this instanceof SelectableMode)
            helper.add("ordinal", ((SelectableMode) this).ordinal());

        if (this instanceof ConditionalMode)
            helper.add("priority", ((ConditionalMode) this).priority());

        return helper.toString();
    }
}
