package spawnchecker.spawnablecheckers;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static spawnchecker.constants.Constants.CANNOT_SPAWN_LIGHT_LEVEL_OF_SURFACEMOBS;
import static spawnchecker.constants.Constants.HEIGHT_OF_SLIME_SPAWNING_LIMIT_MAX;
import static spawnchecker.constants.Constants.HEIGHT_OF_SPAWNING_LIMIT_MAX;
import static spawnchecker.constants.Constants.HEIGHT_OF_SPAWNING_LIMIT_MIN;
import static spawnchecker.spawnablecheckers.SpawnPointMarkerCreator.addMarker;
import static spawnchecker.spawnablecheckers.SpawnableCheckHelper.canMonsterSpawnAtLocation;
import static spawnchecker.spawnablecheckers.SpawnableCheckHelper.canSpawnByLightBrightness;
import static spawnchecker.spawnablecheckers.SpawnableCheckHelper.canSpawnByLightLevel;
import static spawnchecker.spawnablecheckers.SpawnableCheckHelper.isColliding;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import spawnchecker.Settings;
import spawnchecker.SpawnChecker;
import spawnchecker.enums.Mode;
import spawnchecker.enums.SpawnableEntity;
import spawnchecker.markers.SpawnPointMarker;

/**
 * Mob湧き判定.
 */
public class SpawnPointChecker
{
    private static Minecraft game = Minecraft.getMinecraft();

    public static Settings settings;
    private static int viewpointHeightFixValue = 1;

    private static final List<SpawnPointMarker> markerList = new LinkedList<SpawnPointMarker>();

    /**
     * 現在の設定を元に、スポーンポイントがあるかプレイヤー周囲をチェックします.
     */
    public static List<SpawnPointMarker> getSpawnablePoints()
    {
        EntityPlayerSP player = ModLoader.getMinecraftInstance().thePlayer;
        Mode mode = settings.getCurrentMode();
        boolean slimeCheckEnabled = settings.getWorldSeed() != null;
        int pix = MathHelper.floor_double(player.posX);
        int piy = MathHelper.floor_double(player.posY) - viewpointHeightFixValue;
        int piz = MathHelper.floor_double(player.posZ);
        int h = settings.getRangeHorizontal();
        int v = settings.getRangeVertical();
        int hRangeMin = MIN_VALUE + h;
        int hRangeMax = MAX_VALUE - h;
        int vRangeMin = HEIGHT_OF_SPAWNING_LIMIT_MIN + v;
        int vRangeMax = HEIGHT_OF_SPAWNING_LIMIT_MAX - v;
        int minX = (hRangeMin > pix ? MIN_VALUE : pix - h);
        int minY = (vRangeMin > piy ? HEIGHT_OF_SPAWNING_LIMIT_MIN : piy - v);
        int minZ = (hRangeMin > piz ? MIN_VALUE : piz - h);
        int maxX = (hRangeMax < pix ? MAX_VALUE : pix + h);
        int maxY = (vRangeMax < piy ? HEIGHT_OF_SPAWNING_LIMIT_MAX : piy + v);
        int maxZ = (hRangeMax < piz ? MAX_VALUE : piz + h);
        // SpawnChecker.mod.trace("search range: x=", minX, " -> ", maxX);
        SpawnChecker.mod.trace("search range: y=", minY, " -> ", maxY);
        // SpawnChecker.mod.trace("search range: z=", minZ, " -> ", maxZ);
        SpawnPointMarker.reset();
        markerList.clear();

        for (int x = minX; x <= maxX; x++)
        {
            for (int z = minZ; z <= maxZ; z++)
            {
                for (int y = minY; y <= maxY; y++)
                {
                    if (mode.equals(Mode.SPAWABLE_POINT_CHECKER))
                    {
                        checkSpawnable(x, y, z);
                    }

                    if (mode.hasOption(Mode.Option.SLIME) && slimeCheckEnabled)
                    {
                        checkSlimeSpawnable(x, y, z);
                    }
                }
            }
        }

        return markerList;
    }

    public static void reset()
    {
        // 高さ補正値 通常は 身長 1.8 くらいなので、整数キャストで切り捨てられて 1 になる
        viewpointHeightFixValue = (int) Math.floor(ModLoader.getMinecraftInstance().thePlayer.height);
        markerList.clear();
        MeasurementEntity.reset(ModLoader.getMinecraftInstance().theWorld);
    }

    private static void checkSpawnable(int x, int y, int z)
    {
        switch (settings.getDimension())
        {
            case NETHER:
                checkSpawnableByNether(x, y, z);
                break;

            case SURFACE:
                checkSpawnableBySurface(x, y, z);
                break;

            case THE_END:
                break;

            default:
                return;
        }
    }

