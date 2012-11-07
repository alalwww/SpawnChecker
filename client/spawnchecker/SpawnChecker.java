package spawnchecker;

import static spawnchecker.utils.InformationMessageHelper.drawInformations;
import static spawnchecker.utils.InformationMessageHelper.setInformationsByBrightness;
import static spawnchecker.utils.InformationMessageHelper.setInformationsByModeDescription;
import static spawnchecker.utils.InformationMessageHelper.setInformationsByRange;
import static spawnchecker.utils.SpawnCheckerTimingHelper.isCheckTiming;
import static spawnchecker.utils.SpawnCheckerTimingHelper.isSaveTiming;
import static spawnchecker.utils.UserInputHelper.isControlKeyDown;
import static spawnchecker.utils.UserInputHelper.isEnabledKeyDownEvent;
import static spawnchecker.utils.UserInputHelper.isShiftKeyDown;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Render;
import net.minecraft.src.WorldServer;
import net.minecraft.src.mod_SpawnChecker;
import spawnchecker.constants.Resources;
import spawnchecker.enums.Dimension;
import spawnchecker.enums.Mode;
import spawnchecker.enums.Mode.Option;
import spawnchecker.markers.ChunkMarker;
import spawnchecker.markers.MarkerBase;
import spawnchecker.markers.SpawnPointMarker;
import spawnchecker.markers.SpawnerMarker;
import spawnchecker.spawnablecheckers.SlimeChunkSearcher;
import spawnchecker.spawnablecheckers.SpawnPointChecker;
import spawnchecker.spawnervisualizer.SpawnerVisualizer;
import spawnchecker.utils.EnablingItemsHelper;
import spawnchecker.utils.MPWorldSeedHelper;
import spawnchecker.utils.SpawnCheckerTimingHelper;
import spawnchecker.utils.UserInputHelper;

/**
 * SpawnChecker main logic.
 *
 * @author takuru/ale
 */
public class SpawnChecker
{
    private final boolean fml;
    private EntitySpawnChecker entity;
    private EntityPlayer currentPlayer;
    private Mode previousMode;

    /**
     * Constructor.
     *
     * @param mod
     *            mod_SpawnChecker
     */
    public SpawnChecker(mod_SpawnChecker mod, boolean fml)
    {
        SpawnChecker.mod = mod;
        settings = new Settings();
        SpawnPointChecker.settings = settings;
        this.fml = fml;
    }

    /**
     * 初期化.
     */
    public void initialize()
    {
        settings.initialize();
        ModLoader.addLocalization(Resources.KEY_UP, Resources.KEY_UP_EN);
        ModLoader.addLocalization(Resources.KEY_UP, Resources.JA, Resources.KEY_UP_JA);
        ModLoader.addLocalization(Resources.KEY_DOWN, Resources.KEY_DOWN_EN);
        ModLoader.addLocalization(Resources.KEY_DOWN, Resources.JA, Resources.KEY_DOWN_JA);
        ModLoader.addLocalization(Resources.KEY_PLUS, Resources.KEY_PLUS_EN);
        ModLoader.addLocalization(Resources.KEY_PLUS, Resources.JA, Resources.KEY_PLUS_JA);
        ModLoader.addLocalization(Resources.KEY_MINUS, Resources.KEY_MINUS_EN);
        ModLoader.addLocalization(Resources.KEY_MINUS, Resources.JA, Resources.KEY_MINUS_JA);
        ModLoader.setInGameHook(mod, true, false);
        ModLoader.registerKey(mod, settings.modeUpKey, true);
        ModLoader.registerKey(mod, settings.modeDownKey, true);
        ModLoader.registerKey(mod, settings.plusKey, true);
        ModLoader.registerKey(mod, settings.minusKey, true);
        UserInputHelper.initialize();
    }

    /**
     * レンダー追加.
     *
     * @param map
     *            エンティティレンダーマップ
     */
    public void addRender(Map < Class <? extends Entity > , Render > map)
    {
        map.put(EntitySpawnChecker.class, new RenderSpawnChecker());
        mod.debug("SpawnCheckerRenderer added.");
    }

