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

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import net.minecraftforge.client.event.RenderWorldLastEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import net.awairo.mcmod.spawnchecker.client.ClientManager;
import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.marker.RenderingSupport;
import net.awairo.mcmod.spawnchecker.client.mode.AlwaysRunMode;
import net.awairo.mcmod.spawnchecker.client.mode.ConditionalMode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;

/**
 * イベントを契機にモードの状態管理などを行います.
 * 
 * @author alalwww
 */
public final class ModeManager extends ClientManager
{
    private static ModeManager instance = new ModeManager();

    private final SelectableModeContainer selectableModeContainer = new SelectableModeContainer();
    private final ConditionalModeContainer conditionalModeContainer = new ConditionalModeContainer();
    private final AlwaysRunModeContainer alwaysRunModeContainer = new AlwaysRunModeContainer();

    /** 現在起動している操作可能モードを格納するモードコンテナ. */
    private OperatableModeContainer currentOperatableModeContainer;

    /** 条件起動モードが起動しており、選択起動モードに戻すことが予定されている場合trueになる. */
    private boolean scheduleOfResetToPreviousMode;

    /**
     * ワールド変更の検知用. (現在のワールド参照またはnull)
     */
    private World currentWorld;

    /** 最後にモードを更新した時間(ミリ秒)　 */
    private long lastUpdateTime;

    /** ワールドが存在する間clientのtickが呼ばれるたびにインクリメントされるカウンター. */
    private long tickCounts;

    /**
     * 初期化済みフラグ.
     * 
     * @see #addMode(Mode)
     * @see #initialize()
     */
    private boolean initialized = false;

    // ------------------------------------------------------------------------------------

    /** クラスロード用. */
    public static void load()
    {
    }

    static ModeManager instance()
    {
        return instance;
    }

    // ------------------------------------------------------------------------------------

    /** Constructor. */
    private ModeManager()
    {
    }

    @Override
    protected Settings settings()
    {
        return super.settings();
    }

    // ------------------------------------------------------------------------------------

    /**
     * モードを追加します.
     * 
     * @param mode 追加するモード
     */
    public void addMode(Mode mode)
    {
        checkInitializedState();

        if (mode instanceof SelectableMode)
        {
            selectableModeContainer.add((SelectableMode) mode);
            return;
        }

        if (mode instanceof ConditionalMode)
        {
            conditionalModeContainer.add((ConditionalMode) mode);
            return;
        }

        if (mode instanceof AlwaysRunMode)
        {
            alwaysRunModeContainer.add((AlwaysRunMode) mode);
            return;
        }

        throw new IllegalArgumentException(mode.getClass().getName() + " is unknown mode type.");
    }

    /**
     * マネージャーの初期化と、各モードの初期化を行います.
     */
    public void initialize()
    {
        checkInitializedState();

        selectableModeContainer.initialize(settings());
        conditionalModeContainer.initialize(settings());
        alwaysRunModeContainer.initialize(settings());

        currentOperatableModeContainer = selectableModeContainer;
        initialized = true;
    }

    private void checkInitializedState()
    {
        checkState(!initialized, "initialized manager. ur too late!");
    }

    // ------------------------------------------------------------------------------------

    /**
     * 指定方向へのモード変更の計画を追加します.
     * 
     * <p>現在モードが条件起動モードだった場合、直前の選択起動モードに戻します。</p>
     */
    public void changeModeUp()
    {
        if (currentModeIsSelectableMode())
        {
            selectableModeContainer.next();
            return;
        }

        scheduleOfResetToPreviousMode = true;
    }

    /**
     * 指定方向へのモード変更の計画を追加します.
     * 
     * <p>現在モードが条件起動モードだった場合、直前の選択起動モードに戻します。</p>
     */
    public void changeModeDown()
    {
        if (currentModeIsSelectableMode())
        {
            selectableModeContainer.prev();
            return;
        }

        scheduleOfResetToPreviousMode = true;
    }

    /** @return trueは現在のモードがSelectableModeかSelectableModeへの変更が既に予定されている */
    private boolean currentModeIsSelectableMode()
    {
        return currentOperatableModeContainer == selectableModeContainer || scheduleOfResetToPreviousMode;
    }

    /**
     * 現在の操作可能モードにキー押下の通知を送る.
     * 
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    public void onUpKeyPress(boolean shift, boolean alt)
    {
        currentOperatableModeContainer.onUpKeyPress(shift, alt);
    }

    /**
     * 現在の操作可能モードにキー押下の通知を送る.
     * 
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    public void onDownKeyPress(boolean shift, boolean alt)
    {
        currentOperatableModeContainer.onDownKeyPress(shift, alt);
    }

    /**
     * 現在の操作可能モードにキー押下の通知を送る.
     * 
     * @param ctrl trueはコントコールキーが押下されている
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    public void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        currentOperatableModeContainer.onPlusKeyPress(ctrl, shift, alt);
    }

    /**
     * 現在の操作可能モードにキー押下の通知を送る.
     * 
     * @param ctrl trueはコントコールキーが押下されている
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    public void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        currentOperatableModeContainer.onMinusKeyPress(ctrl, shift, alt);
    }

    // ------------------------------------------------------------------------------------

    /**
     * ワールドが変わっていて、モードがすでに初期化されている場合、リセット.
     */
    private void resetIfWorldChanged()
    {
        final World wowld = Minecraft.getMinecraft().world;

        // ワールド変わっていないので何もしない
        if (currentWorld == wowld)
            return;

        currentWorld = wowld;

        // ワールドが変わっているので停止
        alwaysRunModeContainer.stopIfStarted();
        currentOperatableModeContainer.stopIfStarted();
    }

