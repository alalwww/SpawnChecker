package spawnchecker.constants;

/**
 * SpawnChecker constants.
 */
public final class Constants
{
    /*
     * SpawnChecker.
     */
    public static final long SAVE_DURATION = 5L;
    // repeat starting wait.(nano time)
    public static final long REPEAT_START_DURATION = 300L * 1000L * 1000L;
    // repeat duration.(nano time)
    public static final long REPEAT_DURATION = 50L * 1000L * 1000L;

    /*
     * Settings.
     */
    // range limit.
    public static final int MIN_RANGE_HORIZONTAL = 1;
    public static final int MAX_RANGE_HORIZONTAL = 32;
    public static final int MIN_RANGE_VERTICAL = 1;
    public static final int MAX_RANGE_VERTICAL = 32;
    // brightness limit.
    public static final int MIN_BRIGHTNESS = -5;
    public static final int MAX_BRIGHTNESS = 5;

    /*
     * MPWorldSeedHelper.
     */
    // servers seed setting file name.
    public static final String SERVERS_SEED_SETTING_FILENAME = "servers.txt";

    /*
     * Mode.
     */
    public static final String MODE_ID_SC = "sc";
    public static final String MODE_ID_SF = "sf";
    public static final String MODE_ID_SV = "sv";

    /*
     * Dimension.
     */
    public static final int DIMENSION_NETHER = -1;
    public static final int DIMENSION_SURFACE = 0;
    public static final int DIMENSION_THEEND = 1;

    /*
     * InformationMessageHelper.
     */
    public static final int FONT_HEIGHT = 10;

    /*
     * checker constants.
     */
    public static final int SV_V_RANGE_FOR_FINDING_SPAWNER = 3;
    public static final int SV_H_RANGE_FOR_FINDING_SPAWNER = 2;
    public static final double SV_ENABLING_RANGE_LIMIT = 24D;

    public static final int HEIGHT_OF_SPAWNING_LIMIT_MIN = 1;
    public static final int HEIGHT_OF_SPAWNING_LIMIT_MAX = 254;
    public static final int HEIGHT_OF_SLIME_SPAWNING_LIMIT_MAX = 40;
    public static final int CANNOT_SPAWN_LIGHT_LEVEL_OF_SURFACEMOBS = 8;

    public static final int WORLD_HEIGHT_MIN = 0;
    public static final int WORLD_HEIGHT_MAX = 255;

    public static final int SLIME_CHUNK_CHECKED_CHACHE = 64;

    /*
     * Marker.
     */
    public static final double BLOCK_SURFACE_MARGIN = 0.01F;
    public static final double NORMAL_MARKER_AABB_OFFSET = -0.65D;
    public static final double SLIME_MARKER_AABB_OFFSET = -0.59D;
    // slime marker offset
    public static final double SLIME_MARKER_OFFSET = 0.15D;

    /*
     * EntitySpawnChecker.
     */
    public static final float ENTITY_WIDTH = 0.5f;
    public static final float ENTITY_HEIGHT = 0.5f;

    /*
     * JoinEntityInSurroundingsHelper.
     */
    /** エンティティスポーン処理の再帰回数の上限. */
    public static final int ENTITY_RESET_REFLECTION_LIMIT = 15;

    /*
     * RenderSpawnChecker.
     */
    public static final double GUIDELINE_LENGTH = 64D;
    public static final double CHUNKMARKER_LENGTH = 32D;
    public static final int BASE_BRIGHTNESS = 180;
    public static final int BRIGHTNESS_RATIO = 10;

    public static final double SLIME_CHUNK_MERKER_INTERVAL = 1.5d;

    public static final double SPAWNAREA_AREA_OFFSET = 0.01F;
    public static final double SPAWNAREA_AREA_INTERVAL = 1.0d;

    // spawner point marker
    public static final float SPAWNER_POINT_MARKER_TICK_RATIO = 3;
    public static final float SPAWNER_POINT_RADIUS_INNER = 0.01f;
    public static final int SPAWNER_POINT_SLICES_INNER = 3;
    public static final int SPAWNER_POINT_STACKS_INNER = 2;
    public static final float SPAWNER_POINT_RADIUS_OUTER = 0.05f;
    public static final int SPAWNER_POINT_SLICES_OUTER = 4;
    public static final int SPAWNER_POINT_STACKS_OUTER = 2;

    // spawner activate area
    public static final float SPAWNER_ACTIVATE_AREA_RADIUS = 15.5f;
    public static final int SPAWNER_ACTIVATE_AREA_LINE_SLICES = 10;
    public static final int SPAWNER_ACTIVATE_AREA_LINE_STACKS = 10;
    public static final int SPAWNER_ACTIVATE_AREA_SLICES = 15;
    public static final int SPAWNER_ACTIVATE_AREA_STACKS = 15;

    private Constants()
    {
    }
}
