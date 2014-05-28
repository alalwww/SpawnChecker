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
 * スポーナーのスポーン可否マーカーのモデル.
 * 
 * @author alalwww
 */
public class MobSpawnerSpawnablePoint extends SphereModel
{
    @Override
    public void render()
    {
        // 中心の小さいの
        setRadius(0.01f);
        setSlices(3);
        setStacks(2);
        setDrawStyleToFill();
        super.render();

        // 外側の面とアウトライン
        setRadius(0.05f);
        setSlices(4);
        setStacks(2);
        setDrawStyleToFill();
        super.render();

        setDrawStyleToLine();
        super.render();
    }

}
