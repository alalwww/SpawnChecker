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

package net.awairo.mcmod.spawnchecker.presetmode.spawncheck.spawner;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.SpawnCheck;

/**
 * 
 * 
 * @author alalwww
 */
public interface MobSpawnerSpawnableCheck extends SpawnCheck
{
    boolean supported();

    public static final class EntityMap
    {
        private static final ImmutableMap<String, Class<? extends MobSpawnerSpawnableCheck>> INSTANCE;

        static
        {
            INSTANCE = ImmutableMap.<String, Class<? extends MobSpawnerSpawnableCheck>> builder()
                    .put("Creeper", StandardSizeMobSpawnableCheck.class)
                    .put("Skeleton", StandardSizeMobSpawnableCheck.class)
                    .put("Spider", SpiderSpawnableCheck.class)
                    .put("Giant", UnsupportedMobSpawnableCheck.class)
                    .put("Zombie", StandardSizeMobSpawnableCheck.class)
                    .put("Slime", SlimeSpawnableCheck.class)
                    .put("Ghast", UnsupportedMobSpawnableCheck.class)
                    .put("PigZombie", UnsupportedMobSpawnableCheck.class)
                    .put("Enderman", EndermanSpawnableCheck.class)
                    .put("CaveSpider", CaveSpiderSpawnableCheck.class)
                    .put("Silverfish", SilverfishSpawnableCheck.class)
                    .put("Blaze", BlazeSpawnableCheck.class)
                    .put("LavaSlime", UnsupportedMobSpawnableCheck.class)
                    .put("EnderDragon", UnsupportedMobSpawnableCheck.class)
                    .put("WitherBoss", UnsupportedMobSpawnableCheck.class)
                    .put("Bat", UnsupportedMobSpawnableCheck.class)
                    .put("Witch", UnsupportedMobSpawnableCheck.class)
                    .put("Pig", PigSpawnableCheck.class)
                    .put("Sheep", UnsupportedMobSpawnableCheck.class)
                    .put("Cow", UnsupportedMobSpawnableCheck.class)
                    .put("Chicken", UnsupportedMobSpawnableCheck.class)
                    .put("Squid", UnsupportedMobSpawnableCheck.class)
                    .put("Wolf", UnsupportedMobSpawnableCheck.class)
                    .put("MushroomCow", UnsupportedMobSpawnableCheck.class)
                    .put("SnowMan", UnsupportedMobSpawnableCheck.class)
                    .put("Ozelot", UnsupportedMobSpawnableCheck.class)
                    .put("VillagerGolem", UnsupportedMobSpawnableCheck.class)
                    .put("EntityHorse", UnsupportedMobSpawnableCheck.class)
                    .put("Villager", UnsupportedMobSpawnableCheck.class)
                    .build();
        }

        public static MobSpawnerSpawnableCheck newInstanceOf(String entityId)
        {
            try
            {
                if (INSTANCE.containsKey(entityId))
                    return INSTANCE.get(entityId).newInstance();
                return new UnsupportedMobSpawnableCheck();
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                throw Throwables.propagate(e);
            }
        }

        private EntityMap()
        {
            throw new InternalError();
        }
    }
}
