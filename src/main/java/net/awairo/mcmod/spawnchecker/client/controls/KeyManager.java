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

    @SuppressWarnings("unused")
    private static final KeyManager instance = new KeyManager();

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

    @Override
    protected Object newFmlEventListener()
    {
        return new Listener();
    }

    /**
     * イベントリスナー.
     * 
     * @author alalwww
     */
    public class Listener
    {
        private Listener()
        {
        }

        @SubscribeEvent
        public void onTick(ClientTickEvent event)
        {
            final long currentTime = Minecraft.getSystemTime();

            final boolean ctrl = isKeysDown(ctrlKeys);
            final boolean shift = isKeysDown(shiftKeys);
            final boolean alt = isKeysDown(altKeys);

            for (AbstractKeyHandler handler : AbstractKeyHandler.handlerList)
            {
                handler.key.update(currentTime);
                while (handler.key.isPressed())
                {
                    LOGGER.trace("code:{}, ctrl:{}, shift:{}, alt:{}", handler.key.getKeyCode(), ctrl, shift, alt);
                    handler.onKeyPress(settings().state(), ctrl, shift, alt);
                }
            }
        }
    }

    /**
     * 指定キーコードのどれか1つでも押下されていればtrueを返します.
     * 
     * @param keys キー
     * @return true は押下されている
     */
    private boolean isKeysDown(int... keys)
    {
        for (int key : keys)
            if (Keyboard.isKeyDown(key))
                return true;

        return false;
    }
}
