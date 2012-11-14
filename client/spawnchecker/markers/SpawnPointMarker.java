package spawnchecker.markers;

import static net.minecraft.src.Block.stone;
import static spawnchecker.constants.Constants.BLOCK_SURFACE_MARGIN;

import java.util.ArrayList;

import spawnchecker.enums.SpawnableEntity;

/**
 * spawn point marker.
 */
public class SpawnPointMarker extends MarkerBase<SpawnPointMarker>
{
    private static ArrayList<SpawnPointMarker> pool = new ArrayList<SpawnPointMarker>(500);
    private static int usedMarker = 0;

    /**
     * marker cached instance getter.
     */
    public static SpawnPointMarker getInstanceFromPool(int x, int y, int z, SpawnableEntity spawnableEntity,
            double yOffset, double innerBoxSize, double innerBoxOffset)
    {
        if (pool.size() <= usedMarker)
        {
            SpawnPointMarker m = new SpawnPointMarker(x, y, z, spawnableEntity, yOffset, innerBoxSize, innerBoxOffset);
            pool.add(m);
            usedMarker++;
            return m;
        }

        return pool.get(usedMarker++)
                .setPositionAndRenderInfo(x, y, z, spawnableEntity, yOffset, innerBoxSize, innerBoxOffset);
    }

    public static void reset()
    {
        usedMarker = 0;
    }

    public static void clearCache()
    {
        reset();

        if (pool.size() > 500)
        {
            pool.clear();
            pool.trimToSize();
            pool.ensureCapacity(500);
        }
        else
        {
            pool.clear();
        }
    }

    public SpawnableEntity spawnableEntity;
    public double outerMinX;
    public double outerMinY;
    public double outerMinZ;
    public double outerMaxX;
    public double outerMaxY;
    public double outerMaxZ;
    public double innerMinX;
    public double innerMinY;
    public double innerMinZ;
    public double innerMaxX;
    public double innerMaxY;
    public double innerMaxZ;

    private SpawnPointMarker(int x, int y, int z, SpawnableEntity spawnableEntity,
            double yOffset, double innerBoxSize, double innerBoxOffset)
    {
        super(x, y, z);
        this.spawnableEntity = spawnableEntity;
        setVertices(yOffset, innerBoxSize, innerBoxOffset);
    }

    public SpawnPointMarker setPositionAndRenderInfo(int x, int y, int z, SpawnableEntity spawnableEntity,
            double yOffset, double innerBoxSize, double innerBoxOffset)
    {
        setPosition(x, y, z);
        this.spawnableEntity = spawnableEntity;
        setVertices(yOffset, innerBoxSize, innerBoxOffset);
        return this;
    }

    private SpawnPointMarker setVertices(double yOffset, double innerBoxSize, double innerBoxOffset)
    {
        double minX = stone.getBlockBoundsMinX() + (double) x;
        double minY = stone.getBlockBoundsMinY() + (double) (y - 1.0D);
        double minZ = stone.getBlockBoundsMinZ() + (double) z;
        double maxX = stone.getBlockBoundsMaxX() + (double) x;
        double maxY = stone.getBlockBoundsMaxY() + (double) (y - 1.0D);
        double maxZ = stone.getBlockBoundsMaxZ() + (double) z;
        outerMinX = minX - BLOCK_SURFACE_MARGIN;
        outerMinY = minY - BLOCK_SURFACE_MARGIN;
        outerMinZ = minZ - BLOCK_SURFACE_MARGIN;
        outerMaxX = maxX + BLOCK_SURFACE_MARGIN;
        outerMaxY = maxY + BLOCK_SURFACE_MARGIN + yOffset;
        outerMaxZ = maxZ + BLOCK_SURFACE_MARGIN;
        innerMinX = minX - innerBoxSize + innerBoxOffset;
        innerMinY = minY - innerBoxSize + innerBoxOffset;
        innerMinZ = minZ - innerBoxSize + innerBoxOffset;
        innerMaxX = maxX + innerBoxSize + innerBoxOffset;
        innerMaxY = maxY + innerBoxSize + innerBoxOffset;
        innerMaxZ = maxZ + innerBoxSize + innerBoxOffset;
        return this;
    }
}
