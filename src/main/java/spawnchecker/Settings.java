package spawnchecker;

import static net.minecraft.src.mod_SpawnChecker.brightness;
import static net.minecraft.src.mod_SpawnChecker.checkMinDuration;
import static net.minecraft.src.mod_SpawnChecker.duplicationAreaAlpha;
import static net.minecraft.src.mod_SpawnChecker.duplicationAreaBlue;
import static net.minecraft.src.mod_SpawnChecker.duplicationAreaGreen;
import static net.minecraft.src.mod_SpawnChecker.duplicationAreaRed;
import static net.minecraft.src.mod_SpawnChecker.enabledSpawnerVisualizerRightClick;
import static net.minecraft.src.mod_SpawnChecker.enablingItemsCSV;
import static net.minecraft.src.mod_SpawnChecker.entityOffsetX;
import static net.minecraft.src.mod_SpawnChecker.entityOffsetY;
import static net.minecraft.src.mod_SpawnChecker.entityOffsetZ;
import static net.minecraft.src.mod_SpawnChecker.infoBlue;
import static net.minecraft.src.mod_SpawnChecker.infoGreen;
import static net.minecraft.src.mod_SpawnChecker.infoHOffset;
import static net.minecraft.src.mod_SpawnChecker.infoRed;
import static net.minecraft.src.mod_SpawnChecker.infoVOffset;
import static net.minecraft.src.mod_SpawnChecker.informationTimeout;
import static net.minecraft.src.mod_SpawnChecker.marker1Alpha;
import static net.minecraft.src.mod_SpawnChecker.marker1Blue;
import static net.minecraft.src.mod_SpawnChecker.marker1Green;
import static net.minecraft.src.mod_SpawnChecker.marker1Red;
import static net.minecraft.src.mod_SpawnChecker.marker2Alpha;
import static net.minecraft.src.mod_SpawnChecker.marker2Blue;
import static net.minecraft.src.mod_SpawnChecker.marker2Green;
import static net.minecraft.src.mod_SpawnChecker.marker2Red;
import static net.minecraft.src.mod_SpawnChecker.marker3Alpha;
import static net.minecraft.src.mod_SpawnChecker.marker3Blue;
import static net.minecraft.src.mod_SpawnChecker.marker3Green;
import static net.minecraft.src.mod_SpawnChecker.marker3Red;
import static net.minecraft.src.mod_SpawnChecker.marker4Alpha;
import static net.minecraft.src.mod_SpawnChecker.marker4Blue;
import static net.minecraft.src.mod_SpawnChecker.marker4Green;
import static net.minecraft.src.mod_SpawnChecker.marker4Red;
import static net.minecraft.src.mod_SpawnChecker.marker5Alpha;
import static net.minecraft.src.mod_SpawnChecker.marker5Blue;
import static net.minecraft.src.mod_SpawnChecker.marker5Green;
import static net.minecraft.src.mod_SpawnChecker.marker5Red;
import static net.minecraft.src.mod_SpawnChecker.modeValue;
import static net.minecraft.src.mod_SpawnChecker.optionSC;
import static net.minecraft.src.mod_SpawnChecker.optionSF;
import static net.minecraft.src.mod_SpawnChecker.optionSV;
import static net.minecraft.src.mod_SpawnChecker.optionSetSC;
import static net.minecraft.src.mod_SpawnChecker.optionSetSF;
import static net.minecraft.src.mod_SpawnChecker.optionSetSV;
import static net.minecraft.src.mod_SpawnChecker.rangeHorizontal;
import static net.minecraft.src.mod_SpawnChecker.rangeVertical;
import static net.minecraft.src.mod_SpawnChecker.slimeChunkAlpha;
import static net.minecraft.src.mod_SpawnChecker.slimeChunkBlue;
import static net.minecraft.src.mod_SpawnChecker.slimeChunkGreen;
import static net.minecraft.src.mod_SpawnChecker.slimeChunkRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaLineAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaLineBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaLineGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaLineRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerActivateAreaRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnAreaAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnAreaBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnAreaGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnAreaRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnablePointAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnablePointBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnablePointGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerSpawnablePointRed;
import static net.minecraft.src.mod_SpawnChecker.spawnerUnspawnablePointAlpha;
import static net.minecraft.src.mod_SpawnChecker.spawnerUnspawnablePointBlue;
import static net.minecraft.src.mod_SpawnChecker.spawnerUnspawnablePointGreen;
import static net.minecraft.src.mod_SpawnChecker.spawnerUnspawnablePointRed;
import static spawnchecker.constants.Constants.MAX_BRIGHTNESS;
import static spawnchecker.constants.Constants.MAX_RANGE_HORIZONTAL;
import static spawnchecker.constants.Constants.MAX_RANGE_VERTICAL;
import static spawnchecker.constants.Constants.MIN_BRIGHTNESS;
import static spawnchecker.constants.Constants.MIN_RANGE_HORIZONTAL;
import static spawnchecker.constants.Constants.MIN_RANGE_VERTICAL;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import spawnchecker.constants.Resources;
import spawnchecker.enums.Dimension;
import spawnchecker.enums.Mode;
import spawnchecker.enums.SpawnableEntity;
import spawnchecker.markers.MarkerBase;
import spawnchecker.markers.SpawnPointMarker;
import spawnchecker.markers.SpawnerMarker;
import spawnchecker.utils.EnablingItemsHelper;
import spawnchecker.utils.MPWorldSeedHelper;

