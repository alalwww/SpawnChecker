package spawnchecker.spawnablecheckers;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.SpawnerAnimals;
import net.minecraft.src.World;

/**
 * Mob湧き判定ヘルパークラス.
 *
 * @author alalwww
 *
 */
public class SpawnableCheckHelper
{
    private static Minecraft game = Minecraft.getMinecraft();

    /**
     * モンスターがスポーンできる場所かを判定.
     */
    public static boolean canMonsterSpawnAtLocation(int x, int y, int z)
    {
        World w = game.theWorld;
        return SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, w, x, y, z);
    }

    /**
     * 指定座標の空の明るさを考慮しないライトレベルが指定ライトレベルより小さいかを判定.
     */
    public static boolean canSpawnByLightLevel(int x, int y, int z, int lightLevel)
    {
        World w = game.theWorld;
        return w.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < lightLevel;
    }

    /**
     * 指定座標のブライトネスが一定以下か判定.この判定に用いている定数はエンティティのスポーン判定内の定数と同じ。
     * 座標上空に透過じゃないブロックが存在しない場合判定結果は正しくなくなる。
     */
    public static boolean canSpawnByLightBrightness(int x, int y, int z)
    {
        World w = game.theWorld;
        int skyLightValue = w.getSavedLightValue(EnumSkyBlock.Sky, x, y, z);

        if (skyLightValue < 15)
        {
            return 0.5F - w.getLightBrightness(x, y, z) >= 0.0f;
        }

        int time = (int)(w.getWorldTime() % 24000L);
        int lightValueMinus4;

        if (time < 12000)
        {
            lightValueMinus4 = 10;
        }
        else if (time < 13650)
        {
            lightValueMinus4 = (13650 - time) / 165;
        }
        else if (time < 22300)
        {
            lightValueMinus4 = 0;
        }
        else
        {
            lightValueMinus4 = (time - 22300) / 165;
        }

        lightValueMinus4 = Math.max(lightValueMinus4, w.getSavedLightValue(EnumSkyBlock.Block, x, y, z) - 5);
        float brightness = getLightBrightness(lightValueMinus4);
        return 0.5F - brightness >= 0.0f;
    }

    private static float getLightBrightness(int lightValueMinus4)
    {
        switch (Math.min(10, Math.max(0, lightValueMinus4)))
        {
            case 0:
                return 0.111111f;

            case 1:
                return 0.142857f;

            case 2:
                return 0.179487f;

            case 3:
                return 0.222222f;

            case 4:
                return 0.272727f;

            case 5:
                return 0.333333f;

            case 6:
                return 0.407407f;

            case 7:
                return 0.500001f;

            case 8:
                return 0.619048f;

            case 9:
                return 0.777778f;

            case 10:
                return 1.000000f;

            default:
                return 0.083333f;
        }
    }

    /**
     * 指定の座標に測定用のエンティティを移動し接触判定.
     */
    public static boolean isColliding(int x, int y, int z, EntityLiving entity)
    {
        World w = game.theWorld;
        entity.setPosition((double) x + 0.5F, (double) y, (double) z + 0.5F);

        if (entity instanceof EntitySlime)
        {
            ((EntitySlime) entity).setSlimeSize(1);
        }

        AxisAlignedBB bb = entity.boundingBox.copy();

        if (!w.getCollidingBoundingBoxes(entity, bb).isEmpty())
        {
            return true;
        }

        if (w.isAnyLiquid(bb))
        {
            return true;
        }

        return false;
    }

    private SpawnableCheckHelper()
    {
    }
}
