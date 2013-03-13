package spawnchecker.spawnablecheckers;

/**
 * map key class.
 */
class SlimeChunkInfoMapKey
{
    final long chunkSeed;
    final long worldSeed;
    final int chunkX;
    final int chunkZ;

    /**
     * Constructor.
     *
     * @param seed
     * @param chunkX
     * @param chunkZ
     */
    SlimeChunkInfoMapKey(long seed, int chunkX, int chunkZ)
    {
        this.worldSeed = seed;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        chunkSeed = getSlimeChunkSeed();
    }

    private long getSlimeChunkSeed()
    {
        return worldSeed + (long)(chunkX * chunkX * 0x4c1906) + (long)(chunkX * 0x5ac0db)
                + (long)(chunkZ * chunkZ) * 0x4307a7L + (long)(chunkZ * 0x5f24f) ^ 0x3ad8025fL;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj instanceof SlimeChunkInfoMapKey)
        {
            SlimeChunkInfoMapKey o = (SlimeChunkInfoMapKey) obj;
            return o.worldSeed == worldSeed && o.chunkX == chunkX && o.chunkZ == chunkZ;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) chunkSeed;
    }
}
