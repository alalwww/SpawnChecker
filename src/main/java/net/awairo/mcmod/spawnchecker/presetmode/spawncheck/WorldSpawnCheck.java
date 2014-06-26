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

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck;

import net.awairo.mcmod.spawnchecker.client.common.OptionSet;
import net.awairo.mcmod.spawnchecker.presetmode.spawnchecker.SpawnPointMarker;

/**
 * スポーンチェッカーモードのワールド毎のスポーン判定処理.
 * 
 * @author alalwww
 */
public interface WorldSpawnCheck
{
    /**
     * このチェック処理が実施可能かを判定します
     * 
     * @return trueはこのチェック処理が有効である事を示す
     */
    boolean enable();

    /**
     * 現在の表示対象マーカー.
     * 
     * @return マーカー
     */
    Iterable<SpawnPointMarker> markers();

    /**
     * 現在のマーカーの明るさ設定値を設定します.
     * 
     * @param brightness 明るさ設定値(-5～5)
     */
    void setBrightness(int brightness);

    /**
     * メインターゲットのスポーン判定.
     * 
     * @param x 座標
     * @param y 座標
     * @param z 座標
     */
    void check(int x, int y, int z);

    /**
     * 状態をリセットし、次の更新処理に備えます.
     * 
     * マーカーキャッシュと描画予定のマーカー一覧をリセットします。
     * ワールドが変更されていた場合、インスタンスキャッシュのリサイズ、
     * マーカー保持用リストのりサイズ、測定用エンティティの再生成も行います。
     * 
     * @param options オプションセット
     */
    void reset(OptionSet options);

}
