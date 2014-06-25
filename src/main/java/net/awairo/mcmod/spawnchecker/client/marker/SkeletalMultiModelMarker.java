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

package net.awairo.mcmod.spawnchecker.client.marker;

import net.awairo.mcmod.spawnchecker.client.model.MarkerModel;

/**
 * 複数のモデルを使うマーカー.
 * 
 * @author alalwww
 * 
 * @param <T> marker type
 */
public abstract class SkeletalMultiModelMarker<T extends SkeletalMultiModelMarker<T>> extends SkeletalMarker<T>
{
    /**
     * @deprecated
     *             単一モデルの描画には{@link SkeletalMarker}を使用してください。
     */
    @Override
    @Deprecated
    protected final MarkerModel model()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * モデルの配列を返します.
     * 
     * <p>
     * このメソッドはnull値を返す事はできません。
     * しかし、配列の要素にnull値を含む事はできます。null値の要素は単純に無視されます。
     * </p>
     * <p>
     * このメソッドにより呼び出される配列の参照が変更されることはありません。
     * 特に理由がなければ、高速化のために、常に同一インスタンスを返却することを検討してください。
     * </p>
     * 
     * @return モデル配列
     */
    protected abstract MarkerModel[] models();

    @Override
    public final void doRender(long tickCounts, float partialTicks)
    {
        setTicks(tickCounts, partialTicks);

        for (MarkerModel model : models())
            if (model != null)
                render(model);
    };
}
