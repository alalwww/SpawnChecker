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

import static com.google.common.base.Preconditions.*;

import java.awt.Color;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import net.minecraft.util.ResourceLocation;

import net.awairo.mcmod.common.v1.util.Colors;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;

/**
 * 画面に描画するモードの情報のシンプルな実装.
 * 
 * @author alalwww
 */
public class SimpleInformation implements Mode.Information
{
    /**
     * 新しいモード情報を取得します.
     * 
     * @param message 表示するメッセージ
     * @return 指定メッセージのモード情報
     */
    public static SimpleInformation of(String message)
    {
        return new SimpleInformation(message);
    }

    /**
     * 新しいモード情報を取得します.
     * 
     * @param format フォーマット
     * @param args パラメーター
     * @return 指定メッセージのモード情報
     */
    public static SimpleInformation of(String format, Object... args)
    {
        return of(String.format(format, args));
    }

    // ------------------------------------------------------------------

    private final String message;

    private int offsetX;
    private int offsetY;

    private Color color;
    private int colorInt;

    private String iconResourceName;
    private ResourceLocation iconResourceLocation;

    private boolean doubleSizeIcon;

    /**
     * Constructor.
     * 
     * @param message メッセージ
     */
    protected SimpleInformation(final String message)
    {
        this.message = checkNotNull(message);
    }

    @Override
    public String message()
    {
        return message;
    }

    @Override
    public int offsetX()
    {
        return offsetX;
    }

    @Override
    public int offsetY()
    {
        return offsetY;
    }

    @Override
    public int intColor()
    {
        return colorInt;
    }

    @Override
    public boolean doubleSizeIcon()
    {
        return doubleSizeIcon;
    }

    @Override
    public boolean hasIcon()
    {
        return !Strings.isNullOrEmpty(getIconResourceName());
    }

    @Override
    public ResourceLocation getIconResource()
    {
        if (iconResourceLocation == null)
            iconResourceLocation = new ResourceLocation(iconResourceName);
        return iconResourceLocation;
    }

    /**
     * オフセットを設定します.
     * 
     * @param offsetX 水平方向のオフセット
     * @return このインスタンス
     */
    public SimpleInformation setOffsetX(int offsetX)
    {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * オフセットを設定します.
     * 
     * @param offsetY 垂直方向のオフセット
     * @return このインスタンス
     */
    public SimpleInformation setOffsetY(int offsetY)
    {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * アイコンリソース名を設定します.
     * 
     * @param iconResourceName アイコンリソースの設定またはnull
     * @return このインスタンス
     */
    public SimpleInformation setIconResourceName(final String iconResourceName)
    {
        this.iconResourceName = iconResourceName;
        iconResourceLocation = null;
        return this;
    }

    /** @return 設定したアイコンリソース名 */
    public String getIconResourceName()
    {
        return iconResourceName;
    }

    /**
     * 文字色を設定します.
     * 
     * @param color 文字色(null値を認めない)
     * @return このインスタンス
     */
    public SimpleInformation setColor(final Color color)
    {
        this.color = checkNotNull(color);
        this.colorInt = Colors.toIntColor(color);
        return this;
    }

    /** @return 設定した文字色 */
    public Color getColor()
    {
        return color;
    }

    /**
     * アイコンのサイズを倍サイズに変更するオプションを設定します.
     * 
     * @param doubleSizeIcon trueはアイコンを倍のサイズで表示します
     * @return このインスタンス
     */
    public SimpleInformation setIconDoubleSize(final boolean doubleSizeIcon)
    {
        this.doubleSizeIcon = doubleSizeIcon;
        return this;
    }

    /** @return 設定したアイコン倍サイズ変更オプションのフラグ値 */
    public boolean getDoubleSizeIcon()
    {
        return doubleSizeIcon;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("color", color)
                .add("message", message)
                .add("iconName", iconResourceName)
                .toString();
    }

}
