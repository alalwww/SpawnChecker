package spawnchecker.spawnablecheckers;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

/**
 * Measurement slime entity.
 * 
 * @author alalwww
 */
public class EntitySlimeDummy extends EntitySlime
{
    public EntitySlimeDummy(World world)
    {
        super(world);
    }

    @Override
    protected void setSlimeSize(int par1)
    {
        super.setSlimeSize(par1);
    }
}
