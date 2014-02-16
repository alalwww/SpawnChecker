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

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

/**
 * 自動保存用のTickEventリスナーを持った設定保持クラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public class ConfigHolder
{
    private Logger logger;
    private long interval = 3000L;

    private final ConcurrentMap<Class<? extends ConfigCategory>, ConfigCategory> holder;

    /**
     * Constructor.
     * 
     * @param configCategories 設定カテゴリ
     */
    public ConfigHolder(ConfigCategory... configCategories)
    {
        checkArgument(checkNotNull(configCategories, "settings").length > 0);

        holder = Maps.newConcurrentMap();
        for (ConfigCategory category : configCategories)
            holder.put(category.getClass(), category);
    }

    /**
     * 保持している設定マップを取得します.
     * 
     * @return 設定マップ
     */
    public ImmutableMap<Class<? extends ConfigCategory>, ConfigCategory> holder()
    {
        return ImmutableMap.copyOf(holder);
    }

    /**
     * 保持する設定カテゴリを追加します.
     * 
     * <p>
     * 重複する設定があった場合設定マップより削除された設定を返します。重複がなければnullです。
     * </p>
     * 
     * @param configCategory 設定カテゴリ
     * @return null または先に保持されていた設定
     */
    @SuppressWarnings("unchecked")
    public <T extends ConfigCategory> T add(T configCategory)
    {
        return (T) holder.put(configCategory.getClass(), configCategory);
    }

    /**
     * 保存する必要があるか確認を行う間隔を設定します.
     * 
     * @param interval 間隔
     * @return このインスタンス
     */
    public ConfigHolder setInterval(long interval)
    {
        checkArgument(interval > 0, "%s is negative value.", interval);
        this.interval = interval;
        return this;
    }

    /**
     * ロガーを設定します.
     * 
     * @param logger ロガー
     * @return このインスタンス
     */
    public ConfigHolder setLogger(Logger logger)
    {
        this.logger = checkNotNull(logger, "logger");
        return this;
    }

    /**
     * 指定した{@link ConfigCategory}のインスタンスを取得.
     * 
     * @param keyClass {@link ConfigCategory}クラス
     * @return 保持しているインスタンスまたはnull
     */
    @SuppressWarnings("unchecked")
    public <T extends ConfigCategory> T get(Class<T> keyClass)
    {
        return (T) holder.get(checkNotNull(keyClass, "keyClass"));
    }

    /**
     * 自動保存を行うTickイベントリスナを取得.
     * 
     * @return リスナー
     */
    public Object newTickEventListener()
    {
        return new TickEventListener();
    }

    private final Set<Configuration> forgeConfigSetTemp = Sets.newHashSet();

    private void saveConfigIfChanged()
    {
        for (ConfigCategory settings : holder.values())
            if (settings.isSettingChanged())
            {
                forgeConfigSetTemp.add(settings.config.forgeConfig);
                settings.clearChangedFlag();
            }

        if (forgeConfigSetTemp.isEmpty()) return;

        if (logger != null)
            logger.info("saving configuration.");

        for (Configuration forgeConfig : forgeConfigSetTemp)
            forgeConfig.save();

        forgeConfigSetTemp.clear();
    }

    /**
     * イベントリスナ
     * 
     * @author alalwww
     * @version 1.0
     */
    public final class TickEventListener
    {
        /** タイマー. */
        private long nextSaveTime;

        private World world;

        private TickEventListener()
        {
        }

        /**
         * 保存用のTickイベントハンドラー.
         * 
         * @param event ClientTickイベント
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void handleClientTick(ClientTickEvent event)
        {
            autoSave(event);
        }

        @SubscribeEvent
        @SideOnly(Side.SERVER)
        public void handleServerTick(ServerTickEvent event)
        {
            autoSave(event);
        }

        private void autoSave(TickEvent event)
        {
            if (event.phase != Phase.END) return;

            // ログアウトなどでワールドがなくなったら保存を試行
            if (Minecraft.getMinecraft().theWorld != world)
            {
                if (world != null)
                    saveConfigIfChanged();
                world = Minecraft.getMinecraft().theWorld;
                return;
            }

            // インターバル以上の時間が経過していたら保存を試行
            final long now = Minecraft.getSystemTime();
            if (now < nextSaveTime) return;
            nextSaveTime = now + interval;

            saveConfigIfChanged();
        }
    }
}
