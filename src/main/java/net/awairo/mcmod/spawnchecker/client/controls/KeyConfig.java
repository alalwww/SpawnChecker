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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.client.settings.KeyBinding;

import net.awairo.mcmod.common.v1.util.config.Config;
import net.awairo.mcmod.common.v1.util.config.ConfigCategory;
import net.awairo.mcmod.common.v1.util.config.Prop;

/**
 * KeySettings.
 * 
 * @author alalwww
 */
public final class KeyConfig extends ConfigCategory
{
    public static final String CATEGORY = "spawnchecker.key.category";

    private static final String UP_KEY_DESC = "spawnchecker.key.up";
    private static final String DOWN_KEY_DESC = "spawnchecker.key.down";
    private static final String PLUS_KEY_DESC = "spawnchecker.key.plus";
    private static final String MINUS_KEY_DESC = "spawnchecker.key.minus";

    private final ImmutableList<AbstractKeyHandler> handlers;

    /** リピート開始までの待ち時間. */
    final Prop repeatDelay;

    /** リピート間隔. */
    final Prop repeatRate;

    /**
     * Constructor.
     * 
     * @param config 設定
     * @param state キー操作で変更する状態
     */
    public KeyConfig(Config config)
    {
        super(config);
        config.addCategoryComment(
                "Key control settings.\n"
                        + "Please go over options controls screen to register the control keys.");

        repeatDelay = config.getValueOf("repeat_delay", 500)
                .comment("key repeat delay of milliseconds.\n(min: 1, default: 500)");
        if (repeatDelay.getInt() < 1) repeatDelay.set(500);

        repeatRate = config.getValueOf("key_repeat_rate", 100)
                .comment("key repeat rate of milliseconds.\n(min: 1, default: 100)");
        if (repeatRate.getInt() < 1) repeatRate.set(500);

        handlers = ImmutableList.of(
                new UpKeyHandler(new KeyBindingWrapper(UP_KEY_DESC, Keyboard.KEY_UP, this)),
                new DownKeyHandler(new KeyBindingWrapper(DOWN_KEY_DESC, Keyboard.KEY_DOWN, this)),
                new PlusKeyHandler(new KeyBindingWrapper(PLUS_KEY_DESC, Keyboard.KEY_ADD, this)),
                new MinusKeyHandler(new KeyBindingWrapper(MINUS_KEY_DESC, Keyboard.KEY_SUBTRACT, this)));
    }

    @Override
    protected String configurationCategory()
    {
        return "controls";
    }

    public ImmutableList<KeyBinding> keys()
    {
        return ImmutableList.copyOf(Lists.transform(handlers, new Function<AbstractKeyHandler, KeyBinding>()
        {
            @Override
            public KeyBinding apply(AbstractKeyHandler input)
            {
                return input.key;
            }
        }));
    }

    long repeatDelay()
    {
        return repeatDelay.getInt();
    }

    long repeatRate()
    {
        return repeatRate.getInt();
    }
}
