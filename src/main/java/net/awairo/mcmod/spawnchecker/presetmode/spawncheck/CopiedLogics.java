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

import com.google.common.primitives.Floats;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldEntitySpawner;

import net.awairo.mcmod.spawnchecker.client.common.ConstantsConfig;

/**
 * Minecraft本体より処理をコピーした、スポーン判定のロジック群.
 * 
 * @author alalwww
 */
public enum CopiedLogics
{
    /** instance. */
    INSTANCE;

    private final Minecraft game = Minecraft.getMinecraft();

    private final ConstantsConfig config = ConstantsConfig.instance();

    /**
     * Constructor.
     */
    CopiedLogics()
    {
    }

    /**
     * モンスターがスポーンできる場所か判定.
     */
    public boolean canSpawnAtLocation(int x, int y, int z)
    {
        return canSpawnAtLocation(x, y, z, EntityLiving.SpawnPlacementType.ON_GROUND);
    }

    /**
     * モンスターがスポーンできる場所か判定.
     */
    public boolean canSpawnAtLocation(int x, int y, int z, EntityLiving.SpawnPlacementType type)
    {
        return WorldEntitySpawner.canCreatureTypeSpawnAtLocation(type, game.theWorld, new BlockPos(x, y, z));
    }

    /**
     * 指定の座標に測定用のエンティティを移動し接触判定.
     * 
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @param entity 接触判定対象エンティティ
     * @return true は指定のエンティティを指定座標に配置した場合、ブロックないしはエンティティに接触する事を意味する
     */
    public boolean isColliding(int x, int y, int z, EntityLiving entity)
    {
        // ブロック中央に配置
        entity.setPosition((double) x + 0.5F, y, (double) z + 0.5F);

        final AxisAlignedBB copiedEntityAABB = entity.getEntityBoundingBox();

        if (!game.theWorld.getCubes(entity, copiedEntityAABB).isEmpty())
        {
            return true;
        }

        if (game.theWorld.isAnyLiquid(copiedEntityAABB))
        {
            return true;
        }

        return false;
    }

    /**
     * 指定座標の空の明るさを考慮しないライトレベルが、指定ライトレベルより小さいかを判定.
     * 
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @param lightLevel 判定用の16段階の明るさ(0～15)
     * @return true は座標はスポーンが可能な明るさ
     */
    public boolean canSpawnByLightLevel(int x, int y, int z, int lightLevel)
    {
        return getSavedLightValue(EnumSkyBlock.BLOCK, x, y, z) < lightLevel;
    }

    /**
     * 指定座標のブライトネスが一定以下か判定.
     * 
     * <p>
     * この判定は日光の影響を考慮した判定を行う。
     * 判定に用いている定数はエンティティのスポーン判定内の定数と同じ。
     * 座標上空に透過じゃないブロックが存在しない場合、明るさの情報がクライアントに送られておらず、
     * 判定結果が正しくなくなるケースがあるため、擬似的に算出した明るさによるスポーン可否判定を行っており、多少の判定結果のズレがある。
     * 判定結果が正しくなくなるのは日の出および日の入り時の時間経過により変わる明るさであるため、多少のズレは割り切り。
     * </p>
     * 
     * @param x x座標
     * @param y y座標
     * @param z z座標
     * @return true は座標はスポーンが可能な明るさ
     */
    public boolean canSpawnByLightBrightness(int x, int y, int z)
    {
        int skyLightValue = getSavedLightValue(EnumSkyBlock.SKY, x, y, z);

        // 15未満の場合はずれない
        if (skyLightValue < 15)
        {
            return Floats.compare(config.spawnableMaxLightValue, getLightBrightness(x, y, z)) >= 0;
        }

        // 時間を条件に擬似的な明るさ算出を行いスポーン可否判定
        final int time = (int) (game.theWorld.getWorldTime() % 24000L);

        // 10段階に分割する際の適当に調整した除数
        // サーバー側の持つ明るさとの差をみながら調整し165くらいが妥当だったのでこの値
        final int adjustmentDivisor = 165;

        // TODO: 日の出(1700)と日没(1650)の時間の差異は本当か調査
        final int dusk = 12000;
        final int sunset = 13650;
        final int sunrise = 22300;

        // 擬似算出したlightValueを -4 した値を格納する
        int lightValueMinus4;

        if (time < dusk)
        {
            // 日中
            lightValueMinus4 = 10;
        }
        else if (time < sunset)
        {
            // 日没にかかった時間 (0～1650)/ adjustmentDivisor
            lightValueMinus4 = (sunset - time) / adjustmentDivisor;
        }
        else if (time < sunrise)
        {
            // 夜
            lightValueMinus4 = 0;
        }
        else
        {
            // 日の出にかかった時間 (0～1700)/ adjustmentDivisor
            lightValueMinus4 = (time - sunrise) / adjustmentDivisor;
        }

        // 時間を元にして10段階にわけた明るさとブロックの明るさ-5のうち大きなほうを選択
        // この辺りのロジックの理由は失念…
        lightValueMinus4 = Math.max(lightValueMinus4, getSavedLightValue(EnumSkyBlock.BLOCK, x, y, z) - 5);
        float brightness = getLightBrightness(lightValueMinus4);
        return Floats.compare(config.spawnableMaxLightValue, brightness) >= 0;
    }

    private static float getLightBrightness(int lightValueMinus4)
    {
        // 0 <= lightValueMinus4 <= 10
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
                // TODO: 未到達のはず 不要なら削除
                return 0.083333f;
        }
    }

    private int getSavedLightValue(EnumSkyBlock skyBlock, int x, int y, int z)
    {
        return game.theWorld.getLightFor(skyBlock, new BlockPos(x, y, z));
        //return game.theWorld.getLightFromNeighborsFor(skyBlock, new BlockPos(x, y, z)); こっちかも
        //return game.theWorld.getSavedLightValue(skyBlock, new BlockPos(x, y, z)); 元
    }

    private float getLightBrightness(int x, int y, int z)
    {
        return game.theWorld.getLightBrightness(new BlockPos(x, y, z));
    }
}