    /**
     * 毎フレーム処理.
     *
     * @param game
     *            minecraft
     * @return true
     */
    public boolean onTick(float renderPartialTicks, Minecraft game)
    {
        if (game.mcProfiler.profilingEnabled)
        {
            if (!fml)
            {
                // 1.3.2 mod loader 不具合対応用、一度"modtick"セクション抜けてroot以下にぶら下げなおす
                game.mcProfiler.endStartSection("root");
            }

            game.mcProfiler.startSection("SpawnChecker");
            game.mcProfiler.startSection("tick");
        }

        SpawnCheckerTimingHelper.update();
        boolean worldChanged = settings.getCurrentWorld() != game.theWorld;

        if (worldChanged)
        {
            mod.debug("worled chenged.");
            settings.setWorld(game.theWorld);

            if (game.getIntegratedServer() != null)
            {
                MinecraftServer ms = game.getIntegratedServer();
                WorldServer ws = ms.worldServerForDimension(game.thePlayer.dimension);

                if (ws != null)
                {
                    settings.setWorldSeed(ws.getSeed());
                }
                else
                {
                    settings.setWorldSeed(null);
                }
            }
            else
            {
                settings.setWorldSeed(MPWorldSeedHelper.getServerSeed());
            }

            Dimension d = Dimension.getDimensionById(game.thePlayer.dimension);
            settings.setDimension(d);
            mod.debug("Dimension: ", d);
        }

        boolean isResetSpawnChecker = worldChanged;

        if (currentPlayer != game.thePlayer)
        {
            mod.debug("player changed.");
            currentPlayer = game.thePlayer;
            isResetSpawnChecker = true;
        }

        if (isResetSpawnChecker)
        {
            resetSpanwChecker(game);
        }

        if (entity != null)
        {
            if (game.currentScreen == null && game.running)
            {
                game.mcProfiler.startSection("saveSettings");
                saveSettings(worldChanged);
                game.mcProfiler.endStartSection("updateChecker");
                updateChecker(game, worldChanged);
                game.mcProfiler.endSection();
                drawInformations();
            }
            else
            {
                game.mcProfiler.startSection("saveSettings");
                saveSettings(true);
                game.mcProfiler.endSection();
            }
        }
        else
        {
            mod.warn("EntitySpawnChecker join failed.");
        }

        if (game.mcProfiler.profilingEnabled)
        {
            // 1.3.2 mod loader 不具合対応用
            game.mcProfiler.endSection();
            game.mcProfiler.endSection();

            if (!fml)
            {
                game.mcProfiler.endStartSection("modtick");
            }
        }

        return true;
    }

    /**
     * キーボードイベントのハンドラー.
     *
     * @param key
     *            キーバインディング
     */
    public void handleKeyboardEvent(KeyBinding key)
    {
        if (isEnabledKeyDownEvent(key))
        {
            mod.trace("down key: desc=", key.keyDescription, " code=", key.keyCode);

            if (key == settings.modeUpKey)
            {
                onModeUpKeyDown();
                return;
            }

            if (key == settings.modeDownKey)
            {
                onModeDownKeyDown();
                return;
            }

            if (key == settings.plusKey)
            {
                onPlusKeyDown();
                return;
            }

            if (key == settings.minusKey)
            {
                onMinusKeyDown();
                return;
            }
        }
    }

    private void resetSpanwChecker(Minecraft game)
    {
        game.mcProfiler.startSection("reset");
        mod.debug("reset SpawnChecker.");

        if (entity != null)
        {
            entity.destroy();
        }

        entity = new EntitySpawnChecker(game.theWorld);
        game.theWorld.addWeatherEffect(entity);
        Mode mode = settings.getCurrentMode();

        if (mode.isInvalidOption())
        {
            mode.toLowerOption();
        }

        setInformationsByModeDescription(settings.getCurrentMode());
        SpawnPointChecker.reset();
        SpawnPointMarker.clearCache();
        ChunkMarker.clearCache();
        SpawnerMarker.clearCache();
        settings.resetRenderDatas();
        game.mcProfiler.endSection();
    }

    private void changeMode(Mode newMode)
    {
        mod.trace("changeMode");
        assert newMode != null;

        if (settings.getCurrentMode().equals(newMode))
        {
            return;
        }

        mod.debug("change mode: ", settings.getCurrentMode().name(), " -> ", newMode.name());
        settings.setCurrentMode(newMode);
        settings.resetRenderDatas();
        SpawnerVisualizer.clear();
        setInformationsByModeDescription(newMode);
        SpawnCheckerTimingHelper.modeSettingsChanged = true;
        previousMode = null;
    }

