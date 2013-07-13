package spawnchecker.spawnervisualizer;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static spawnchecker.constants.Constants.SV_ENABLING_RANGE_LIMIT;
import static spawnchecker.constants.Constants.SV_H_RANGE_FOR_FINDING_SPAWNER;
import static spawnchecker.constants.Constants.SV_V_RANGE_FOR_FINDING_SPAWNER;
import static spawnchecker.constants.Constants.WORLD_HEIGHT_MAX;
import static spawnchecker.constants.Constants.WORLD_HEIGHT_MIN;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import spawnchecker.enums.SpawnableEntity;
import spawnchecker.markers.SpawnerMarker;
import spawnchecker.spawnablecheckers.SpawnPointChecker;

/**
 * スポーナーを可視化するモードの処理です.
 * 
 * @author takuru/ale
 */
public class SpawnerVisualizer
{
    private static final int MIN_H_RANGE = MIN_VALUE + SV_H_RANGE_FOR_FINDING_SPAWNER;
    private static final int MAX_H_RANGE = MAX_VALUE - SV_H_RANGE_FOR_FINDING_SPAWNER;
    private static final int MIN_V_RANGE = WORLD_HEIGHT_MIN + SV_V_RANGE_FOR_FINDING_SPAWNER;
    private static final int MAX_V_RANGE = WORLD_HEIGHT_MAX - SV_V_RANGE_FOR_FINDING_SPAWNER;
    private static int chunkX;
    private static int chunkZ;
    private static Chunk currentChunk;

    private static int spawnerX;
    private static int spawnerY;
    private static int spawnerZ;
    private static String mobID;
    private static SpawnableEntity spawnableEntity;
    private static SpawnerMarker markerData;

    /**
     * 周囲のスポーナーを探します.
     * 
     * @return 見つかったら true
     */
    public static boolean findSpawner()
    {
        Minecraft game = ModLoader.getMinecraftInstance();
        EntityPlayerSP player = game.thePlayer;
        int pix = MathHelper.floor_double(player.posX);
        int piy = MathHelper.floor_double(player.posY);
        int piz = MathHelper.floor_double(player.posZ);
        World w = game.theWorld;
        int minX = pix - (MIN_H_RANGE > pix ? MIN_VALUE : SV_H_RANGE_FOR_FINDING_SPAWNER);
        int minY = piy - (MIN_V_RANGE > piy ? WORLD_HEIGHT_MIN : SV_V_RANGE_FOR_FINDING_SPAWNER);
        int minZ = piz - (MIN_H_RANGE > piz ? MIN_VALUE : SV_H_RANGE_FOR_FINDING_SPAWNER);
        int maxX = pix + (MAX_H_RANGE < pix ? MAX_VALUE : SV_H_RANGE_FOR_FINDING_SPAWNER);
        int maxY = piy + (MAX_V_RANGE < piy ? WORLD_HEIGHT_MAX : SV_V_RANGE_FOR_FINDING_SPAWNER);
        int maxZ = piz + (MAX_H_RANGE < piz ? MAX_VALUE : SV_H_RANGE_FOR_FINDING_SPAWNER);

        for (int x = minX; x <= maxX; x++)
        {
            for (int z = minZ; z <= maxZ; z++)
            {
                Chunk c = getChunk(w, x, z);

                if (c == null)
                {
                    continue;
                }

                for (int y = minY; y <= maxY; y++)
                {
                    if (y < 0 || y >= 256)
                    {
                        continue;
                    }

                    int id = c.getBlockID(x & 0xf, y, z & 0xf);

                    if (id != Block.mobSpawner.blockID)
                    {
                        continue;
                    }

                    setSpawner(x, y, z, c.getChunkBlockTileEntity(x & 0xf, y, z & 0xf));
                    markerData.visible = true;
                    updateRenderData();
                    return true;
                }
            }
        }

        clear();
        return false;
    }

