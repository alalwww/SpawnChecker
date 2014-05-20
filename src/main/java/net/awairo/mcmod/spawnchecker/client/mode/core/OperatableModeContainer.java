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

package net.awairo.mcmod.spawnchecker.client.mode.core;

/**
 * 操作可能なモードのコンテナ.
 * 
 * @author alalwww
 */
interface OperatableModeContainer
{
    /**
     * 停止していたら開始する.
     */
    void startIfStopped();

    /**
     * 開始していたら停止する.
     */
    void stopIfStarted();

    /**
     * 更新する.
     */
    void update();

    /**
     * GUIを描画する.
     * 
     * @param tickCounts tick counts
     * @param partialTicks partial ticks of rendering
     */
    void renderGui(long tickCounts, float partialTicks);

    /**
     * ゲーム(ワールド)内に描画する.
     * 
     * @param tickCounts tick counts
     * @param partialTicks partial ticks of rendering
     */
    void renderIngame(long tickCounts, float partialTicks);

    /**
     * 上キー押下を通知する.
     * 
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    void onUpKeyPress(boolean shift, boolean alt);

    /**
     * 下キー押下を通知する.
     * 
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    void onDownKeyPress(boolean shift, boolean alt);

    /**
     * テンキーのプラスキー押下を通知する.
     * 
     * @param ctrl trueはコントロールキーが押下されている
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt);

    /**
     * テンキーのマイナスキー押下を通知する.
     * 
     * @param ctrl trueはコントロールキーが押下されている
     * @param shift trueはシフトキーが押下されている
     * @param alt trueはオルトキーが押下されている
     */
    void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt);
}
