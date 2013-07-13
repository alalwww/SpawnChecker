package spawnchecker.markers;

import static spawnchecker.constants.Constants.BLOCK_SURFACE_MARGIN;

import java.util.Random;

import net.minecraft.block.Block;

/**
 * spawner spawn-able area model.
 */
public class SpawnerMarker extends MarkerBase<SpawnerMarker>
{
    private static SpawnerMarker instance = null;

    /**
     * marker cached instance getter.
     */
    public static SpawnerMarker getInstance(int x, int y, int z)
    {
        if (instance == null)
        {
            instance = new SpawnerMarker(x, y, z);
        }
        else
        {
            instance.setPosition(x, y, z);
        }

        return instance;
    }

    public static void clearCache()
    {
        instance = null;
    }

    public boolean visible = false;
    public double centerMinX;
    public double centerMinY;
    public double centerMinZ;
    public double centerMaxX;
    public double centerMaxY;
    public double centerMaxZ;
    public double areaMinX;
    public double areaMinY;
    public double areaMinZ;
    public double areaMaxX;
    public double areaMaxY;
    public double areaMaxZ;
    public double duplicationAreaMinX;
    public double duplicationAreaMinY;
    public double duplicationAreaMinZ;
    public double duplicationAreaMaxX;
    public double duplicationAreaMaxY;
    public double duplicationAreaMaxZ;
    public boolean[] spawnables;
    public int[] spawnablesOffset;

    private SpawnerMarker(int x, int y, int z)
    {
        super(x, y, z);
        spawnablesOffset = new int[192];
        Random random = new Random();

        for (int i = 0; i < spawnablesOffset.length; i++)
        {
            random.setSeed(i);
            spawnablesOffset[i] = random.nextInt(spawnablesOffset.length);
        }
    }

    @Override
    public SpawnerMarker setPosition(int x, int y, int z)
    {
        super.setPosition(x, y, z);
        setVertices();
        return this;
    }

    public SpawnerMarker updateSpawnPoint(boolean[] spawnables)
    {
        this.spawnables = spawnables;
        return this;
    }

    private void setVertices()
    {
        Block spawner = Block.mobSpawner;
        centerMinX = spawner.getBlockBoundsMinX() + x - BLOCK_SURFACE_MARGIN;
        centerMinY = spawner.getBlockBoundsMinY() + y - BLOCK_SURFACE_MARGIN;
        centerMinZ = spawner.getBlockBoundsMinZ() + z - BLOCK_SURFACE_MARGIN;
        centerMaxX = spawner.getBlockBoundsMaxX() + x + BLOCK_SURFACE_MARGIN;
        centerMaxY = spawner.getBlockBoundsMaxY() + y + BLOCK_SURFACE_MARGIN;
        centerMaxZ = spawner.getBlockBoundsMaxZ() + z + BLOCK_SURFACE_MARGIN;
        areaMinX = x - 4;
        areaMinY = y - 1;
        areaMinZ = z - 4;
        areaMaxX = x + 4;
        areaMaxY = y + 2;
        areaMaxZ = z + 4;
        duplicationAreaMinX = x - 8;
        duplicationAreaMinY = y - 4;
        duplicationAreaMinZ = z - 8;
        duplicationAreaMaxX = x + 9;
        duplicationAreaMaxY = y + 5;
        duplicationAreaMaxZ = z + 9;
    }
}
