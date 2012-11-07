package spawnchecker.utils;

import static spawnchecker.constants.Constants.SAVE_DURATION;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import spawnchecker.SpawnChecker;

/**
 * onTickInGame 中の処理タイミングを管理するためのユーティリティ.
 */
public class SpawnCheckerTimingHelper
{
    private static final Minecraft game = ModLoader.getMinecraftInstance();

    // mod の tick 開始時のナノ秒
    public static long nanoSeconds = 0L;
    // 前回の mod の tick から 1ms 以上時間が経過したか判定するためのキャッシュ
    public static long milliSeconds = 0L;
    // ミリ秒が変化したかのフラグ
    public static boolean milliSecondsChanged = false;
    // 前回の mod の tick から 1s 以上経過したか判定するためのキャッシュ
    public static long seconds = 0L;
    // 描画変化したかのフラグ
    public static boolean secChanged = false;
    // 最後にスポーン判定などのチェック処理をした時間
    public static long lastCheckTime = 0L;
    // 設定が変更された場合次のフレームでチェックを行うためのフラグ
    public static boolean modeSettingsChanged = false;

    public static void update()
    {
        game.mcProfiler.startSection("timingHelper");
        nanoSeconds = System.nanoTime();
        long ms = nanoSeconds / 1000000L; // nanoSec to milliSec
        milliSecondsChanged = milliSeconds != ms;

        if (milliSecondsChanged)
        {
            milliSeconds = ms;
            long sec = ms / 1000L;
            secChanged = seconds != sec;

            if (secChanged)
            {
                seconds = sec;
            }
        }

        game.mcProfiler.endSection();
    }

    public static boolean isSaveTiming()
    {
        // SpawnChecker.getSettings().mod.trace(milliSecondsChanged, " isSaveTiming ", nanoSeconds, " / ",
        // (nanoSeconds / 1000000000L), " / ", (nanoSeconds / 1000000000L) % SAVE_DURATION);
        return secChanged && (seconds % SAVE_DURATION) == 0L;
    }

    public static boolean isCheckTiming()
    {
        boolean ret = false;

        if (modeSettingsChanged)
        {
            modeSettingsChanged = false;
            ret = true;
        }
        else
        {
            if (milliSecondsChanged)
            {
                ret = (nanoSeconds - lastCheckTime) > SpawnChecker.getSettings().getCheckMinDuration();
            }
        }

        if (ret)
        {
            lastCheckTime = nanoSeconds;
        }

        return ret;
    }

    private SpawnCheckerTimingHelper()
    {
    }
}