    /**
     * プレイヤーが見つけているスポーナーから離れたかを判定.
     * 
     * @param player
     *            プレイヤー
     * @return スポナーを見つけていないか離れた場合 true
     */
    public static boolean isLeaveFromPlayer()
    {
        if (mobID == null)
        {
            return true;
        }

        EntityPlayerSP player = ModLoader.getMinecraftInstance().thePlayer;
        double x = spawnerX + 0.5d - player.posX;
        double y = spawnerY + 0.5d - player.posY;
        double z = spawnerZ + 0.5d - player.posZ;
        return Math.sqrt(Math.pow(x, 2d) + Math.pow(y, 2d) + Math.pow(z, 2d)) > SV_ENABLING_RANGE_LIMIT;
    }

    /**
     * スポーン可否判定の結果を更新.
     */
    public static void updateRenderData()
    {
        if (markerData == null && !findSpawner())
        {
            return;
        }

        if (!markerData.visible)
        {
            return;
        }

        World w = ModLoader.getMinecraftInstance().theWorld;

        if (w.getBlockId(spawnerX, spawnerY, spawnerZ) != Block.mobSpawner.blockID)
        {
            markerData.visible = false;
            clear();
            return;
        }

        boolean[] result = new boolean[192];
        int index = 0;
        int maxX = spawnerX + 4;
        int maxY = spawnerY + 2;
        int maxZ = spawnerZ + 4;

        for (int x = spawnerX - 4; x < maxX; x++)
        {
            for (int z = spawnerZ - 4; z < maxZ; z++)
            {
                for (int y = spawnerY - 1; y < maxY; y++)
                {
                    result[index++] = SpawnPointChecker.canSpawnable(spawnableEntity, x, y, z);
                }
            }
        }

        markerData.updateSpawnPoint(result);
    }

    public static void clear()
    {
        spawnerX = 0;
        spawnerY = 0;
        spawnerZ = 0;
        mobID = null;
        currentChunk = null;

        if (markerData != null)
        {
            markerData.visible = false;
            markerData = null;
        }
    }

    public static void setSpawner(int x, int y, int z)
    {
        if (x != spawnerX || y != spawnerY || z != spawnerZ)
        {
            World w = ModLoader.getMinecraftInstance().theWorld;
            TileEntity te = w.getBlockTileEntity(x, y, z);
            setSpawner(x, y, z, te);
        }
    }

    private static void setSpawner(int x, int y, int z, TileEntity spawner)
    {
        assert spawner.getBlockType() != null && spawner.blockType.blockID == Block.mobSpawner.blockID;

        if (spawner instanceof TileEntityMobSpawner)
        {
            spawnerX = x;
            spawnerY = y;
            spawnerZ = z;
            mobID = ((TileEntityMobSpawner) spawner).getSpawnerLogic().getEntityNameToSpawn();
            spawnableEntity = SpawnableEntity.getByMobID(mobID);
            currentChunk = null;
            markerData = SpawnerMarker.getInstance(spawnerX, spawnerY, spawnerZ);
        }
    }

    private static Chunk getChunk(World w, int x, int z)
    {
        if (x < 0xfe363c80 || z < 0xfe363c80 || x >= 0x1c9c380 || z >= 0x1c9c380)
        {
            return null;
        }

        int newChunkX = x >> 4;
        int newChunkZ = z >> 4;

        if (currentChunk == null || chunkX != newChunkX || chunkZ != newChunkZ)
        {
            chunkX = newChunkX;
            chunkZ = newChunkZ;
            currentChunk = w.getChunkFromChunkCoords(chunkX, chunkZ);
        }

        return currentChunk;
    }

    public static int getSpawnerX()
    {
        return spawnerX;
    }

    public static int getSpawnerY()
    {
        return spawnerY;
    }

    public static int getSpawnerZ()
    {
        return spawnerZ;
    }

    public static String getMobID()
    {
        return mobID;
    }

    public static SpawnableEntity getSpawnableEntity()
    {
        return spawnableEntity;
    }

    public static SpawnerMarker getMarkerData()
    {
        return markerData;
    }

    private SpawnerVisualizer()
    {
    }
}
