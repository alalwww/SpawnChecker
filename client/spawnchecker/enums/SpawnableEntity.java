package spawnchecker.enums;

import static net.minecraft.src.EntityList.getEntityString;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiantZombie;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntityZombie;

/**
 * スポーンするエンティティの列挙.
 *
 * 使用可否考えずにとりあえず敵対するやつごっそり.
 *
 * @author takuru/ale
 */
public enum SpawnableEntity
{
    CREEPER(getEntityString(new EntityCreeper(null))),
    SKELETON(getEntityString(new EntitySkeleton(null))),
    SPIDER(getEntityString(new EntitySpider(null))),
    GIANT(getEntityString(new EntityGiantZombie(null))),
    ZOMBIE(getEntityString(new EntityZombie(null))),
    SLIME(getEntityString(new EntitySlime(null))),
    GHAST(getEntityString(new EntityGhast(null))),
    PIG_ZOMBIE(getEntityString(new EntityPigZombie(null))),
    ENDERMAN(getEntityString(new EntityEnderman(null))),
    CAVE_SPIDER(getEntityString(new EntityCaveSpider(null))),
    SILVERFISH(getEntityString(new EntitySilverfish(null))),
    BLAZE(getEntityString(new EntityBlaze(null))),
    MAGMA_CUBE(getEntityString(new EntityMagmaCube(null))),
    OTHERS("others mobs");

    private static SpawnableEntity[] valuesCopy = null;
    private final String mobID;

    /**
     * Constructor.
     */
    private SpawnableEntity(String mobID)
    {
        this.mobID = mobID;
    }

    public static SpawnableEntity getByMobID(String mobID)
    {
        if (valuesCopy == null)
        {
            valuesCopy = values();
        }

        for (SpawnableEntity mob : valuesCopy)
        {
            if (mob.mobID.equals(mobID))
            {
                return mob;
            }
        }

        return OTHERS;
    }
}