    private static void checkSpawnableBySurface(int x, int y, int z)
    {
        if (!canMonsterSpawnAtLocation(x, y, z))
        {
            return;
        }

        if (!canSpawnByLightLevel(x, y, z, CANNOT_SPAWN_LIGHT_LEVEL_OF_SURFACEMOBS))
        {
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.enderman))
        {
            addMarker(markerList, SpawnableEntity.ENDERMAN, x, y, z);
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.skeleton))
        {
            addMarker(markerList, SpawnableEntity.SKELETON, x, y, z);
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.spider))
        {
            addMarker(markerList, SpawnableEntity.SPIDER, x, y, z);
            return;
        }
    }

    private static void checkSpawnableByNether(int x, int y, int z)
    {
        if (!canMonsterSpawnAtLocation(x, y, z))
        {
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.ghast))
        {
            addMarker(markerList, SpawnableEntity.GHAST, x, y, z);
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.skeleton))
        {
            addMarker(markerList, SpawnableEntity.PIG_ZOMBIE, x, y, z);
            return;
        }

        if (!isColliding(x, y, z, MeasurementEntity.slime))
        {
            addMarker(markerList, SpawnableEntity.MAGMA_CUBE, x, y, z);
            return;
        }
    }

    /**
     * 指定の座標に指定のエンティティがスポーンできるかを判定します.
     */
    public static boolean canSpawnable(SpawnableEntity entity, int x, int y, int z)
    {
        switch (entity)
        {
            case CAVE_SPIDER:
            case CREEPER:
            case SKELETON:
            case ZOMBIE:
            case ENDERMAN:
            case SPIDER:
                if (!canSpawnByLightLevel(x, y, z, CANNOT_SPAWN_LIGHT_LEVEL_OF_SURFACEMOBS))
                {
                    return false;
                }

                switch (entity)
                {
                    case CAVE_SPIDER:
                        if (isColliding(x, y, z, MeasurementEntity.caveSpider))
                        {
                            return false;
                        }

                        break;

                    case CREEPER:
                    case SKELETON:
                    case ZOMBIE:
                        if (isColliding(x, y, z, MeasurementEntity.skeleton))
                        {
                            return false;
                        }

                        break;

                    case ENDERMAN:
                        if (isColliding(x, y, z, MeasurementEntity.enderman))
                        {
                            return false;
                        }

                        break;

                    case SPIDER:
                        if (isColliding(x, y, z, MeasurementEntity.spider))
                        {
                            return false;
                        }

                        break;

                    case BLAZE:
                    case GHAST:
                    case GIANT:
                    case MAGMA_CUBE:
                    case OTHERS:
                    case PIG_ZOMBIE:
                    case SILVERFISH:
                    case SLIME:
                    default:
                        break;
                }

                return canSpawnByLightBrightness(x, y, z);

            case SILVERFISH:
                if (!isColliding(x, y, z, MeasurementEntity.silverfish) && canSpawnByLightBrightness(x, y, z))
                {
                    return true;
                }

                break;

            case BLAZE:
                if (!isColliding(x, y, z, MeasurementEntity.skeleton) && canSpawnByLightBrightness(x, y, z))
                {
                    return true;
                }

                break;

            case GHAST:
                if (!isColliding(x, y, z, MeasurementEntity.ghast))
                {
                    return true;
                }

                break;

            case PIG_ZOMBIE:
                if (!isColliding(x, y, z, MeasurementEntity.skeleton))
                {
                    return true;
                }

                break;

            case SLIME:
                if (canSpawnOfSlime(x, y, z))
                {
                    return true;
                }

                break;

            case MAGMA_CUBE:
                if (!isColliding(x, y, z, MeasurementEntity.slime))
                {
                    return true;
                }

                break;

            case GIANT:
                // 需要ないだろうし無視
                return false;

            case OTHERS:
                // バニラの敵対するMob以外のスポーナーは全無視している
                // イカの水や高さ制限、動物の地面が草ブロックであることなどのチェックが必要だが
                // 見りゃわかるレベルなのであまり意味がない
                // 追加Mobなどもエンティティサイズが不明なので対象外
                return false;
        }

        return false;
    }

    private static void checkSlimeSpawnable(int x, int y, int z)
    {
        if (canSpawnOfSlime(x, y, z))
        {
            addMarker(markerList, SpawnableEntity.SLIME, x, y, z);
        }
    }

    private static boolean canSpawnOfSlime(int x, int y, int z)
    {
        boolean isSlimeChunkSpawnHeight = false;
        if (SlimeChunkSearcher.isSlimeChunk(settings.getWorldSeed(), x, z))
        {
            if (y < HEIGHT_OF_SLIME_SPAWNING_LIMIT_MAX)
            {
                isSlimeChunkSpawnHeight = true;
            }
        }
        if (!isSlimeChunkSpawnHeight)
        {
            if (y <= 50 || y >= 70)
            {
                return false;
            }

            if (game.theWorld.getBiomeGenForCoords(x, z) != BiomeGenBase.swampland)
            {
                return false;
            }

            if (!canSpawnByLightLevel(x, y, z, CANNOT_SPAWN_LIGHT_LEVEL_OF_SURFACEMOBS))
            {
                return false;
            }
        }

        if (!canMonsterSpawnAtLocation(x, y, z))
        {
            return false;
        }

        if (isColliding(x, y, z, MeasurementEntity.slime))
        {
            return false;
        }
        return true;
    }

    private SpawnPointChecker()
    {
    }
}
