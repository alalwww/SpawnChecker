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
        static final int HIGH = 0b1111_1111_1111_1111;
        static final int LOW = -HIGH;
        private static final Integer CACHES[];

        static
        {
            CACHES = new Integer[(HIGH - LOW) + 1];
            int value = LOW;
            for (int index = 0; index < CACHES.length; index++)
                CACHES[index] = Integer.valueOf(value++);
        }

        static Integer get(int i)
        {
            if (i >= IntegerCache.LOW && i <= IntegerCache.HIGH)
                return IntegerCache.CACHES[i + (-IntegerCache.LOW)];

            return Integer.valueOf(i);
        }

        private IntegerCache()
        {
        }
    }

}
