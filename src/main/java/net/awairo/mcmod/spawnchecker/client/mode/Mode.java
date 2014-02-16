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

import java.awt.Color;

import net.minecraft.util.ResourceLocation;

import net.awairo.mcmod.common.v1.util.LimitedNumber;

/**
 * SpawnChecker mode.
 * 
 * <p>
 * このインターフェイスを実装したクラスのFQDNを、IMCでを送信することで、独自の新たなモードを追加することが出来ます。
 * IMCのmodidは、"spawnchecker"、メッセージキーは"registerMode"になります。
 * インターフェイスを独自に実装することも出来ますが、{@link net.awairo.mcmod.spawnchecker.client.mode.core.ModeBase}を継承し、
 * 最低限の実装のみで新たなモードを作成することも出来ます。
 * </p>
 * 
 * @author alalwww
 */
public interface Mode extends Comparable<Mode>
{
    /** @return id */
    String id();

    /** @return mode name */
    String name();

    /** @return icon resource path */
    String iconResourceName();

    /** @return ordinal */
    int ordinal();

    /** @return true is mode enabled */
    boolean enabled();

    /**
     * initialize mode.
     * 
     * モードの初期化を行います。
     * このメソッドは、SpawnChecker の FMLPostInitializationEvent が呼ばれたタイミングで実行されます。
     * 具体的には、設定のロードなど、最初に一度のみ行えばいい処理を実行するタイミングです。
     */
    void initialize();

    /**
     * mode start.
     * 
     * このモードが開始されたタイミングで実行されます。
     * このタイミングは、このモードが初期選択であり、初めてワールドにログインしたか、別のモードからこのモードへと切り替わった場合です。
     * モードの更新や描画に必要なリソースを準備などを行うタイミングです。
     */
    void begin();

    /**
     * mode update.
     * 
     * このモードが開始している場合に、定期的に実行されます。
     * 呼び出される間隔は、初期設定では最短でおよそ200ms毎に呼び出されます。
     */
    void update();

    /**
     * mode stop.
     * 
     * このモードが終了されるタイミングで実行されます。
     * このタイミングは、別のモードに切り替わるか、ワールドが削除された場合です。
     * ここでは必要のないリソースの開放などを行います。
     * 
     * ゲーム終了時には、このメソッドが実行されない可能性があります。
     * 何らかのデータの永続化を行いたい場合、更新処理の中で行うことを検討してください。
     */
    void end();

    /**
     * ゲームのワールド内への描画を行います.
     * 
     * このメソッドは頻繁に実行されます。
     * 
     * @param tickCount tick count
     * @param partialTick partial tick
     */
    void renderIngame(long tickCount, float partialTick);

    /**
     * ゲームのGUIへの描画を行います.
     * 
     * このメソッドは頻繁に実行されます。
     * 
     * @param tickCount tick count
     * @param partialTick partial tick
     */
    void renderGui(long tickCount, float partialTick);

    /**
     * 上キーが押下された場合の処理を行います.
     * 
     * <p>
     * CTRLキーとの同時操作はモード変更操作として予約されているため通知されません。
     * </p>
     * 
     * @param shift true is key down
     * @param alt true is key down
     */
    void onUpKeyPress(boolean shift, boolean alt);

    /**
     * 下キーが押下された場合の処理を行います.
     * 
     * <p>
     * CTRLキーとの同時操作はモード変更操作として予約されているため通知されません。
     * </p>
     * 
     * @param shift true is key down
     * @param alt true is key down
     */
    void onDownKeyPress(boolean shift, boolean alt);

    /**
     * テンキーのプラスキーが押下された場合の処理を行います.
     * 
     * @param ctrl true is key down
     * @param shift true is key down
     * @param alt true is key down
     */
    void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt);

    /**
     * テンキーのマイナスキーが押下された場合の処理を行います.
     * 
     * @param ctrl true is key down
     * @param shift true is key down
     * @param alt true is key down
     */
    void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt);

    // ----------------------------------------------------

    /**
     * Mode option.
     * 
     * @author alalwww
     */
    interface Option extends Comparable<Option>
    {
        String id();

        /** @return display option name */
        String name();
    }

    /**
     * 共通のスポーンチェッカー状態.
     * 
     * @author alalwww
     */
    interface CommonState
    {
        /** @return 水平範囲 */
        LimitedNumber<Integer> horizontalRange();

        /** @return 垂直範囲 */
        LimitedNumber<Integer> verticalRange();

        /** @return 明るさ */
        LimitedNumber<Integer> brightness();
    }

    /**
     * スポーンチェッカーのプリセットモードで利用されている共通の色設定.
     * 
     * @author alalwww
     */
    interface CommonColor
    {
        /** @return メッセージの文字色 */
        Color informationMessage();

        /** @return エンダーマンマーカー 色 */
        Color enderman();

        /** @return 高さ2のMob用マーカー 色 */
        Color standardSizeMob();

        /** @return スパイダー用マーカー色 */
        Color spider();

        /** @return スライム用マーカー色 */
        Color slime();

        /** @return ガスト用マーカー色 */
        Color ghast();

        /** @return スライムチャンクマーカー色 */
        Color slimeChunk();

        /** @return 表示対象スポーナーマーカー色 */
        Color spawnerBorder();

        /** @return スポーナーのスポーン可能エリアマーカー色 */
        Color spawnerSpawnArea();

        /** @return スポーナーの重複スポーン可能範囲マーカー色 */
        Color spawnerSpawnLimitArea();

        /** @return スポーナーのスポーン可能マーカー色 */
        Color spawnerSpawnablePoint();

        /** @return スポーナーのスポーン不可能マーカー色 */
        Color spawnerUnspawnablePoint();

        /** @return スポーナーアクティブ化範囲マーカー(面)色 */
        Color spawnerActiveArea();

        /** @return スポーナーアクティブ化範囲マーカー(縁)色 */
        Color spawnerActiveAreaLine();
    }

    /**
     * モードの情報.
     * 
     * @author alalwww
     */
    interface Information
    {
        /**
         * 表示メッセージを取得.
         * 
         * @return 表示メッセージ
         */
        String message();

        /**
         * 水平方向のオフセット
         * 
         * @return オフセット
         */
        int offsetX();

        /**
         * 垂直方向のオフセット
         * 
         * @return オフセット
         */
        int offsetY();

        /**
         * 文字色を取得.
         * 
         * @return 文字色
         */
        public int intColor();

        /**
         * @return true はアイコンを持っている情報を意味する
         */
        boolean hasIcon();

        /**
         * @return true はアイコンサイズを倍のサイズで描画する
         */
        boolean doubleSizeIcon();

        /**
         * アイコンリソースを取得します.
         * 
         * <p>
         * このメソッドは、{@link #hasIcon()} がtrueを返却する場合、必ず{@link ResourceLocation}を返します。
         * falseを帰す場合の動作は未定義です。null値を返すかもしれませんし、RuntimeExceptionをスローするかもしれません。
         * </p>
         * 
         * @return アイコンリソース
         */
        ResourceLocation getIconResource();
    }
}