/**
 * SpawnChecker の設定.
 * 
 * @author takuru/ale
 */
public class Settings
{
    public final KeyBinding modeUpKey;
    public final KeyBinding modeDownKey;
    public final KeyBinding plusKey;
    public final KeyBinding minusKey;

    private Mode mode = null;
    private Long worldSeed = null;
    private World currentWorld = null;
    private Dimension dimension = null;
    private long checkMinDurationNanoSec;
    private long informationTimeoutNanoSec;

    private int[] enablingItemIds;

    private Color endermanMarkerColor;
    private Color skeletonMarkerColor;
    private Color spiderMarkerColor;
    private Color slimeMarkerColor;
    private Color ghastMarkerColor;
    private Color colorSlimeChunk;
    private Color colorSpawnerBorder;
    private Color colorSpawnerSpawnArea;
    private Color colorSpawnerDuplicationArea;
    private Color colorSpawnerSpawnablePoint;
    private Color colorSpawnerUnspawnablePoint;
    private Color colorSpawnerActivateArea;
    private Color colorSpawnerActivateAreaLine;
    private Color colorInformation;

    public volatile boolean settingChanged = false;

    private List<SpawnPointMarker> renderDataForSpawnPointList = Collections.emptyList();
    private List<MarkerBase> renderDataForChunkMarkerList = Collections.emptyList();
    private SpawnerMarker renderDataForSpawnerMarker;

    /** Constructor. */
    Settings()
    {
        modeUpKey = new KeyBinding(Resources.KEY_UP, Keyboard.KEY_UP);
        modeDownKey = new KeyBinding(Resources.KEY_DOWN, Keyboard.KEY_DOWN);
        plusKey = new KeyBinding(Resources.KEY_PLUS, Keyboard.KEY_ADD);
        minusKey = new KeyBinding(Resources.KEY_MINUS, Keyboard.KEY_SUBTRACT);
    }

    /** 設定の初期化. */
    public void initialize()
    {
        Mode.initializeModes(this);
        resetRenderDatas();
        checkMinDurationNanoSec = (checkMinDuration) * 1000L * 1000L;
        SpawnChecker.mod.info("check duration=", checkMinDuration, "ms (", checkMinDurationNanoSec, " nano sec)");
        informationTimeoutNanoSec = (informationTimeout) * 1000L * 1000L;
        // 色設定
        endermanMarkerColor = new Color(marker1Red, marker1Green, marker1Blue, marker1Alpha);
        skeletonMarkerColor = new Color(marker2Red, marker2Green, marker2Blue, marker2Alpha);
        spiderMarkerColor = new Color(marker3Red, marker3Green, marker3Blue, marker3Alpha);
        slimeMarkerColor = new Color(marker4Red, marker4Green, marker4Blue, marker4Alpha);
        ghastMarkerColor = new Color(marker5Red, marker5Green, marker5Blue, marker5Alpha);
        colorSlimeChunk = new Color(slimeChunkRed, slimeChunkGreen, slimeChunkBlue, slimeChunkAlpha);
        colorSpawnerBorder = new Color(spawnerRed, spawnerGreen, spawnerBlue, spawnerAlpha);
        colorSpawnerSpawnArea = new Color(
                spawnerSpawnAreaRed, spawnerSpawnAreaGreen, spawnerSpawnAreaBlue, spawnerSpawnAreaAlpha);
        colorSpawnerDuplicationArea = new Color(
                duplicationAreaRed, duplicationAreaGreen, duplicationAreaBlue, duplicationAreaAlpha);
        colorSpawnerSpawnablePoint = new Color(
                spawnerSpawnablePointRed, spawnerSpawnablePointGreen, spawnerSpawnablePointBlue,
                spawnerSpawnablePointAlpha);
        colorSpawnerUnspawnablePoint = new Color(spawnerUnspawnablePointRed, spawnerUnspawnablePointGreen,
                spawnerUnspawnablePointBlue, spawnerUnspawnablePointAlpha);
        colorSpawnerActivateArea = new Color(
                spawnerActivateAreaRed, spawnerActivateAreaGreen, spawnerActivateAreaBlue, spawnerActivateAreaAlpha);
        colorSpawnerActivateAreaLine = new Color(
                spawnerActivateAreaLineRed, spawnerActivateAreaLineGreen, spawnerActivateAreaLineBlue,
                spawnerActivateAreaLineAlpha);
        colorInformation = new Color(infoRed, infoGreen, infoBlue);
        EnablingItemsHelper.loadPromotedItems(enablingItemsCSV);
        MPWorldSeedHelper.loadServersSeedSetting();
    }

