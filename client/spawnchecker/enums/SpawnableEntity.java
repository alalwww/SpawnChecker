package spawnchecker.enums;

import static net.minecraft.entity.EntityList.getEntityString;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;

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