    /**
     * 現在のモードを更新.
     */
    private void updateMode()
    {
        setNewModeIfScheduled();

        // 開始していないモードは開始
        alwaysRunModeContainer.startIfStopped();
        currentOperatableModeContainer.startIfStopped();

        if (isNotUpdateTiming())
            return;

        alwaysRunModeContainer.update();
        currentOperatableModeContainer.update();

        lastUpdateTime = Minecraft.getSystemTime();
    }

    /**
     * 更新タイミングか判定.
     * 
     * @return true は更新タイミングではない
     */
    private boolean isNotUpdateTiming()
    {
        // 最後に更新してから一定時間が立っていればtrue
        return Minecraft.getSystemTime() - lastUpdateTime < settings().mode().updateFrequency.getInt();
    }

    /**
     * モード変更が予定されている場合、新しいモードに変更する .
     */
    private void setNewModeIfScheduled()
    {
        if (!scheduleOfResetToPreviousMode)
        {
            // 条件起動モードを抜けるフラグが立っていない場合、条件起動モードが有効か判定
            if (conditionalModeContainer.enabled())
            {
                // 現在モードが条件起動モードではなければ、切り替える
                if (currentOperatableModeContainer != conditionalModeContainer)
                {
                    currentOperatableModeContainer.stopIfStarted();
                    currentOperatableModeContainer = conditionalModeContainer;
                }
                return;
            }

            // 現在条件起動モードになっており、既に無効になっていたら、変更を予定する
            if (currentOperatableModeContainer == conditionalModeContainer)
                scheduleOfResetToPreviousMode = true;
        }

        if (scheduleOfResetToPreviousMode || selectableModeContainer.modeChangeScheduled())
        {
            // 条件起動モードから戻す予定がされていたら、戻す
            if (scheduleOfResetToPreviousMode)
            {
                scheduleOfResetToPreviousMode = false;
                currentOperatableModeContainer.stopIfStarted();
                currentOperatableModeContainer = selectableModeContainer;
            }

            // 選択可能モードの変更が予定されていたら変更する
            selectableModeContainer.changeNewModeIfScheduled();
        }
    }

    /** ワールド内にマーカーを描画. */
    private void renderMarker(float partialTicks)
    {
        RenderingSupport.beginRendering();

        currentOperatableModeContainer.renderIngame(tickCounts, partialTicks);
        alwaysRunModeContainer.renderIngame(tickCounts, partialTicks);

        RenderingSupport.endRendering();
    }

    /** 画面上に情報を描画. */
    private void renderGui(float partialTicks)
    {
        currentOperatableModeContainer.renderGui(tickCounts, partialTicks);
        alwaysRunModeContainer.renderGui(tickCounts, partialTicks);
    }

    private static boolean notStartedTheWorld()
    {
        return Minecraft.getMinecraft().world == null;
    }

    // ------------------------------------------------------------------------------------

    @Override
    protected Object newFmlEventListener()
    {
        // アクセス可能なクラスじゃないとイベントバスが解析してくれなかったため、ここは無名クラスにできない
        return new FMLEventListener(this);
    }

    @Override
    protected Object newForgeEventListener()
    {
        // アクセス可能なクラスじゃないとイベントバスが解析してくれなかったため、ここは無名クラスにできない
        return new ForgeEventListener(this);
    }

    // ------------------------------------------------------------------------------------

    /**
     * FMLイベントのリスナー.
     * 
     * @author alalwww
     */
    public static final class FMLEventListener
    {
        private final ModeManager manager;

        private FMLEventListener(ModeManager manager)
        {
            this.manager = checkNotNull(manager);
        }

        /**
         * @param event RenderTickEvent
         */
        @SubscribeEvent
        public void handleClientTick(RenderTickEvent event)
        {
            if (event.phase != Phase.START)
                return;

            try
            {
                // tick.SpawnChecker
                profiler().startSection("SpawnChecker");

                manager.resetIfWorldChanged();

                if (notStartedTheWorld())
                    return;

                manager.tickCounts++;

                manager.updateMode();
            }
            finally
            {

                profiler().endSection();
            }
        }

        /**
         * @param event RenderTickEvent
         */
        @SubscribeEvent
        public void handleRenderGameOverlay(RenderTickEvent event)
        {
            if (event.phase != Phase.END)
                return;

            if (notStartedTheWorld())
                return;

            // gameRenderer.gui.SpawnChecker
            profiler().startSection("gameRenderer");
            profiler().startSection("gui");
            profiler().startSection("SpawnChecker");

            manager.renderGui(event.renderTickTime);

            profiler().endSection();
            profiler().endSection();
            profiler().endSection();
        }
    }

    /**
     * Forgeイベントのリスナー.
     * 
     * @author alalwww
     */
    public static final class ForgeEventListener
    {
        private final ModeManager manager;

        private ForgeEventListener(ModeManager manager)
        {
            this.manager = checkNotNull(manager);
        }

        /**
         * @param event RenderWorldLastEvent
         */
        @SubscribeEvent
        public void handleRenderWorldLast(RenderWorldLastEvent event)
        {
            if (notStartedTheWorld())
                return;

            // gameRenderer.level.SpawnChecker
            profiler().endStartSection("SpawnChecker");

            manager.renderMarker(event.getPartialTicks());

            profiler().endStartSection("FRenderLast");
        }
    }

}
