package spawnchecker.markers;

import static spawnchecker.constants.Constants.BLOCK_SURFACE_MARGIN;

import java.util.Random;

import net.minecraft.src.Block;

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
//        centerMinX = spawner.getMinX() + x - BLOCK_SURFACE_MARGIN;
        centerMinX = spawner.func_83009_v() + x - BLOCK_SURFACE_MARGIN;
//        centerMinY = spawner.getMinY() + y - BLOCK_SURFACE_MARGIN;
        centerMinY = spawner.func_83008_x() + y - BLOCK_SURFACE_MARGIN;
//        centerMinZ = spawner.getMinZ() + z - BLOCK_SURFACE_MARGIN;
        centerMinZ = spawner.func_83005_z() + z - BLOCK_SURFACE_MARGIN;
//        centerMaxX = spawner.getMaxX() + x + BLOCK_SURFACE_MARGIN;
        centerMaxX = spawner.func_83007_w() + x + BLOCK_SURFACE_MARGIN;
//        centerMaxY = spawner.getMaxY() + y + BLOCK_SURFACE_MARGIN;
        centerMaxY = spawner.func_83010_y() + y + BLOCK_SURFACE_MARGIN;
//        centerMaxZ = spawner.getMaxZ() + z + BLOCK_SURFACE_MARGIN;
        centerMaxZ = spawner.func_83006_A() + z + BLOCK_SURFACE_MARGIN;
        areaMinX = (double)(x - 4);
        areaMinY = (double)(y - 1);
        areaMinZ = (double)(z - 4);
        areaMaxX = (double)(x + 4);
        areaMaxY = (double)(y + 2);
        areaMaxZ = (double)(z + 4);
        duplicationAreaMinX = (double)(x - 8);
        duplicationAreaMinY = (double)(y - 4);
        duplicationAreaMinZ = (double)(z - 8);
        duplicationAreaMaxX = (double)(x + 9);
        duplicationAreaMaxY = (double)(y + 5);
        duplicationAreaMaxZ = (double)(z + 9);
    }
}