    /*
     * ワールド、ディメンション.
     * ---------------------------------------
     */

    public World getCurrentWorld()
    {
        return currentWorld;
    }

    public void setWorld(World newWorld)
    {
        if (currentWorld == newWorld)
        {
            return;
        }

        currentWorld = newWorld;
    }

    public Long getWorldSeed()
    {
        return worldSeed;
    }

    public void setWorldSeed(Long worldSeed)
    {
        this.worldSeed = worldSeed;
    }

    public Dimension getDimension()
    {
        return dimension;
    }

    public void setDimension(Dimension newDimention)
    {
        this.dimension = newDimention;
    }

    /*
     * モードとモードオプション
     * ---------------------------------------
     */

    public Mode getCurrentMode()
    {
        if (mode != null)
        {
            switch (getDimension())
            {
                case NETHER:
                case THE_END:
                    if (mode.equals(Mode.SLIME_CHUNK_FINDER))
                    {
                        return Mode.SPAWABLE_POINT_CHECKER;
                    }

                    break;

                case SURFACE:
                case UNKNOWN:
                    break;

                default:
                    throw new InternalError("unexpected dimension type:" + getDimension());
            }

            return mode;
        }

        if (modeValue != null)
        {
            if (modeValue.equals(Mode.SPAWABLE_POINT_CHECKER.toString()))
            {
                mode = Mode.SPAWABLE_POINT_CHECKER;
                return mode;
            }

            if (modeValue.equals(Mode.SLIME_CHUNK_FINDER.toString()))
            {
                mode = Mode.SLIME_CHUNK_FINDER;
                return mode;
            }

            if (modeValue.equals(Mode.SPAWNER_VISUALIZER.toString()))
            {
                mode = Mode.SPAWNER_VISUALIZER;
                return mode;
            }
        }

        SpawnChecker.mod.warn("Invalid mode value. (mode value=", modeValue, ")");
        SpawnChecker.mod.info("Set to spawnable point checker mode.");
        mode = Mode.SPAWABLE_POINT_CHECKER;
        modeValue = mode.toString();
        settingChanged = true;
        return mode;
    }

    public void setCurrentMode(Mode newMode)
    {
        assert newMode != null;

        if (mode == newMode)
        {
            return;
        }

        mode = newMode;
        modeValue = mode.toString();
        settingChanged = true;
    }

    public int getOptionSC()
    {
        return optionSC;
    }

    public String getOptionSetSC()
    {
        return optionSetSC;
    }

    public void setOptionSC(int value)
    {
        if (optionSC == value)
        {
            return;
        }

        optionSC = value;
        settingChanged = true;
    }

    public int getOptionSF()
    {
        return optionSF;
    }

    public String getOptionSetSF()
    {
        return optionSetSF;
    }

    public void setOptionSF(int value)
    {
        if (optionSF == value)
        {
            return;
        }

        optionSF = value;
        settingChanged = true;
    }

    public int getOptionSV()
    {
        return optionSV;
    }

    public String getOptionSetSV()
    {
        return optionSetSV;
    }

    public void setOptionSV(int value)
    {
        if (optionSV == value)
        {
            return;
        }

        optionSV = value;
        settingChanged = true;
    }

    /*
     * 明るさとチェック範囲.
     * ---------------------------------------
     */

    public int getRangeHorizontal()
    {
        return rangeHorizontal;
    }

    public boolean incrementRangeHorizontal()
    {
        if (rangeHorizontal >= MAX_RANGE_HORIZONTAL)
        {
            return false;
        }

        rangeHorizontal++;
        settingChanged = true;
        return true;
    }

    public boolean decrementRangeHorizontal()
    {
        if (rangeHorizontal <= MIN_RANGE_HORIZONTAL)
        {
            return false;
        }

        rangeHorizontal--;
        settingChanged = true;
        return true;
    }

    public int getRangeVertical()
    {
        return rangeVertical;
    }

    public boolean incrementRangeVertical()
    {
        if (rangeVertical >= MAX_RANGE_VERTICAL)
        {
            return false;
        }

        rangeVertical++;
        settingChanged = true;
        return true;
    }

