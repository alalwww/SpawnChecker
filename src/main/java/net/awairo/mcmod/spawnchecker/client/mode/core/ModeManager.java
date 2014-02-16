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

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import net.awairo.mcmod.spawnchecker.client.ClientManager;
import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.common.State.Direction;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.model.RenderingSupport;

/**
 * イベントを契機にモードの状態管理などを行います.
 * 
 * @author alalwww
 */
public final class ModeManager extends ClientManager
{
    private static ModeManager instance = new ModeManager();

    private final Map<String, Mode> idToModeMap = Maps.newHashMap();
    private final List<Mode> sortedModeList = Lists.newArrayList();

    private int selectedModeCursor;

    private long tickCount;
    private long lastUpdateTime;

    private boolean beganMode;

    public static void load()
    {
    }

    private ModeManager()
    {
    }

    static ModeManager instance()
    {
        return instance;
    }

    @Override
    protected Settings settings()
    {
        return super.settings();
    }

    public void addMode(Mode mode)
    {
        checkArgument(!idToModeMap.containsKey(mode.id()), "mode id %s is duplicate.", mode.id());

        idToModeMap.put(mode.id(), mode);
        sortedModeList.add(mode);
    }

    public void initialize()
    {
        Collections.sort(sortedModeList);
        selectedModeCursor = sortedModeList.indexOf(idToModeMap.get(settings().common().selectedMode.getString()));
        if (selectedModeCursor < 0) selectedModeCursor = 0;

        for (Mode mode : sortedModeList)
            mode.initialize();
    }

    private void updateMode()
    {
        final World wowld = Minecraft.getMinecraft().theWorld;

        resetIfWorldChanged(wowld);

        if (wowld == null) return;

        tickCount++;

        if (isNotUpdateTiming()) return;

        setNewModeIfScheduled();

        settings().state().currentMode().update();

        lastUpdateTime = Minecraft.getSystemTime();
    }

    /** @return true は更新タイミングではない */
    private boolean isNotUpdateTiming()
    {
        return Minecraft.getSystemTime() - lastUpdateTime < settings().common().updateFrequency.getInt();
    }

    /** ワールドが変わっていて、モードがすでに初期化されている場合、モードリセット . */
    private void resetIfWorldChanged(World wowld)
    {
        if (settings().state().currentWorld() == wowld)
            return;

        if (!settings().state().modeInitialized())
            return;

        // モードが開始していれば一度終了する
        if (beganMode)
        {
            settings().state().currentMode().end();
            beganMode = false;
        }

        settings().state().setWorld(wowld);

        // ワールドがあるなら開始する
        if (wowld != null && !beganMode)
        {
            settings().state().currentMode().begin();
            beganMode = true;
        }
    }

    /** モード変更が予定されているか、まだモードが設定されていない場合、新しいモードを設定する . */
    private void setNewModeIfScheduled()
    {
        if (!settings().state().modeChangeScheduled() && settings().state().modeInitialized())
            return;

        // モードが開始していたら終了
        if (beganMode)
        {
            settings().state().currentMode().end();
            beganMode = false;
        }

        // 新しいモードを設定して開始
        final Mode newMode = getNewMode();
        settings().state().setNewMode(newMode);
        newMode.begin();
        beganMode = true;
    }

    private Mode getNewMode()
    {
        int newCursor = selectedModeCursor;

        final int size = sortedModeList.size();
        for (Direction d : settings().state().directions())
        {
            newCursor += d == Direction.UP ? 1 : -1;

            // TODO: モード選択にループ機能を設けてもいいかも

            if (newCursor < 0)
                newCursor = 0;

            if (newCursor >= size)
                newCursor = size - 1;
        }

        selectedModeCursor = newCursor;

        return sortedModeList.get(selectedModeCursor);
    }

    private void renderMarker(float partialTick)
    {
        if (!beganMode) return;

        RenderingSupport.beginRendering();
        settings().state().currentMode().renderIngame(tickCount, partialTick);
        RenderingSupport.endRendering();
    }

    private void renderGui(float partialTick)
    {
        if (!beganMode) return;

        settings().state().currentMode().renderGui(tickCount, partialTick);
    }

    // ------------------------------------------------------------------------------------

    @Override
    protected Object newFmlEventListener()
    {
        return new FMLEventListener();
    }

    @Override
    protected Object newForgeEventListener()
    {
        return new ForgeEventListener();
    }

    // ------------------------------------------------------------------------------------

    /**
     * FMLイベントのリスナー.
     * 
     * @author alalwww
     */
    public final class FMLEventListener
    {
        private FMLEventListener()
        {
        }

        @SubscribeEvent
        public void handleClientTick(ClientTickEvent event)
        {
            if (event.phase != Phase.START) return;

            profiler().startSection("SpawnChecker");
            updateMode();
            profiler().endSection();
        }

        @SubscribeEvent
        public void handleRenderGameOverlay(RenderTickEvent event)
        {
            if (Minecraft.getMinecraft().theWorld == null
                    || event.phase != Phase.END)
                return;

            profiler().startSection("SpawnChecker");
            renderGui(event.renderTickTime);
            profiler().endSection();

        }
    }

    /**
     * Forgeイベントのリスナー.
     * 
     * @author alalwww
     */
    public final class ForgeEventListener
    {
        private ForgeEventListener()
        {
        }

        @SubscribeEvent
        public void handleRenderWorldLast(RenderWorldLastEvent event)
        {
            if (Minecraft.getMinecraft().theWorld == null)
                return;

            profiler().startSection("SpawnChecker");
            renderMarker(event.partialTicks);
            profiler().endSection();
        }
    }

}
