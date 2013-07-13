package spawnchecker.spawnablecheckers;

import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

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
        slime = new EntitySlimeDummy(w);
        ((EntitySlimeDummy) slime).setSlimeSize(1);
        spider = new EntitySpider(w);
    }

    private MeasurementEntity()
    {
    }
}
