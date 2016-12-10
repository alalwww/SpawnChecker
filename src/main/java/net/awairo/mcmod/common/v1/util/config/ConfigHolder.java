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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 自動保存用のTickEventリスナーを持った設定保持クラス.
 * 
 * @author alalwww
 * @version 1.0
 */
public class ConfigHolder
{
    /** 各カテゴリ毎の設定を保存するマップ. */
    protected final ConcurrentMap<Class<? extends ConfigCategory>, ConfigCategory> holder;
    /** 保存時にSaveを実行するForgeのConfigurationを一時保管する場所. */
    protected final Set<Configuration> forgeConfigSetTemp;

    private Logger logger;
    private long interval = 3000L;

    /**
     * Constructor.
     */
    protected ConfigHolder()
    {
        holder = Maps.newConcurrentMap();
        forgeConfigSetTemp = Sets.newHashSet();
    }

    /**
     * Constructor.
     * 
     * @param logger ロガー
     */
    protected ConfigHolder(Logger logger)
    {
        this();
        setLogger(logger);
    }

    /**
     * 保持している設定マップのコピーを取得します.
     * 
     * @return 設定マップ
     */
    public ImmutableMap<Class<? extends ConfigCategory>, ConfigCategory> copy()
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
     * 保存する必要があるか確認を行う間隔を設定します.
     * 
     * @param interval 間隔
     */
    protected void setInterval(long interval)
    {
        checkArgument(interval > 0, "%s is negative value.", interval);
        this.interval = interval;
    }

    /**
     * ロガーを設定します.
     * 
     * @param logger ロガー
     */
    protected void setLogger(Logger logger)
    {
        this.logger = checkNotNull(logger, "logger");
    }

    /**
     * 変更されている設定があれば保存します.
     */
    protected void saveConfigIfChanged()
    {
        // 変更チェック
        for (ConfigCategory category : holder.values())
        {
            if (category.isSettingChanged())
            {
                forgeConfigSetTemp.add(category.config.forgeConfig);
                category.clearChangedFlag();
            }
        }

        // 変更ないため何もしない
        if (forgeConfigSetTemp.isEmpty()) return;

        // ロガーが設定されていれば保存ログ
        if (logger != null)
            logger.info("saving configuration.");

        // 全ての変更されている設定を保存
        for (Configuration forgeConfig : forgeConfigSetTemp)
            forgeConfig.save();

        forgeConfigSetTemp.clear();
    }

    // ------------------------------------------------

    /**
     * 自動保存を行うTickイベントリスナを取得.
     * 
     * @return リスナー
     */
    public Object newTickEventListener()
    {
        return new TickEventListener();
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
            if (Minecraft.getMinecraft().world != world)
            {
                if (world != null)
                    saveConfigIfChanged();

                world = Minecraft.getMinecraft().world;
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