    public boolean decrementRangeVertical()
    {
        if (rangeVertical <= MIN_RANGE_VERTICAL)
        {
            return false;
        }

        rangeVertical--;
        settingChanged = true;
        return true;
    }

    public int getBrightness()
    {
        return brightness;
    }

    public boolean incrementBrightness()
    {
        if (brightness >= MAX_BRIGHTNESS)
        {
            return false;
        }

        brightness++;
        settingChanged = true;
        return true;
    }

    public boolean decrementBrightness()
    {
        if (brightness <= MIN_BRIGHTNESS)
        {
            return false;
        }

        brightness--;
        settingChanged = true;
        return true;
    }

    /*
     * 情報メッセージ.
     * ---------------------------------------
     */

    public int getInfoVOffset()
    {
        return infoVOffset;
    }

    public int getInfoHOffset()
    {
        return infoHOffset;
    }

    public long getInformationTimeout()
    {
        return informationTimeoutNanoSec;
    }

    public int getInformationColorRGB()
    {
        return colorInformation.getRGB();
    }

    /*
     * スポーナー可視化表示切替での、右クリック使用可否.
     * ---------------------------------------
     */

    public boolean isEabledSpawnerVisualizerRightClick()
    {
        return enabledSpawnerVisualizerRightClick;
    }

    /*
     * マーカー表示有効化アイテムのID配列.
     * ---------------------------------------
     */

    public int[] getEnablingItemIds()
    {
        return enablingItemIds;
    }

    public void setEnablingItemIds(int[] value)
    {
        enablingItemIds = value;
    }

    /*
     * entityOffset.
     * ---------------------------------------
     */

    public double getEntityOffsetX()
    {
        return entityOffsetX;
    }

    public double getEntityOffsetY()
    {
        return entityOffsetY;
    }

    public double getEntityOffsetZ()
    {
        return entityOffsetZ;
    }

    /*
     * Colors.
     * ---------------------------------------
     */

    public Color getColorByMob(SpawnableEntity mob)
    {
        switch (mob)
        {
            case ENDERMAN:
                return endermanMarkerColor;

            case SKELETON:
            case ZOMBIE:
            case CREEPER:
            case PIG_ZOMBIE:
                return skeletonMarkerColor;

            case SPIDER:
                return spiderMarkerColor;

            case SLIME:
            case MAGMA_CUBE:
                return slimeMarkerColor;

            case GHAST:
                return ghastMarkerColor;

            default:
                return null;
        }
    }

    public Color getColorSlimeChunk()
    {
        return colorSlimeChunk;
    }

    public Color getColorSpawnerBorder()
    {
        return colorSpawnerBorder;
    }

    public Color getColorSpawnerSpawnArea()
    {
        return colorSpawnerSpawnArea;
    }

    public Color getColorSpawnerDuplicationArea()
    {
        return colorSpawnerDuplicationArea;
    }

    public Color getColorSpawnerSpawnablePoint()
    {
        return colorSpawnerSpawnablePoint;
    }

    public Color getColorSpawnerUnspawnablePoint()
    {
        return colorSpawnerUnspawnablePoint;
    }

    public Color getColorSpawnerActivateArea()
    {
        return colorSpawnerActivateArea;
    }

    public Color getColorSpawnerActivateAreaLine()
    {
        return colorSpawnerActivateAreaLine;
    }

    /*
     * チェック間隔
     * ---------------------------------------
     */

    public long getCheckMinDuration()
    {
        return checkMinDurationNanoSec;
    }

    /*
     * レンダリング用データ.
     * ---------------------------------------
     */

    public void resetRenderDatas()
    {
        renderDataForSpawnPointList.clear();
        renderDataForChunkMarkerList.clear();
        renderDataForSpawnerMarker = null;
    }

    /*
     * Spawn point marker.
     */

    public List<SpawnPointMarker> getRenderDataForSpawnPointList()
    {
        return renderDataForSpawnPointList;
    }

    public void setRenderDataForSpawnPointList(List<SpawnPointMarker> renderData)
    {
        renderDataForSpawnPointList = renderData;
    }

    /*
     * Chunk marker.
     */

    public List<MarkerBase> getRenderDataForChunkMarkerList()
    {
        return renderDataForChunkMarkerList;
    }

    public void setRenderDataForChunkMarkerList(List<MarkerBase> rendarData)
    {
        renderDataForChunkMarkerList = rendarData;
    }

    /*
     * Spawner marker.
     */

    public SpawnerMarker getRenderDataForSpawnerMarker()
    {
        return renderDataForSpawnerMarker;
    }

    public void setRenderDataForSpawnerMarker(SpawnerMarker renderData)
    {
        renderDataForSpawnerMarker = renderData;
    }
}