    /** マーカー情報などの更新処理. */
    private void updateChecker(Minecraft game, boolean worldChanged)
    {
        if (worldChanged)
        {
            switch (settings.getDimension())
            {
                case NETHER:
                case THE_END:
                    changeMode(Mode.SPAWABLE_POINT_CHECKER);
            }
        }

        Mode mode = settings.getCurrentMode();

        if (UserInputHelper.checkClickForSpawner())
        {
            if (!mode.equals(Mode.SPAWNER_VISUALIZER))
            {
                Mode m = mode;
                changeMode(Mode.SPAWNER_VISUALIZER);
                previousMode = m;
                setSpawnerToSpawnerVisualizer(game);
            }
            else
            {
                setSpawnerToSpawnerVisualizer(game);

                if (!SpawnerVisualizer.getMarkerData().visible && previousMode != null)
                {
                    changeMode(previousMode);
                }
                else
                {
                    setInformationsByModeDescription(mode);
                }
            }
        }

        if (mode.getOption() == Option.DISABLE)
        {
            return;
        }

        switch (mode)
        {
            case SPAWNER_VISUALIZER:
                if (SpawnerVisualizer.getMarkerData() == null || !SpawnerVisualizer.getMarkerData().visible)
                {
                    if (!isControlKeyDown() || !SpawnerVisualizer.findSpawner())
                    {
                        return;
                    }

                    settings.setRenderDataForSpawnerMarker(SpawnerVisualizer.getMarkerData());
                }

                if (SpawnerVisualizer.isLeaveFromPlayer())
                {
                    SpawnerVisualizer.clear();
                    settings.resetRenderDatas();

                    if (previousMode != null)
                    {
                        changeMode(previousMode);
                    }
                }
                else
                {
                    if (isCheckTiming())
                    {
                        mod.trace("update spawner marker data.");
                        SpawnerVisualizer.updateRenderData();
                    }
                }

                return;

            case SPAWABLE_POINT_CHECKER:
                if (!mode.hasOption(Option.FORCE) && !EnablingItemsHelper.hasEnablingItem())
                {
                    return;
                }

                if (isCheckTiming())
                {
                    settings.setRenderDataForSpawnPointList(SpawnPointChecker.getSpawnablePoints());
                }

                return;

            case SLIME_CHUNK_FINDER:
                if (isCheckTiming())
                {
                    List<MarkerBase> list = new LinkedList<MarkerBase>(SpawnPointChecker.getSpawnablePoints());
                    list.addAll(SlimeChunkSearcher.getSlimeChunkMarkers());
                    settings.setRenderDataForChunkMarkerList(list);
                }

                return;

            default:
                throw new InternalError("unexpected mode." + settings.getCurrentMode().name());
        }
    }

    private void setSpawnerToSpawnerVisualizer(Minecraft game)
    {
        MovingObjectPosition mop = game.objectMouseOver;
        SpawnerVisualizer.setSpawner(mop.blockX, mop.blockY, mop.blockZ);

        if (SpawnerVisualizer.getMarkerData() == null)
        {
            // ものすごく稀なタイミングで、スポーナーが他者に破壊された場合に発生
            return;
        }

        settings.setRenderDataForSpawnerMarker(SpawnerVisualizer.getMarkerData());
        SpawnerVisualizer.getMarkerData().visible = !SpawnerVisualizer.getMarkerData().visible;

        if (SpawnerVisualizer.getMarkerData().visible)
        {
            SpawnerVisualizer.updateRenderData();
        }

        if (game.gameSettings.keyBindUseItem.pressed)
        {
            // 右クリの場合音叩いた音を鳴らす
            String sound = Block.mobSpawner.stepSound.getStepSound();
            float volume = (Block.mobSpawner.stepSound.getVolume() + 1.0F) / 8F;
            float pitch = Block.mobSpawner.stepSound.getPitch() * 0.5F;
            float x = (float) mop.blockX + 0.5F;
            float y = (float) mop.blockY + 0.5F;
            float z = (float) mop.blockZ + 0.5F;
            game.sndManager.playSound(sound, x, y, z, volume, pitch);
        }

        game.thePlayer.swingItem();
    }

