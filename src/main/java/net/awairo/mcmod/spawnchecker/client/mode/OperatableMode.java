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

/**
 * モード起動中にユーザー操作が可能なモード.
 * 
 * <p>
 * 内部的に使用されるモードのインターフェイスです。このインターフェイスを直接実装しないでください。
 * </p>
 * 
 * <p>
 * ユーザー操作を条件に起動し、ユーザー操作で状態が変更できるモード。
 * 起動条件の確認やソート順は優先度により決まる。
 * </p>
 * 
 * @author alalwww
 */
public interface OperatableMode extends Mode
{

    /** @return icon resource path */
    String iconResourceName();

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

}
