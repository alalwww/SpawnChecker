package spawnchecker.markers;

import static spawnchecker.constants.Constants.CHUNKMARKER_LENGTH;

import java.util.ArrayList;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.src.ModLoader;

/**
 * chunk marker.
 *
 * @author takuru/ale
 */
public class ChunkMarker extends MarkerBase<ChunkMarker>
{
    private static ArrayList<ChunkMarker> pool = new ArrayList<ChunkMarker>(20);
    private static int usedMarker = 0;

    public static ChunkMarker getInstanceFromPool(int chunkX, int chunkZ)
    {
        if (pool.size() <= usedMarker)
        {
            ChunkMarker m = new ChunkMarker(chunkX, chunkZ);
            pool.add(m);
            usedMarker++;
            return m;
        }

        return pool.get(usedMarker++).setPosition(chunkX, chunkZ);
    }

    public static void reset()
    {
        usedMarker = 0;
    }

    public static void clearCache()
    {
        reset();

        if (pool.size() > 20)
        {
            pool.clear();
            pool.trimToSize();
            pool.ensureCapacity(20);
        }
        else
        {
            pool.clear();
        }
    }

    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    private ChunkMarker(int x, int z)
    {
        super(x, 0, z);
    }

    @Override
    public ChunkMarker setPosition(int chunkX, int unuse, int chunkZ)
    {
        return setPosition(chunkX, chunkZ);
    }

    public ChunkMarker setPosition(int chunkX, int chunkZ)
    {
        super.setPosition(chunkX, 0, chunkZ);
        setVertices();
        return this;
    }

    private void setVertices()
    {
        EntityPlayerSP p = ModLoader.getMinecraftInstance().thePlayer;
        minX = x * 16d;
        minY = p.posY - CHUNKMARKER_LENGTH;
        minZ = z * 16d;
        maxX = x * 16d + 16d;
        maxY = p.posY + CHUNKMARKER_LENGTH;
        maxZ = z * 16d + 16d;
    }
}
