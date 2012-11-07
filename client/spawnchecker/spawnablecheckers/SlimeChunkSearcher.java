package spawnchecker.spawnablecheckers;

import static spawnchecker.constants.Constants.SLIME_CHUNK_CHECKED_CHACHE;
import static java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import spawnchecker.markers.ChunkMarker;

/**
 * slime spawn-able chunk searcher.
 *
 * @author takuru/ale
 */
public class SlimeChunkSearcher extends LinkedHashMap<SlimeChunkInfoMapKey, Boolean>
{
    private static final long serialVersionUID = 1L;
    private static Map<SlimeChunkInfoMapKey, Boolean> map = new SlimeChunkSearcher();
    private static Long currentSeed = null;

    /**
     * Check the point is for slime spawn-able chunk.
     *
     * @param seed
     *            world seed
     * @param x
     *            x-coordinate
     * @param z
     *            z-coordinate
     * @return true is slime spawn-able chunk
     */
    public static boolean isSlimeChunk(Long seed, int x, int z)
    {
        if (isSeedChanged(seed))
        {
            map.clear();
        }

        if (seed == null)
        {
            return false;
        }

        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return getCachedResult(seed, chunkX, chunkZ);
    }

    public static Set<ChunkMarker> getSlimeChunkMarkers()
    {
        Set<ChunkMarker> set = new LinkedHashSet<ChunkMarker>();
        ChunkMarker.reset();

        for (Entry<SlimeChunkInfoMapKey, Boolean> e : map.entrySet())
        {
            if (e.getValue() == null || !e.getValue())
            {
                continue;
            }

            set.add(ChunkMarker.getInstanceFromPool(e.getKey().chunkX, e.getKey().chunkZ));
        }

        return set;
    }

    private static boolean isSeedChanged(Long newSeed)
    {
        if (currentSeed == newSeed)
        {
            return false;
        }

        if (newSeed != null && newSeed.equals(currentSeed))
        {
            return false;
        }

        if (currentSeed != null && currentSeed.equals(newSeed))
        {
            return false;
        }

        currentSeed = newSeed;
        return true;
    }

    private static boolean getCachedResult(long seed, int chunkX, int chunkZ)
    {
        SlimeChunkInfoMapKey key = new SlimeChunkInfoMapKey(seed, chunkX, chunkZ);
        Boolean b = map.get(key);

        if (b == null)
        {
            b = isSlimeChunk(key.chunkSeed);
            map.put(key, b);
        }

        return b.booleanValue();
    }

    private static Boolean isSlimeChunk(long slimeChunkSeed)
    {
        return new Random(slimeChunkSeed).nextInt(10) == 0;
    }

    @Override
    protected boolean removeEldestEntry(Entry<SlimeChunkInfoMapKey, Boolean> eldest)
    {
        return size() >= SLIME_CHUNK_CHECKED_CHACHE;
    }

    /**
     * Constructor.
     */
    private SlimeChunkSearcher()
    {
        super(SLIME_CHUNK_CHECKED_CHACHE + 1, 1, true);
    }
}
