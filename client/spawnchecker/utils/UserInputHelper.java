package spawnchecker.utils;

import static org.lwjgl.input.Keyboard.isKeyDown;
import static spawnchecker.constants.Constants.REPEAT_DURATION;
import static spawnchecker.constants.Constants.REPEAT_START_DURATION;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import spawnchecker.Settings;
import spawnchecker.SpawnChecker;

public class UserInputHelper
{
    private static final Map<KeyBinding, Long> lastKeyDownEventTimerMap = new HashMap<KeyBinding, Long>(6);
    private static boolean isClicking = false;
    private static int lastTargetedX = Integer.MIN_VALUE;
    private static int lastTargetedY = Integer.MIN_VALUE;
    private static int lastTargetedZ = Integer.MIN_VALUE;

    public static void initialize()
    {
        Settings settings = SpawnChecker.getSettings();
        lastKeyDownEventTimerMap.put(settings.modeUpKey, 0L);
        lastKeyDownEventTimerMap.put(settings.modeDownKey, 0L);
        lastKeyDownEventTimerMap.put(settings.plusKey, 0L);
        lastKeyDownEventTimerMap.put(settings.minusKey, 0L);
    }

    /**
     * 有効なキーダウンか判定.
     * <p>
     * modLoaderによるキーダウンのリピート間隔はワールド時間の変更を基準に発生するが
     * 結構高頻度で、FPSが高くなると高速リピートして操作性がよくないため、適当に遅延を設けるための処理。
     * </p>
     *
     * @param key
     *            KeyBinding
     * @return キーダウンが有効なら true
     */
    public static boolean isEnabledKeyDownEvent(KeyBinding key)
    {
        // minecraft の tick 時は押されておらず、ModLoader の tick 時に押されていた場合 pressed が false
        // 以下で pressTime を見てるので、pressed が false のときに処理すると2重押しになるので除外する
        if (!key.pressed)
        {
            return false;
        }

        // 押されっぱなしの場合には pressTime は増えないので、一度0にリセットしてあげると、
        // 押されている間は初回の判定に入らなくなる。
        // ModLoaderはキーアップ時の通知がないため、一度離したかどうかが判定できない
        // しかし、pressTime は minecraft の tick 毎に、キーを押しなおした回数だけ増えるので
        // この数が前回の数以上になっていれば、離したかどうかの判定ができる
        // 毎度、前回の値を覚えておく意味はないので、0リセットして0より大きいかで判定している
        if (key.pressTime > 0)
        {
            // 初回
            key.pressTime = 0;
            SpawnChecker.mod.trace("key down timer reset = ", key.keyCode, " ",
                    SpawnCheckerTimingHelper.nanoSeconds + REPEAT_START_DURATION);
            // 初回はリピートタイマーを未来に設定してリピート開始を遅らせる
            lastKeyDownEventTimerMap.put(key, SpawnCheckerTimingHelper.nanoSeconds + REPEAT_START_DURATION);
            return true;
        }

        long duration = SpawnCheckerTimingHelper.nanoSeconds - lastKeyDownEventTimerMap.get(key);

        // 最後に押してから一定時間たってなければfalse
        if (duration <= REPEAT_DURATION)
        {
            return false;
        }

        // タイマーを更新
        SpawnChecker.mod.trace("key down timer reset = ", key.keyCode, " ", SpawnCheckerTimingHelper.nanoSeconds);
        lastKeyDownEventTimerMap.put(key, SpawnCheckerTimingHelper.nanoSeconds);
        return true;
    }

    public static boolean isShiftKeyDown()
    {
        boolean b = isKeyDown(Keyboard.KEY_LSHIFT) || isKeyDown(Keyboard.KEY_RSHIFT);

        if (b)
        {
            SpawnChecker.mod.trace("shift key is downed.");
        }

        return b;
    }

    public static boolean isControlKeyDown()
    {
        boolean b = isKeyDown(Keyboard.KEY_LCONTROL) || isKeyDown(Keyboard.KEY_RCONTROL);

        if (b)
        {
            SpawnChecker.mod.trace("ctrl key is downed.");
        }

        return b;
    }

    public static boolean checkClickForSpawner()
    {
        if (!SpawnCheckerTimingHelper.milliSecondsChanged)
        {
            // ミリ秒経過してない;
            return false;
        }

        Minecraft game = ModLoader.getMinecraftInstance();
        MovingObjectPosition mop = game.objectMouseOver;

        if (isClicked(game) && mop != null && mop.typeOfHit == EnumMovingObjectType.TILE)
        {
            // クリエイティブじゃない場合、右クリ時は素手のみ許可
            if (game.playerController.isNotCreative())
            {
                if (game.gameSettings.keyBindUseItem.pressed
                        && game.thePlayer.inventory.getCurrentItem() != null)
                {
                    return false;
                }
            }

            if (isClicking)
            {
                return false;
            }

            isClicking = true;

            if (isTargetChanged(mop) || !isSpawner(game, mop))
            {
                return false;
            }

            return true;
        }

        updateLastTarget(mop);
        isClicking = false;
        return false;
    }

    private static boolean isClicked(Minecraft game)
    {
        if (game.gameSettings.keyBindAttack.pressed)
        {
            return true;
        }

        if (SpawnChecker.getSettings().isEabledSpawnerVisualizerRightClick()
                && game.gameSettings.keyBindUseItem.pressed)
        {
            return true;
        }

        return false;
    }

    private static boolean isTargetChanged(MovingObjectPosition mop)
    {
        // ブロックを破壊した直後の左クリックは、一度クリックをやめるまで無視貯めのチェック
        boolean b = lastTargetedX != mop.blockX || lastTargetedY != mop.blockY || lastTargetedZ != mop.blockZ;
        updateLastTarget(mop);
        return b;
    }

    private static void updateLastTarget(MovingObjectPosition mop)
    {
        if (mop != null)
        {
            lastTargetedX = mop.blockX;
            lastTargetedY = mop.blockY;
            lastTargetedZ = mop.blockZ;
        }
        else
        {
            lastTargetedX = Integer.MIN_VALUE;
            lastTargetedY = Integer.MIN_VALUE;
            lastTargetedZ = Integer.MIN_VALUE;
        }
    }

    private static boolean isSpawner(Minecraft game, MovingObjectPosition mop)
    {
        return game.theWorld.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == Block.mobSpawner.blockID;
    }

    private UserInputHelper()
    {
    }
}
