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

package net.awairo.mcmod.spawnchecker.client.controls;

/**
 *「右」キー操作ハンドラー.<br>
 * テンキーレスキーボードに対応する為、テンキー"+"の代替としての役割を果たす。
 * 
 * @author U0326
 */
final class RightKeyHandler extends AbstractKeyHandler
{
    RightKeyHandler(KeyBindingWrapper key)
    {
        super(key);
    }

    @Override
    void onKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
    		// テンキー"+"の代替として、以下メソッドを呼び出す。
        KeyManager.INSTANCE.onPlusKeyPress(ctrl, shift, alt);
    }

}
