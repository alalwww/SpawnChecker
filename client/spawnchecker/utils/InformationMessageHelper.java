package spawnchecker.utils;

import static spawnchecker.constants.Constants.FONT_HEIGHT;

import java.awt.Point;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ScaledResolution;
import spawnchecker.Settings;
import spawnchecker.SpawnChecker;
import spawnchecker.constants.Resources;
import spawnchecker.enums.Mode;

/**
 * 情報メッセージの描画処理など.
 *
 * @author takuru/ale
 */
public class InformationMessageHelper
{
    private static final Point informationPoint = new Point();
    private static final Minecraft game = ModLoader.getMinecraftInstance();
    private static String[] informations;

    private static long informationTimer;

    public static void setInformationsByModeDescription(Mode mode)
    {
        SpawnChecker.mod.trace("setInformationsByModeDescription");
        assert mode != null;
        List<String> list = mode.getDescriptionList();
        informations = list.toArray(new String[list.size()]);
        resetInformationPosition();
    }

    public static void setInformationsByBrightness()
    {
        Settings settings = SpawnChecker.getSettings();
        SpawnChecker.mod.trace("setInformationsByBrightness");
        informations = new String[]
        {
            Resources.BRIGHT_COMMON,
            Resources.BRIGHT_MESSAGE + (settings.getBrightness() > 0
                    ? "+" + settings.getBrightness() : settings.getBrightness())
        };
        resetInformationPosition();
    }

    public static void setInformationsByRange()
    {
        Settings settings = SpawnChecker.getSettings();
        SpawnChecker.mod.trace("setInformationsByRange");
        informations = new String[]
        {
            Resources.RANGE_COMMON,
            Resources.RANGE_VERTICAL + settings.getRangeVertical(),
            Resources.RANGE_HORIZONTAL + settings.getRangeHorizontal()
        };
        resetInformationPosition();
    }

    private static void resetInformationPosition()
    {
        Settings settings = SpawnChecker.getSettings();
        SpawnChecker.mod.trace("resetInformationPosition");
        Minecraft m = ModLoader.getMinecraftInstance();
        ScaledResolution sr = new ScaledResolution(m.gameSettings, m.displayWidth, m.displayHeight);
        int x = 5 + settings.getInfoHOffset(); // 5 is default x offset
        int y = sr.getScaledHeight() / 2;
        y += y * settings.getInfoVOffset() / 100;
        informationPoint.setLocation(x, y);
        informationTimer = SpawnCheckerTimingHelper.nanoSeconds;
    }

    public static void drawInformations()
    {
        if (informations == null)
        {
            return;
        }

        game.mcProfiler.startSection("drawInformations");
        // settings.mod.trace("draw information. line=", informations.length);
        int x = informationPoint.x;
        int y = informationPoint.y;
        Settings settings = SpawnChecker.getSettings();
        int color = settings.getInformationColorRGB();

        for (int i = 0; i < informations.length; y += FONT_HEIGHT, i++)
        {
            game.fontRenderer.drawStringWithShadow(informations[i], x, y, color);
        }

        long duration = SpawnCheckerTimingHelper.nanoSeconds - informationTimer;

        if (SpawnCheckerTimingHelper.milliSecondsChanged && duration > settings.getInformationTimeout())
        {
            SpawnChecker.mod.trace("timeout: clock=", SpawnCheckerTimingHelper.nanoSeconds, " duration=", duration);
            informations = null;
        }

        game.mcProfiler.endSection();
    }

    private InformationMessageHelper()
    {
    }
}