    private void saveSettings(boolean force)
    {
        if (!settings.settingChanged)
        {
            return;
        }

        if (force || (!isSpawnCheckerBindingKeyPressed() && isSaveTiming()))
        {
            settings.settingChanged = false;
            mod.saveConfig();
        }
    }

    private boolean isSpawnCheckerBindingKeyPressed()
    {
        if (settings.modeUpKey.pressed)
        {
            return true;
        }

        if (settings.modeDownKey.pressed)
        {
            return true;
        }

        if (settings.plusKey.pressed)
        {
            return true;
        }

        if (settings.minusKey.pressed)
        {
            return true;
        }

        return false;
    }

    /*
     * ユーザー操作を処理.
     */
    private void onModeUpKeyDown()
    {
        Mode mode = settings.getCurrentMode();

        if (isControlKeyDown())
        {
            switch (mode)
            {
                case SPAWABLE_POINT_CHECKER:
                    switch (settings.getDimension())
                    {
                        case SURFACE:
                            changeMode(Mode.SLIME_CHUNK_FINDER);
                            return;
                    }

                    // fall-throw

                case SLIME_CHUNK_FINDER:
                    changeMode(Mode.SPAWNER_VISUALIZER);

                    if (SpawnerVisualizer.findSpawner())
                    {
                        settings.setRenderDataForSpawnerMarker(SpawnerVisualizer.getMarkerData());
                    }

                    return;

                case SPAWNER_VISUALIZER:
                    return;

                default:
                    throw new InternalError("unexpected mode." + mode.name());
            }
        }

        if (mode.toUpperOption())
        {
            setInformationsByModeDescription(mode);
            SpawnCheckerTimingHelper.modeSettingsChanged = true;
        }
    }

    private void onModeDownKeyDown()
    {
        Mode mode = settings.getCurrentMode();

        if (isControlKeyDown())
        {
            switch (mode)
            {
                case SPAWNER_VISUALIZER:
                    switch (settings.getDimension())
                    {
                        case SURFACE:
                            changeMode(Mode.SLIME_CHUNK_FINDER);
                            return;
                    }

                    // fall-throw

                case SLIME_CHUNK_FINDER:
                    changeMode(Mode.SPAWABLE_POINT_CHECKER);
                    return;

                case SPAWABLE_POINT_CHECKER:
                    return;

                default:
                    throw new InternalError("unexpected mode." + mode.name());
            }
        }

        if (mode.toLowerOption())
        {
            setInformationsByModeDescription(mode);
            SpawnCheckerTimingHelper.modeSettingsChanged = true;
        }
    }

    private void onPlusKeyDown()
    {
        if (isControlKeyDown() && isShiftKeyDown())
        {
            return;
        }

        if (isControlKeyDown())
        {
            if (settings.incrementBrightness())
            {
                setInformationsByBrightness();
                SpawnCheckerTimingHelper.modeSettingsChanged = true;
            }

            return;
        }

        if (isShiftKeyDown())
        {
            if (settings.incrementRangeVertical())
            {
                setInformationsByRange();
                SpawnCheckerTimingHelper.modeSettingsChanged = true;
            }

            return;
        }

        if (settings.incrementRangeHorizontal())
        {
            setInformationsByRange();
            SpawnCheckerTimingHelper.modeSettingsChanged = true;
        }
    }

    private void onMinusKeyDown()
    {
        if (isControlKeyDown() && isShiftKeyDown())
        {
            return;
        }

        if (isControlKeyDown())
        {
            if (settings.decrementBrightness())
            {
                setInformationsByBrightness();
                SpawnCheckerTimingHelper.modeSettingsChanged = true;
            }

            return;
        }

        if (isShiftKeyDown())
        {
            if (settings.decrementRangeVertical())
            {
                setInformationsByRange();
                SpawnCheckerTimingHelper.modeSettingsChanged = true;
            }

            return;
        }

        if (settings.decrementRangeHorizontal())
        {
            setInformationsByRange();
            SpawnCheckerTimingHelper.modeSettingsChanged = true;
        }
    }

    public static mod_SpawnChecker mod;
    private static Settings settings;

    public static final Settings getSettings()
    {
        return settings;
    }
}
