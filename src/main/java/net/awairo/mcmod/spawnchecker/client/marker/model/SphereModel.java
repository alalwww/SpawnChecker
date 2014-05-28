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

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * 球体の描画用モデル.
 * 
 * @author alalwww
 */
public class SphereModel implements MarkerModel
{
    protected static final Sphere sphere = new Sphere();

    protected int drawStyle = GLU.GLU_LINE;

    protected float radius;
    protected int slices;
    protected int stacks;

    /** @param radius the radius to set */
    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    /** @param slices the slices to set */
    public void setSlices(int slices)
    {
        this.slices = slices;
    }

    /** @param stacks the stacks to set */
    public void setStacks(int stacks)
    {
        this.stacks = stacks;
    }

    public void setDrawStyleToLine()
    {
        drawStyle = GLU.GLU_LINE;
    }

    public void setDrawStyleToFill()
    {
        drawStyle = GLU.GLU_FILL;
    }

    @Override
    public void render()
    {
        sphere.setDrawStyle(drawStyle);
        sphere.draw(radius, slices, stacks);
    }

}
