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

package net.awairo.mcmod.spawnchecker.client.marker.model;

/**
 * マーカーモデル.
 * 
 * <p>
 * 全てのモデルは再利用される可能性があります。
 * そのため個別にリセット処理が行われていない限りは、直前の描画状態を保持しています。
 * 描画を行う前に、常に最適な状態に設定する必要があります。
 * </p>
 * 
 * @author alalwww
 */
public interface MarkerModel
{
    /**
     * レンダリングします.
     */
    void render();
}
