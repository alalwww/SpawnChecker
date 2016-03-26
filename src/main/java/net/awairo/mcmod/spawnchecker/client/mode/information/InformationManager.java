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

package net.awairo.mcmod.spawnchecker.client.mode.information;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import net.awairo.mcmod.spawnchecker.client.ClientManager;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * InformationManager.
 * 
 * @author alalwww
 */
public final class InformationManager extends ClientManager
{
    private final List<Mode.Information> cache = Lists.newArrayList();
    private final Renderer renderer = new Renderer();
    private Mode.Information[] infos;

    private static InformationManager instance = new InformationManager();

    private long timeout;

    private boolean playFadeout = false;
    private long fadeout;

    /**
     * @return このクラスのシングルトンなインスタンス
     */
    public static InformationManager instance()
    {
        return instance;
    }

    /** クラスロード用. */
    public static void load()
    {
    }

    /**
     * Constructor.
     */
    private InformationManager()
    {
    }

    /**
     * @param info 追加するモード情報
     */
    public void add(Mode.Information info)
    {
        cache.add(info);
    }

    /**
     * 現在描画しているモード情報をクリア.
     */
    public void clear()
    {
        cache.clear();
    }

    /**
     * @param infos 新たに設定するモード情報
     */
    public void set(Mode.Information... infos)
    {
        clear();
        for (Mode.Information info : infos)
            add(info);
    }

    private void drawInformationIfPresent()
    {
        final long now = Minecraft.getSystemTime();

        if (GAME.currentScreen != null || GAME.theWorld == null)
            infos = null;

        if (!cache.isEmpty())
        {
            infos = cache.toArray(new Mode.Information[cache.size()]);
            cache.clear();
            timeout = now + settings().common().informationDuration.getInt();
        }

        if (isAbsentInformation()) return;

        if (isTimeout(now))
        {
            fadeout(now);

            if (isAbsentInformation()) return;
        }

        final ScaledResolution resolution = newScaledResolution();
        int posX = settings().common().informationOffsetX.getInt();
        int posY = computePosY(resolution, settings().common().informationOffsetY.getInt());

        for (Mode.Information info : infos)
        {
            renderer.draw(posX + info.offsetX(), posY + info.offsetY(), info);
            posY += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT; // TODO: standardGalacticFontRendererかも
        }

        renderer.endDrawing();
    }

    private boolean isTimeout(long now)
    {
        return now > timeout;
    }

    private boolean isAbsentInformation()
    {
        return infos == null || infos.length == 0;
    }

    private void fadeout(long now)
    {
        final int informationFadeout = settings().common().informationFadeout.getInt();

        if (!playFadeout)
        {
            playFadeout = true;
            fadeout = now + informationFadeout;
        }

        if (now > fadeout)
        {
            playFadeout = false;
            infos = null;
            return;
        }

        final float dividend = Ints.saturatedCast(fadeout - now);
        final float divisor = informationFadeout;
        renderer.setAlpha(dividend / divisor);
    }

    private int computePosY(ScaledResolution resolution, int offsetY)
    {
        final int halfHeight = resolution.getScaledHeight() / 2;
        return halfHeight + ((halfHeight * offsetY) / 100);
    }

    private ScaledResolution newScaledResolution()
    {
        return new ScaledResolution(GAME);
    }

    // -------------------------

    @Override
    protected Object newFmlEventListener()
    {
        return new Listener(this);
    }

    /**
     * イベントリスナ.
     * 
     * @author alalwww
     */
    public static final class Listener
    {
        private final InformationManager manager;

        private Listener(InformationManager manager)
        {
            this.manager = manager;
        }

        /**
         * RenderTickEventを処理.
         * 
         * @param event RenderTickEvent
         */
        @SubscribeEvent
        public void onRenderTick(RenderTickEvent event)
        {
            if (event.phase != Phase.END) return;

            manager.drawInformationIfPresent();

        }
    }
}
