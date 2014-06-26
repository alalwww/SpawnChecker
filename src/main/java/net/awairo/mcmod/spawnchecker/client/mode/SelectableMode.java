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

package net.awairo.mcmod.spawnchecker.client.mode;

import com.google.common.primitives.Ints;

/**
 * ユーザー操作により起動を選択できるモード.
 * 
 * @author alalwww
 */
public interface SelectableMode extends OperatableMode, Comparable<SelectableMode>
{
    /** @return 順序 */
    int ordinal();

    /**
     * 順序の比較用.
     * 
     * @author alalwww
     */
    static class Comparator
    {
        public static int compare(SelectableMode a, SelectableMode b)
        {
            final int result = Ints.compare(a.ordinal(), b.ordinal());
            if (result != 0) return result;
            return a.id().compareTo(b.id());
        }
    }

}
