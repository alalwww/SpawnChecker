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

package net.awairo.mcmod.spawnchecker.client.controls;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;

import net.awairo.mcmod.spawnchecker.client.ClientManager;
import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeManager;

/**
 * Tick毎にキー状態を処理するクラス.
 * 
 * @author alalwww
 */
public final class KeyManager extends ClientManager
{
    private final int[] ctrlKeys;
    private final int[] shiftKeys;
    private final int[] altKeys;

    static final KeyManager instance = new KeyManager();

    /** クラスロード用. */
    public static void load()
    {
    }

    /**
     * Constructor.
     */
    private KeyManager()
    {
        ctrlKeys = ConstantsConfig.instance().ctrlKeyCodes.clone();
        shiftKeys = ConstantsConfig.instance().shiftKeyCodes.clone();
        altKeys = ConstantsConfig.instance().altKeyCodes.clone();
    }

    void onUpKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (ctrl)
            modeManager().changeModeUp();
        else
            modeManager().onUpKeyPress(shift, alt);
    }

    void onDownKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        if (ctrl)
            modeManager().changeModeDown();
        else
            modeManager().onDownKeyPress(shift, alt);
    }

    void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        modeManager().onPlusKeyPress(shift, shift, alt);
    }

    void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        modeManager().onMinusKeyPress(shift, shift, alt);
    }

    private ModeManager modeManager()
    {
        return get(ModeManager.class);
    }

    private static boolean isKeysDown(int... keys)
    {
        for (int key : keys)
            if (Keyboard.isKeyDown(key))
                return true;

        return false;
    }

    // ------------------------------------------------------------------------------------

    @Override
    protected Object newFmlEventListener()
    {
        return new Listener(this);
    }

    /**
     * イベントリスナー.
     * 
     * @author alalwww
     */
    public static class Listener
    {
        private final KeyManager manager;

        private Listener(KeyManager manager)
        {
            this.manager = manager;
        }

        @SubscribeEvent
        public void onTick(ClientTickEvent event)
        {
            final long currentTime = Minecraft.getSystemTime();

            final boolean ctrl = isKeysDown(manager.ctrlKeys);
            final boolean shift = isKeysDown(manager.shiftKeys);
            final boolean alt = isKeysDown(manager.altKeys);

            for (AbstractKeyHandler handler : AbstractKeyHandler.handlerList)
            {
                handler.key.update(currentTime);
                while (handler.key.isPressed())
                {
                    LOGGER.trace("code:{}, ctrl:{}, shift:{}, alt:{}", handler.key.getKeyCode(), ctrl, shift, alt);
                    handler.onKeyPress(ctrl, shift, alt);
                }
            }
        }
    }
}
