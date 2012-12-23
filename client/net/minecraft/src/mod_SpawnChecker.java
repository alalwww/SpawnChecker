package net.minecraft.src;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static spawnchecker.enums.Mode.Option.ACTIVATE_AREA;
import static spawnchecker.enums.Mode.Option.CHUNK_MARKER;
import static spawnchecker.enums.Mode.Option.DISABLE;
import static spawnchecker.enums.Mode.Option.DUPLICATION_AREA;
import static spawnchecker.enums.Mode.Option.FORCE;
import static spawnchecker.enums.Mode.Option.GHAST;
import static spawnchecker.enums.Mode.Option.GUIDELINE;
import static spawnchecker.enums.Mode.Option.MARKER;
import static spawnchecker.enums.Mode.Option.SLIME;
import static spawnchecker.enums.Mode.Option.SPAWNABLE_POINT;
import static spawnchecker.enums.Mode.Option.SPAWNER;
import static spawnchecker.enums.Mode.Option.SPAWN_AREA;
import static spawnchecker.enums.Mode.Option.UNSPAWNABLE_POINT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import spawnchecker.SpawnChecker;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * mod_SpawnChecker.
 *
 * @author fillppo (substitute version author alalwww)
 * @version {@value #VERSION}
 */
public class mod_SpawnChecker extends BaseMod
{
    private static final String VERSION = "144v1 #115";

    /**
     * 難読化されている場合 true、Eclipse 上での実行時は false.
     */
    public static final boolean RELEASE = BaseMod.class.getPackage() == null;

    /**
     * mode.
     */
    @MLProp(name = "mode", info = "sc: Spawable point checker, sf: Slime chunk finder, sv: Spawner visualizer")
    public static String modeValue = "sc";

    /**
     * Spawn-able point checker submode.
     */
    @MLProp(name = "option_sc", min = 0, max = 31, info = "Spawnable point checker option. bit flag [0:disable, 1:+marker, 2:+guideline, 4:+force mode, 8:+slime finder, 16:+ghast finder")
    public static int optionSC = 0;
    @MLProp(name = "option_set_sc", info = "sc mode option set.")
    public static String optionSetSC;

    /**
     * Slime chunk finder submode.
     */
    @MLProp(name = "option_sf", min = 0, max = 7, info = "Slime chunk finder option. bit flag [0:disable, 1:+marker, 2:+guideline, 4:+chunk marker")
    public static int optionSF = 0;
    @MLProp(name = "option_set_sf", info = "sf mode option set.")
    public static String optionSetSF;

    /**
     * Spawner visualizer submode.
     */
    @MLProp(name = "option_sv", min = 0, max = 63, info = "Spawner visualizer option. bit flag [0:disable, 1:+spawner, 2:+spawn area, 4:+spawnable point, 8:+unspawnable point, 16:+duplication limit area, 32:+activate area")
    public static int optionSV = 1;
    @MLProp(name = "option_set_sv", info = "sv mode option set.")
    public static String optionSetSV;

    /**
     * 水平チェック範囲.
     */
    @MLProp(name = "horizontal_range", min = 1, max = 32, info = "horizontal check area range.")
    public static int rangeHorizontal = 10;

    /**
     * 垂直チェック範囲.
     */
    @MLProp(name = "vertical_range", min = 1, max = 32, info = "vertical check area range.")
    public static int rangeVertical = 5;

    /**
     * マーカーの明るさ.
     */
    @MLProp(name = "brightness", min = -5, max = 5, info = "marker and guidline default brightness.")
    public static int brightness = 0;

    /**
     * カンマ区切りのアイテムID文字列.
     */
    @MLProp(name = "enabling_item_ids", info = "comma-separated item id. ex: enabling_item_ids=50,89,91 (default: 50 = torch's ID)")
    public static String enablingItemsCSV = String.valueOf(Block.torchWood.blockID);

    /**
     * チェック間隔の最小時間.
     */
    @MLProp(name = "checking_duration", min = 1, max = 10000, info = "milliseconds checking minimum duration.")
    public static int checkMinDuration = 300;

    /**
     * 情報メッセージの表示時間.
     */
    @MLProp(name = "information_timeout", min = 1, max = 10000, info = "milliseconds information message timeout.")
    public static int informationTimeout = 2500;

    /**
     * スポーナー可視化モード時の右クリック有効無効切り替え.
     */
    @MLProp(name = "enabled_spawner_visualizer_right_click", info = "when this value change to false, disabled 'right click (item use) key' for spawner visualizer toggle visibility.")
    public static boolean enabledSpawnerVisualizerRightClick = true;

    /*
     * 高さ3mobマーカー色.
     */
    @MLProp(name = "spawn_marker1_color_R", min = 0, max = 255, info = "enderman marker color.(default: #40ff00)")
    public static short marker1Red = 64;
    @MLProp(name = "spawn_marker1_color_G", min = 0, max = 255, info = "enderman marker color.")
    public static short marker1Green = 255;
    @MLProp(name = "spawn_marker1_color_B", min = 0, max = 255, info = "enderman marker color.")
    public static short marker1Blue = 0;
    @MLProp(name = "spawn_marker1_color_A", min = 0, max = 255, info = "enderman marker alpha.(default: 100(39.2%))")
    public static short marker1Alpha = 100;

    /*
     * 高さ2mobマーカー色.
     */
    @MLProp(name = "spawn_marker2_color_R", min = 0, max = 255, info = "height 2 mob marker color.(default: #ffff40)")
    public static short marker2Red = 255;
    @MLProp(name = "spawn_marker2_color_G", min = 0, max = 255, info = "height 2 mob marker color.")
    public static short marker2Green = 255;
    @MLProp(name = "spawn_marker2_color_B", min = 0, max = 255, info = "height 2 mob marker color.")
    public static short marker2Blue = 64;
    @MLProp(name = "spawn_marker2_color_A", min = 0, max = 255, info = "height 2 mob marker alpha.(default: 100(39.2%))")
    public static short marker2Alpha = 100;

    /*
     * クモマーカー色.
     */
    @MLProp(name = "spawn_marker3_color_R", min = 0, max = 255, info = "spider marker color.(default: #4040ff)")
    public static short marker3Red = 64;
    @MLProp(name = "spawn_marker3_color_G", min = 0, max = 255, info = "spider marker color.")
    public static short marker3Green = 64;
    @MLProp(name = "spawn_marker3_color_B", min = 0, max = 255, info = "spider marker color.")
    public static short marker3Blue = 255;
    @MLProp(name = "spawn_marker3_color_A", min = 0, max = 255, info = "spider marker alpha.(default: 100(39.2%))")
    public static short marker3Alpha = 100;

    /*
     * スライムマーカー色.
     */
    @MLProp(name = "spawn_marker4_color_R", min = 0, max = 255, info = "slime marker color.(default: #50e8c9)")
    public static short marker4Red = 80;
    @MLProp(name = "spawn_marker4_color_G", min = 0, max = 255, info = "slime marker color.")
    public static short marker4Green = 232;
    @MLProp(name = "spawn_marker4_color_B", min = 0, max = 255, info = "slime marker color.")
    public static short marker4Blue = 201;
    @MLProp(name = "spawn_marker4_color_A", min = 0, max = 255, info = "slime marker alpha.(default: 100(39.2%))")
    public static short marker4Alpha = 100;

    /*
     * ガストマーカー色.
     */
    @MLProp(name = "spawn_marker5_color_R", min = 0, max = 255, info = "ghast marker color.(default: #4040ff)")
    public static short marker5Red = 255;
    @MLProp(name = "spawn_marker5_color_G", min = 0, max = 255, info = "ghast marker color.")
    public static short marker5Green = 255;
    @MLProp(name = "spawn_marker5_color_B", min = 0, max = 255, info = "ghast marker color.")
    public static short marker5Blue = 255;
    @MLProp(name = "spawn_marker5_color_A", min = 0, max = 255, info = "ghast marker alpha.(default: 100(39.2%))")
    public static short marker5Alpha = 100;

    /*
     * スライムチャンクマーカー色.
     */
    @MLProp(name = "slime_chunk_color_R", min = 0, max = 255, info = "slime chunk marker color.(default: #50e8c9)")
    public static short slimeChunkRed = 80;
    @MLProp(name = "slime_chunk_color_G", min = 0, max = 255, info = "slime chunk marker color.")
    public static short slimeChunkGreen = 232;
    @MLProp(name = "slime_chunk_color_B", min = 0, max = 255, info = "slime chunk marker color.")
    public static short slimeChunkBlue = 201;
    @MLProp(name = "slime_chunk_color_A", min = 0, max = 255, info = "slime chunk marker alpha.(default: 100(39.2%))")
    public static short slimeChunkAlpha = 64;

    /*
     * スポナーボーダー色.
     */
    @MLProp(name = "spawner_border_color_R", min = 0, max = 255, info = "spawner border color.(default: #ff0000)")
    public static short spawnerRed = 225;
    @MLProp(name = "spawner_border_color_G", min = 0, max = 255, info = "spawner border color.")
    public static short spawnerGreen = 0;
    @MLProp(name = "spawner_border_color_B", min = 0, max = 255, info = "spawner border color.")
    public static short spawnerBlue = 0;
    @MLProp(name = "spanwer_border_color_A", min = 0, max = 255, info = "spawner border alpha.(default: 255(100.0%))")
    public static short spawnerAlpha = 255;

    /*
     * モブスポーンエリア色.
     */
    @MLProp(name = "spawner_spawn_area_color_R", min = 0, max = 255, info = "spawner spawn area color.(default: #00ff00)")
    public static short spawnerSpawnAreaRed = 0;
    @MLProp(name = "spawner_spawn_area_color_G", min = 0, max = 255, info = "spawner spawn area color.")
    public static short spawnerSpawnAreaGreen = 255;
    @MLProp(name = "spawner_spawn_area_color_B", min = 0, max = 255, info = "spawner spawn area color.")
    public static short spawnerSpawnAreaBlue = 0;
    @MLProp(name = "spawner_spawn_area_color_A", min = 0, max = 255, info = "spawner spawn area alpha.(default: 100(39.2%))")
    public static short spawnerSpawnAreaAlpha = 100;

    /*
     * モブ湧き制限エリア色.
     */
    @MLProp(name = "spawner_duplication_area_color_R", min = 0, max = 255, info = "spawner duplication area color.(default: #0000ff)")
    public static short duplicationAreaRed = 0;
    @MLProp(name = "spawner_duplication_area_color_G", min = 0, max = 255, info = "spawner duplicationarea color.")
    public static short duplicationAreaGreen = 0;
    @MLProp(name = "spawner_duplication_area_color_B", min = 0, max = 255, info = "spawner duplication area color.")
    public static short duplicationAreaBlue = 255;
    @MLProp(name = "spawner_duplication_area_color_A", min = 0, max = 255, info = "spawner duplication area alpha.(default: 100(39.2%))")
    public static short duplicationAreaAlpha = 100;

    /*
     * スポナーのスポーンするポイント色.
     */
    @MLProp(name = "spawner_spawnable_point_line_color_R", min = 0, max = 255, info = "spawner activate area color.(default: #ffa080)")
    public static short spawnerSpawnablePointRed = 255;
    @MLProp(name = "spawner_spawnable_point_line_color_G", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerSpawnablePointGreen = 160;
    @MLProp(name = "spawner_spawnable_point_line_color_B", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerSpawnablePointBlue = 128;
    @MLProp(name = "spawner_spawnable_point_line_color_A", min = 0, max = 255, info = "spawner activate area alpha.(default: 64(25.1%))")
    public static short spawnerSpawnablePointAlpha = 64;

    /*
     * スポナーのスポーンしないポイント色.
     */
    @MLProp(name = "spawner_unspawnable_point_line_color_R", min = 0, max = 255, info = "spawner activate area color.(default: #a0ffd6)")
    public static short spawnerUnspawnablePointRed = 160;
    @MLProp(name = "spawner_unspawnable_point_line_color_G", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerUnspawnablePointGreen = 255;
    @MLProp(name = "spawner_unspawnable_point_line_color_B", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerUnspawnablePointBlue = 214;
    @MLProp(name = "spawner_unspawnable_point_line_color_A", min = 0, max = 255, info = "spawner activate area alpha.(default: 64(25.1%))")
    public static short spawnerUnspawnablePointAlpha = 64;

    /*
     * スポナーアクティブエリア色.
     */
    @MLProp(name = "spawner_activate_area_color_R", min = 0, max = 255, info = "spawner activate area color.(default: #402010)")
    public static short spawnerActivateAreaRed = 64;
    @MLProp(name = "spawner_activate_area_color_G", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerActivateAreaGreen = 32;
    @MLProp(name = "spawner_activate_area_color_B", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerActivateAreaBlue = 16;
    @MLProp(name = "spawner_activate_area_color_A", min = 0, max = 255, info = "spawner activate area alpha.(default: 64(25.1%))")
    public static short spawnerActivateAreaAlpha = 64;

    /*
     * スポナーアクティブエリア色(線).
     */
    @MLProp(name = "spawner_activate_area_line_color_R", min = 0, max = 255, info = "spawner activate area color.(default: #ff8080)")
    public static short spawnerActivateAreaLineRed = 255;
    @MLProp(name = "spawner_activate_area_line_color_G", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerActivateAreaLineGreen = 128;
    @MLProp(name = "spawner_activate_area_line_color_B", min = 0, max = 255, info = "spawner activate area color.")
    public static short spawnerActivateAreaLineBlue = 128;
    @MLProp(name = "spawner_activate_area_line_color_A", min = 0, max = 255, info = "spawner activate area alpha.(default: 96(37.6%))")
    public static short spawnerActivateAreaLineAlpha = 96;

    /*
     * メッセージ描画位置オフセット.
     */
    @MLProp(name = "info_v_offset", min = -100, max = 100, info = "information message vertical offset.")
    public static int infoVOffset = -50;
    @MLProp(name = "info_h_offset", min = -30, max = 30, info = "information message horizontal offset.")
    public static int infoHOffset = 0;

    /*
     * メッセージ色.
     */
    @MLProp(name = "info_color_R", min = 0, max = 255, info = "message color.(default: #ffffff)")
    public static short infoRed = 255;
    @MLProp(name = "info_color_G", min = 0, max = 255, info = "message color.")
    public static short infoGreen = 255;
    @MLProp(name = "info_color_B", min = 0, max = 255, info = "message color.")
    public static short infoBlue = 255;

    /*
     * レンダリング用エンティティ位置オフセット.
     */
    @MLProp(name = "entity_offset_x", min = -100.0D, max = 100.0D, info = "offset for marker renderer entity position.")
    public static double entityOffsetX = 0.0D;
    @MLProp(name = "entity_offset_y", min = -100.0D, max = 100.0D, info = "offset for marker renderer entity position.")
    public static double entityOffsetY = 5.0D;
    @MLProp(name = "entity_offset_z", min = -100.0D, max = 100.0D, info = "offset for marker renderer entity position.")
    public static double entityOffsetZ = 0.0D;

    /**
     * debug mode flag.
     */
    @MLProp(name = "debug")
    public static boolean DEBUG_MODE = !RELEASE;

    static
    {
        optionSetSC = "" + DISABLE
                + "," + (GHAST)
                + "," + (GHAST | GUIDELINE)
                + "," + (MARKER)
                + "," + (MARKER | GUIDELINE)
                + "," + (GHAST | FORCE)
                + "," + (GHAST | GUIDELINE | FORCE)
                + "," + (MARKER | FORCE)
                + "," + (MARKER | GUIDELINE | FORCE)
                + "," + (MARKER | SLIME)
                + "," + (MARKER | SLIME | GUIDELINE)
                + "," + (MARKER | SLIME | FORCE)
                + "," + (MARKER | SLIME | GUIDELINE | FORCE)
                ;
        optionSetSF = "" + DISABLE
                + "," + (MARKER)
                + "," + (MARKER | GUIDELINE)
                + "," + (CHUNK_MARKER)
                + "," + (MARKER | CHUNK_MARKER)
                + "," + (MARKER | GUIDELINE | CHUNK_MARKER)
                ;
        optionSetSV = "" + (SPAWNER)
                + "," + (SPAWNER | SPAWN_AREA)
                + "," + (SPAWNER | SPAWN_AREA | SPAWNABLE_POINT | DUPLICATION_AREA)
                + "," + (SPAWNER | SPAWN_AREA | UNSPAWNABLE_POINT | DUPLICATION_AREA)
                + "," + (SPAWNER | SPAWN_AREA | SPAWNABLE_POINT | UNSPAWNABLE_POINT | DUPLICATION_AREA)
                + "," + (SPAWNER | ACTIVATE_AREA)
                + "," + (SPAWNER | SPAWN_AREA | ACTIVATE_AREA)
                + "," + (SPAWNER | SPAWN_AREA | SPAWNABLE_POINT | DUPLICATION_AREA | ACTIVATE_AREA)
                + "," + (SPAWNER | SPAWN_AREA | UNSPAWNABLE_POINT | DUPLICATION_AREA | ACTIVATE_AREA)
                + "," + (SPAWNER | SPAWN_AREA | SPAWNABLE_POINT | UNSPAWNABLE_POINT | DUPLICATION_AREA | ACTIVATE_AREA)
                ;
    }

    private final SpawnChecker checker;

    /**
     * modクラスの simpleName.
     */
    private final String modName;

    /**
     * modの設定ファイルより読み込んだプロパティ.
     */
    private final Properties properties;

    /**
     * ロガーインスタンス.
     */
    private final Logger logger;

    /**
     * コンフィグファイルの保存先のディレクトリ.
     *
     * Windowsであれば通常は「%appdata%/.minecraft/config/」ディレクトリ。
     */
    private final File configDirectory;

    /**
     * mod 用コンフィグファイルの保存先のディレクトリ.
     *
     * Windowsであれば通常は「%appdata%/.minecraft/config/mod の simpleName/」ディレクトリ。
     */
    private final File modConfigDirectory;

    /**
     * MLProp の設定を保持する コンフィグファイル.
     */
    private final File configFile;

    /**
     * 非リリース時にログレベルに関係なくデバッグログを出力したい場合 true.
     */
    private boolean enableDebugLog = true;

    /**
     * 非リリース時にログレベルに関係なくトレースログを出力したい場合 true.
     */
    private boolean traceEnabled = false;

    /**
     * ModLoader.txt にログを出力する場合 true.
     */
    private boolean modLoaderLoggerEnabled = RELEASE;

    private String propertyComments = null;
    private List<Field> fields = null;

    private boolean fml = true;

    /**
     * Constructor.
     */
    public mod_SpawnChecker()
    {
        modName = getClass().getSimpleName();
        properties = new Properties();
        logger = Logger.getLogger(modName);
        logger.setParent(ModLoader.getLogger());
        configDirectory = getConfigDir();
        configFile = new File(configDirectory, modName + ".cfg");
        modConfigDirectory = new File(configDirectory, modName + "/");
        loadConfig();
        String trace = properties.getProperty("trace");

        if (trace != null && trace.equals("true"))
        {
            logger.setLevel(Level.ALL);
        }
        else
        {
            logger.setLevel(DEBUG_MODE ? Level.FINEST : Level.FINER);
        }

        checker = new SpawnChecker(this, fml);
        // traceEnabled = true; // 開発時専用 トレースログ出力フラグ
    }

    @Override
    public String getVersion()
    {
        return VERSION;
    }

    @Override
    public void load()
    {
        checker.initialize();

        if (fml)
        {
            Map < Class <? extends Entity > , Render > map = new HashMap < Class <? extends Entity > , Render > ();
            checker.addRender(map);

            for (Map.Entry < Class <? extends Entity > , Render > entry : map.entrySet())
            {
                RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), entry.getValue());
                EntityRegistry.registerGlobalEntityID(entry.getKey(), "spawnChecker", ModLoader.getUniqueEntityId());
                break;
            }
        }
    }

    @Override
    public boolean onTickInGame(float renderPartialTicks, Minecraft game)
    {
        return checker.onTick(renderPartialTicks, game);
    }

    @Override
    public void keyboardEvent(KeyBinding key)
    {
        checker.handleKeyboardEvent(key);
    }

    @Override
    public void addRenderer(Map entityRenderMap)
    {
        if (!fml)
        {
            checker.addRender(entityRenderMap);
        }
    }

    // configure file management
    // --------------------------------------------------------
    /**
     * コンフィグファイルを再読み込みし、MLPropで修飾されたフィールド値を更新します.
     *
     * @throws IllegalStateException
     *             ファイルが読み込めない場合にスローされます.
     */
    public void loadConfig() throws IllegalStateException
    {
        info("reload configuration file.");
        loadProperties(properties, configFile);
        setFieldValueByProperties();
    }

    /**
     * MLPropで修飾されたフィールド値を取得し、コンフィグファイルに書き出します.
     *
     * @throws IllegalStateException
     *             ファイルに書き込めない場合にスローされます.
     */
    public void saveConfig() throws IllegalStateException
    {
        info("save configuration to file.");
        setPropertiesValuesByField();
        saveProperties(properties, propertyComments, configFile);
    }

    /**
     * mod用のコンフィグファイルの抽象表現を取得します.
     * <p>
     * この処理は config ディレクトリ以下に mod 名のディレクトリを作成し、
     * そのなかに指定のファイル名のファイルの抽象表現を取得します。
     * ファイル名が妥当であるか、ファイルが作成できるかなどはチェックしません。
     * また、このメソッドではファイルの生成は行いません。
     * </p>
     *
     * @param path
     *            ".minecraft/config/mod名/" ディレクトリ直下のコンフィグファイル名
     * @return 作成したファイル
     * @throws IllegalArgumentException
     *             指定されたパスがディレクトリ(フォルダー)の場合にスローされます.
     */
    public File getModConfigFile(String filePath) throws IllegalArgumentException
    {
        modConfigDirectory.mkdir();
        File file = new File(modConfigDirectory, filePath);

        if (file.isDirectory())
        {
            throw new IllegalArgumentException("File param is directory. :" + file.getAbsolutePath());
        }

        return file;
    }

    /**
     * ファイルからプロパティを読み込みます.
     *
     * @param prop
     *            プロパティ
     * @param filePath
     *            ".minecraft/config/mod名/" ディレクトリ直下のコンフィグファイル名
     * @throws IllegalArgumentException
     *             指定されたパスがディレクトリの場合にスローされます.
     * @throws IllegalStateException
     *             ファイルが読み込めない場合にスローされます.
     */
    public void loadProperties(Properties prop, String filePath)
    throws IllegalArgumentException, IllegalStateException
    {
        loadProperties(prop, getModConfigFile(filePath));
    }

    /**
     * ファイルからプロパティを読み込みます.
     *
     * @param prop
     *            プロパティ
     * @param file
     *            読み込むプロパティファイル
     *
     * @throws IllegalArgumentException
     *             fileがディレクトリ(フォルダー)の場合にスローされます.
     * @throws IllegalStateException
     *             ファイルがなく作成できなかったか、読み込めない場合にスローされます.
     */
    public void loadProperties(Properties prop, File file)
    throws IllegalArgumentException, IllegalStateException
    {
        try
        {
            checkFile(file);

            if (!file.canRead())
            {
                throw new IllegalStateException("File reading failed. :" + file.getAbsolutePath());
            }

            prop.load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            handleException(e);
        }
    }

    /**
     * プロパティをファイルに書き出します.
     *
     * @param prop
     *            プロパティ
     * @param comments
     *            コメント
     * @param filePath
     *            ".minecraft/config/mod名/" ディレクトリ直下のコンフィグファイル名
     * @throws IllegalArgumentException
     *             指定されたパスがディレクトリの場合にスローされます.
     * @throws IllegalStateException
     *             ファイルがなく作成できなかったか、書き込めない場合にスローされます.
     */
    public void saveProperties(Properties prop, String comments, String filePath)
    throws IllegalArgumentException, IllegalStateException
    {
        saveProperties(prop, comments, getModConfigFile(filePath));
    }

    /**
     * プロパティをファイルに書き出します.
     *
     * @param prop
     *            プロパティ
     * @param comments
     *            コメント
     * @param file
     *            書き出すプロパティファイル
     * @throws IllegalArgumentException
     *             指定されたパスがディレクトリの場合にスローされます.
     * @throws IllegalStateException
     *             ファイルがなく作成できなかったか、書き込めない場合にスローされます.
     */
    public void saveProperties(Properties prop, String comments, File file)
    throws IllegalArgumentException, IllegalStateException
    {
        try
        {
            checkFile(file);

            if (!file.canWrite())
            {
                throw new IllegalStateException("could not write file." + file.getAbsolutePath());
            }

            FileOutputStream fos = null;

            try
            {
                fos = new FileOutputStream(file);
                prop.store(fos, comments);
            }
            finally
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
        }
        catch (IOException e)
        {
            handleException(e);
        }
    }

    /**
     * ファイルがディレクトリでないことをチェックし、ファイルがなかったら作成する.
     * 頻度の低い例外ケースまでは想定されていない手抜き実装.
     */
    private void checkFile(File file) throws IOException
    {
        if (file.isDirectory())
        {
            throw new IllegalArgumentException("'file' is directory. :" + file.getAbsolutePath());
        }

        if (!file.exists())
        {
            if (file.createNewFile())
            {
                info("create config file.: ", file.getAbsolutePath());
            }
            else
            {
                if (!file.exists())
                {
                    throw new IllegalStateException("File creation failed. :" + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * プロパティからフィールドに値を設定.
     */
    private void setFieldValueByProperties()
    {
        Iterator<Field> i = getMLPropFields();

        while (i.hasNext())
        {
            Field f = i.next();
            assert(f.getModifiers() & (STATIC | PUBLIC)) != 0;
            assert f.isAnnotationPresent(MLProp.class);
            Class<?> type = f.getType();
            MLProp mlp = f.getAnnotation(MLProp.class);
            String key = mlp.name().length() != 0 ? mlp.name() : f.getName();
            String valueString = properties.getProperty(key);

            if (valueString == null)
            {
                continue;
            }

            Object value;

            if (type.isAssignableFrom(String.class))
            {
                value = valueString;
            }
            else if (type.isAssignableFrom(Integer.TYPE))
            {
                value = Integer.valueOf(Integer.parseInt(valueString));
            }
            else if (type.isAssignableFrom(Long.TYPE))
            {
                value = Long.valueOf(Long.parseLong(valueString));
            }
            else if (type.isAssignableFrom(Short.TYPE))
            {
                value = Short.valueOf(Short.parseShort(valueString));
            }
            else if (type.isAssignableFrom(Byte.TYPE))
            {
                value = Byte.valueOf(Byte.parseByte(valueString));
            }
            else if (type.isAssignableFrom(Boolean.TYPE))
            {
                value = Boolean.valueOf(Boolean.parseBoolean(valueString));
            }
            else if (type.isAssignableFrom(Float.TYPE))
            {
                value = Float.valueOf(Float.parseFloat(valueString));
            }
            else if (type.isAssignableFrom(Double.TYPE))
            {
                value = Double.valueOf(Double.parseDouble(valueString));
            }
            else
            {
                value = null;
            }

            if (value == null)
            {
                warn("invalid field type: key=", key, "type=", type.getName());
                continue;
            }

            if (value instanceof Number)
            {
                double d = ((Number) value).doubleValue();

                if (mlp.min() != (-1.0D / 0.0D) && d < mlp.min()
                        || mlp.max() != (1.0D / 0.0D) && d > mlp.max())
                {
                    warn("invalid value: name=", key, "value=", value);
                    continue;
                }
            }

            try
            {
                Object o = f.get(null);

                if (!value.equals(o))
                {
                    debug(key, " set to ", value);
                    f.set(null, value);
                }
            }
            catch (IllegalAccessException e)
            {
                handleException(e);
            }
        }
    }

    /**
     * フィールドからプロパティに値を設定.
     */
    private void setPropertiesValuesByField()
    {
        Iterator<Field> i = getMLPropFields();
        StringBuilder comments = new StringBuilder();

        while (i.hasNext())
        {
            Field f = i.next();
            assert(f.getModifiers() & (STATIC | PUBLIC)) != 0;
            assert f.isAnnotationPresent(MLProp.class);
            MLProp mlprop = f.getAnnotation(MLProp.class);
            String key = mlprop.name().length() != 0 ? mlprop.name() : f.getName();

            try
            {
                Object value = f.get(null);
                String valueString = value != null ? value.toString() : "";
                properties.setProperty(key, valueString);
                StringBuilder sb1 = new StringBuilder();

                if (mlprop.min() != (-1.0D / 0.0D))
                {
                    sb1.append(String.format(",>=%.1f", mlprop.min()));
                }

                if (mlprop.max() != (1.0D / 0.0D))
                {
                    sb1.append(String.format(",<=%.1f", mlprop.max()));
                }

                StringBuilder sb2 = new StringBuilder();

                if (mlprop.info().length() > 0)
                {
                    sb2.append(" -- ").append(mlprop.info());
                }

                comments.append(String.format("%s (%s:%s%s)%s\n", key, getName(), value, sb1, sb2));
            }
            catch (IllegalAccessException e)
            {
                handleException(e);
            }
        }

        propertyComments = comments.toString();
    }

    /**
     * MLPropを持つフィールドを取得します.
     *
     * @return MLProp修飾子を持つフィールドの反復子
     */
    private Iterator<Field> getMLPropFields()
    {
        if (fields != null)
        {
            return fields.iterator();
        }

        fields = new LinkedList<Field>();

        for (Field f : getClass().getFields())
        {
            if ((f.getModifiers() & (STATIC | PUBLIC)) != 0 && f.isAnnotationPresent(MLProp.class))
            {
                fields.add(f);
            }
        }

        return fields.iterator();
    }

    // logging
    // --------------------------------------------------------
    private static final String TRACE = "trace";
    private static final String DEBUG = "debug";
    private static final String INFO = "info";
    private static final String WARN = "warn";
    private static final String ERROR = "error";

    /**
     * 現在のログレベルを取得します.
     *
     * @return ログレベル
     */
    public Level getLogLevel()
    {
        return getLogLevel(logger);
    }

    /**
     * ログレベルを設定します.
     * <p>
     * nullを設定した場合、ModLoader共通のもつ共通のログレベルになります。
     * </p>
     *
     * @param newLevel
     *            Level インスタンス または null
     */
    public void setLogLevel(Level newLevel)
    {
        logger.setLevel(newLevel);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     * ログレベルが {@link Level#ALL} の場合のみ出力します.
     *
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void trace(Object... messages)
    {
        if ((RELEASE || !traceEnabled) && !canLogging(Level.ALL))
        {
            return;
        }

        logging(TRACE, messages);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param throwable
     *            例外またはエラー
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void trace(Throwable trowable, Object... messages)
    {
        if ((RELEASE || !traceEnabled) && !canLogging(Level.ALL))
        {
            return;
        }

        trace(messages);
        logging(trowable);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     * Eclipse上での実行か、ログレベルが {@link Level#FINEST} の場合のみ出力します.
     *
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void debug(Object... messages)
    {
        if ((RELEASE || !enableDebugLog) && !canLogging(Level.FINEST))
        {
            return;
        }

        logging(DEBUG, messages);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param throwable
     *            例外またはエラー
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void debug(Throwable trowable, Object... messages)
    {
        if ((RELEASE || !enableDebugLog) && !canLogging(Level.FINEST))
        {
            return;
        }

        debug(messages);
        logging(trowable);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void info(Object... messages)
    {
        if (!canLogging(Level.INFO))
        {
            return;
        }

        logging(INFO, messages);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param throwable
     *            例外またはエラー
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void info(Throwable trowable, Object... messages)
    {
        if (!canLogging(Level.INFO))
        {
            return;
        }

        info(messages);
        logging(trowable);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void warn(Object... messages)
    {
        if (!canLogging(Level.WARNING))
        {
            return;
        }

        logging(WARN, messages);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param throwable
     *            例外またはエラー
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void warn(Throwable trowable, Object... messages)
    {
        if (!canLogging(Level.WARNING))
        {
            return;
        }

        warn(messages);
        logging(trowable);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void error(Object... messages)
    {
        if (!canLogging(Level.SEVERE))
        {
            return;
        }

        logging(ERROR, messages);
    }

    /**
     * ログを出力します.
     *
     * <p>messages に指定した引数を全て文字列として結合し、メッセージにします。</p>
     *
     * @param throwable
     *            例外またはエラー
     * @param messages
     *            メッセージ。null または 空配列の場合何もしない
     */
    public void error(Throwable throwable, Object... messages)
    {
        if (!canLogging(Level.SEVERE))
        {
            return;
        }

        error(messages);
        logging(throwable);
    }

    /**
     * 標準出力に下記フォーマットのログを出力します.
     *
     * <p>[modName](type): message</p>
     *
     * @param type
     *            log type (error/warn/info/debug/trace/etc...)
     * @param message
     *            output message
     */
    public void stdout(String type, String message)
    {
        System.out.println(new StringBuilder(modName.length() + type.length() + message.length() + 6)
                .append("[").append(modName).append("](").append(type).append("): ")
                .append(message).toString());
    }

    private void logging(String type, Object... messages)
    {
        assert type != null;

        if (messages == null || messages.length <= 0)
        {
            return;
        }

        String s = joinMessage(messages);
        stdout(type, s);

        if (!modLoaderLoggerEnabled)
        {
            return;
        }

        if (type.equals(TRACE))
        {
            logger.log(Level.ALL, s);
        }
        else if (type.equals(DEBUG))
        {
            logger.finest(s);
        }
        else if (type.equals(INFO))
        {
            logger.info(s);
        }
        else if (type.equals(WARN))
        {
            logger.warning(s);
        }
        else if (type.equals(ERROR))
        {
            logger.severe(s);
        }
        else
        {
            throw new InternalError("unknown type:" + type);
        }
    }

    private void logging(Throwable t)
    {
        t.printStackTrace();
        StackTraceElement[] stes = t.getStackTrace();

        if (stes.length > 0)
        {
            logger.throwing(stes[0].getClassName(), stes[0].getMethodName(), t);
        }
    }

    private String joinMessage(Object... messages)
    {
        assert messages != null;
        StringBuilder sb = new StringBuilder();

        for (Object s : messages)
        {
            sb.append(s);
        }

        return sb.toString();
    }

    private boolean canLogging(Level level)
    {
        return getLogLevel().intValue() <= level.intValue();
    }

    private Level getLogLevel(Logger logger)
    {
        if (logger == null)
        {
            return Level.FINER;
        }

        Level level = logger.getLevel();

        if (level == null)
        {
            return getLogLevel(logger.getParent());
        }

        return level;
    }

    // --------------------------------------------------------

    private File getConfigDir()
    {
        try
        {
            Class<?> clazz = Class.forName("cpw.mods.fml.common.Loader");
            Method m = clazz.getMethod("instance", (Class[]) null);
            Object o = m.invoke(null);
            m = clazz.getMethod("getConfigDir", (Class[]) null);
            return (File) m.invoke(o);
        }
        catch (ClassNotFoundException  e)
        {
            if (enableDebugLog)
            {
                debug(e, "");
            }

            info("NotFound Minecraft Forge's ModLoader.");
            fml = false;
            return (File) getPrivateValue(ModLoader.class, null, "cfgdir");
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    // --------------------------------------------------------

    /**
     * {@link ModLoader#getPrivateValue(Class, Object, int)} wrapper.
     *
     * @param clazz
     *            target class
     * @param obj
     *            target class instance or null
     * @param index
     *            target class field index
     * @param type
     *            return value type
     * @return private value
     */
    public <T, E> T getPrivateValue(Class <? super E > clazz, E instance, int index)
    {
        return getPrivateValueInternal(clazz, instance, Integer.valueOf(index));
    }

    /**
     * {@link ModLoader#getPrivateValue(Class, Object, String)} wrapper.
     *
     * @param clazz
     *            target class
     * @param obj
     *            target class instance or null
     * @param field
     *            target class field name
     * @param type
     *            return value type
     * @return private value or null
     */
    public <T, E> T getPrivateValue(Class <? super E > clazz, E instance, String field)
    {
        return getPrivateValueInternal(clazz, instance, field);
    }

    private <T, E, V> T getPrivateValueInternal(Class <? super E > clazz, E instance, V target)
    {
        try
        {
            if (target instanceof Integer)
            {
                return (T) ModLoader.getPrivateValue(clazz, instance, (Integer) target);
            }

            if (target instanceof String)
            {
                return (T) ModLoader.getPrivateValue(clazz, instance, (String) target);
            }

            throw new InternalError("unexpected target value. : " + target);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null; // unreachable code
    }

    /**
     * logging and throw the exception.
     *
     * @param e
     *            the exception
     */
    protected void handleException(Exception e)
    {
        error(e, e.getMessage());

        if (e instanceof RuntimeException)
        {
            throw(RuntimeException) e;
        }

        throw new RuntimeException(e);
    }
}