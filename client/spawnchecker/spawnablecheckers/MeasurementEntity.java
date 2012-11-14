package spawnchecker.spawnablecheckers;

import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.SpawnCheckerProtectedAccessHelper;
import net.minecraft.src.World;

/**
 * 測定用Mobエンティティ.
 *
 * @author takuru/ale
 */
class MeasurementEntity
{
    static EntityCaveSpider caveSpider;
    static EntityEnderman enderman;
    static EntityGhast ghast;
    static EntitySilverfish silverfish;
    static EntitySkeleton skeleton;
    static EntitySlime slime;
    static EntitySpider spider;

    static void reset(World w)
    {
        // ワールド変わったらちゃんと破棄してあげないと
        // ワールドがGC対象にならないので作り直し
        caveSpider = new EntityCaveSpider(w);
        enderman = new EntityEnderman(w);
        ghast = new EntityGhast(w);
        silverfish = new EntitySilverfish(w);
        skeleton = new EntitySkeleton(w);
        slime = new EntitySlime(w);
        SpawnCheckerProtectedAccessHelper.setSlimeSize(slime, 1);
        spider = new EntitySpider(w);
    }

    private MeasurementEntity()
    {
    }
}
