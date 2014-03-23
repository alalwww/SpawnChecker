/*
 * SpawnChecker.
 * 
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.spawnchecker.client.common;

/**
 * 座標計算処理ヘルパー.
 * 
 * @author alalwww
 */
public final class CoordHelper
{
    private CoordHelper()
    {
        throw new InternalError();
    }

    /**
     * ワールドの座標をチャンク座標に変換します.
     * 
     * @param worldCoord
     * @return チャンク座標
     */
    public static Integer toChunkCoord(final int worldCoord)
    {
        return IntegerCache.get(worldCoord >> 4);
    }

    /**
     * Integerインスタンスキャッシュ.
     * 
     * @author alalwww
     */
    public static class IntegerCache
    {
        static final int high = 0b1111_1111_1111_1111;
        static final int low = -high;
        private static final Integer cache[];

        static
        {
            cache = new Integer[(high - low) + 1];
            int value = low;
            for (int index = 0; index < cache.length; index++)
                cache[index] = Integer.valueOf(value++);
        }

        static Integer get(int i)
        {
            if (i >= IntegerCache.low && i <= IntegerCache.high)
                return IntegerCache.cache[i + (-IntegerCache.low)];

            return Integer.valueOf(i);
        }

        private IntegerCache()
        {
        }
    }

}
