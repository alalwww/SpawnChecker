package spawnchecker.spawnablecheckers;

import static spawnchecker.constants.Constants.NORMAL_MARKER_AABB_OFFSET;
import static spawnchecker.constants.Constants.SLIME_MARKER_AABB_OFFSET;
import static spawnchecker.constants.Constants.SLIME_MARKER_OFFSET;
import static spawnchecker.markers.SpawnPointMarker.getInstanceFromPool;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import spawnchecker.enums.SpawnableEntity;
import spawnchecker.markers.SpawnPointMarker;

/**
 * スポーンポイントマーカー生成ヘルパー.
 *
 * @author takuru/ale
 */
class SpawnPointMarkerCreator
{
    /**
     * 渡したリストにマーカーを追加します.
     */
    static void addMarker(List<SpawnPointMarker> markerList, SpawnableEntity mob, int x, int y, int z)
    {
        switch (mob)
        {
            case ENDERMAN:
            case SKELETON:
            case CREEPER:
            case ZOMBIE:
            case PIG_ZOMBIE:
            case SPIDER:
            case GHAST:
            case MAGMA_CUBE:
                markerList.add(getInstanceFromPool(
                        x, y, z, mob, getYOffset(x, y, z), NORMAL_MARKER_AABB_OFFSET, 0.0D));
                break;

            case SLIME:
                markerList.add(getInstanceFromPool(
                        x, y, z, mob, getYOffset(x, y, z), SLIME_MARKER_AABB_OFFSET, SLIME_MARKER_OFFSET));
                break;

            default:
                break;
        }
    }

    private static double getYOffset(int x, int y, int z)
    {
        World w = ModLoader.getMinecraftInstance().theWorld;
        int bid = w.getBlockId(x, y, z);

        if (bid == Block.snow.blockID)
        {
//            return Block.snow.getMaxY();
            return Block.snow.func_83010_y();
        }
        else if (bid == Block.rail.blockID)
        {
//            return Block.rail.getMaxY();
            return Block.rail.func_83010_y();
        }
        else if (bid == Block.railDetector.blockID)
        {
//            return Block.railDetector.getMaxY();
            return Block.railDetector.func_83010_y();
        }
        else if (bid == Block.railPowered.blockID)
        {
//            return Block.railPowered.getMaxY();
            return Block.railPowered.func_83010_y();
        }
        else if (bid == Block.lever.blockID)
        {
            int metadata = w.getBlockMetadata(x, y, z);

            // was put on the any block. 5 or 6
            if (metadata == 5 || metadata == 6
                    || metadata == 13 || metadata == 14)
            {
                // Block.lever.minY is too higher.
                return 0.18D;
            }
            else
            {
                return 0.0D;
            }
        }
        else if (bid == Block.pressurePlatePlanks.blockID)
        {
            // Block.pressurePlatePlanks.maxY is Inaccuracy.
            return 0.0625D;
        }
        else if (bid == Block.pressurePlateStone.blockID)
        {
            // Block.pressurePlateStone.maxY is Inaccuracy.
            return 0.0625D;
        }
        else
        {
            return 0.0D;
        }
    }

    private SpawnPointMarkerCreator()
    {
    }
}
