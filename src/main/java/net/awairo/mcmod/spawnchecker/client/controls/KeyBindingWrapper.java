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

import com.google.common.base.Objects;

import net.minecraft.client.settings.KeyBinding;

class KeyBindingWrapper extends KeyBinding
{
    private long lastPressTime = -1L;

    final KeyConfig keySettings;

    /**
     * Constructor.
     * 
     * @param keyDescription options.txt用の設定名
     * @param keyCode キーコード
     * @param keySettings キー設定
     */
    public KeyBindingWrapper(final String keyDescription, final int keyCode, final KeyConfig keySettings)
    {
        super(keyDescription, keyCode, KeyConfig.CATEGORY);
        this.keySettings = keySettings;
    }

    /**
     * update repeat timer.
     */
    public void update(final long currentTime)
    {
        if (!getIsKeyPressed())
        {
            lastPressTime = -1L;
            return;
        }

        if (lastPressTime == -1L)
        {
            lastPressTime = currentTime + keySettings.repeatDelay();
            return;
        }

        final long newTime = currentTime;
        if (newTime - lastPressTime > keySettings.repeatRate())
        {
            lastPressTime = newTime;
            onTick(getKeyCode());
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("keyCode", getKeyCode())
                .add("pressed", getIsKeyPressed())
                .add("keyDescription", getKeyDescription())
                .add("lastPressTime", lastPressTime)
                .toString();
    }
}
