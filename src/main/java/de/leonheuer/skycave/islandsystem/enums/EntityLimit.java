package de.leonheuer.skycave.islandsystem.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum EntityLimit {

    /**
     * hostile farming
     */
    CREEPER(EntityType.CREEPER, 15, Material.CREEPER_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    DROWNED(EntityType.DROWNED, 15, Material.DROWNED_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    ENDERMAN(EntityType.ENDERMAN, 15, Material.ENDERMAN_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    HUSK(EntityType.HUSK, 15, Material.HUSK_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    SKELETON(EntityType.SKELETON, 15, Material.SKELETON_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    SLIME(EntityType.SLIME, 20, Material.SLIME_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    SPIDER(EntityType.SPIDER, 15, Material.SPIDER_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    STRAY(EntityType.STRAY, 15, Material.STRAY_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    WITCH(EntityType.WITCH, 15, Material.WITCH_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),
    ZOMBIE(EntityType.ZOMBIE, 15, Material.ZOMBIE_SPAWN_EGG, EntityLimitType.HOSTILE_FARMING),

    /**
     * friendly farming
     */
    BEE(EntityType.BEE, 40, Material.BEE_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    CHICKEN(EntityType.CHICKEN, 20, Material.CHICKEN_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    COD(EntityType.COD, 10, Material.COD_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    SALMON(EntityType.SALMON, 10, Material.SALMON_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    COW(EntityType.COW, 20, Material.COW_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, 20, Material.MOOSHROOM_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    PIG(EntityType.PIG, 20, Material.PIG_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    RABBIT(EntityType.RABBIT, 10, Material.RABBIT_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    SHEEP(EntityType.SHEEP, 20, Material.SHEEP_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    SQUID(EntityType.SQUID, 10, Material.SQUID_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),
    TURTLE(EntityType.TURTLE, 10, Material.TURTLE_SPAWN_EGG, EntityLimitType.FRIENDLY_FARMING),

    /**
     * hostile
     */
    PHANTOM(EntityType.PHANTOM, 5, Material.PHANTOM_SPAWN_EGG, EntityLimitType.HOSTILE),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, 5, Material.SKELETON_HORSE_SPAWN_EGG, EntityLimitType.HOSTILE),
    WITHER(EntityType.WITHER, 2, Material.WITHER_SKELETON_SPAWN_EGG, EntityLimitType.HOSTILE),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, 5, Material.ZOMBIE_HORSE_SPAWN_EGG, EntityLimitType.HOSTILE),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, 5, Material.ZOMBIE_VILLAGER_SPAWN_EGG, EntityLimitType.HOSTILE),

    /**
     * raider
     */
    EVOKER(EntityType.EVOKER, 5, Material.EVOKER_SPAWN_EGG, EntityLimitType.RAIDER),
    PILLAGER(EntityType.PILLAGER, 10, Material.PILLAGER_SPAWN_EGG, EntityLimitType.RAIDER),
    RAVAGER(EntityType.RAVAGER, 2, Material.RAVAGER_SPAWN_EGG, EntityLimitType.RAIDER),
    VEX(EntityType.VEX, 10, Material.VEX_SPAWN_EGG, EntityLimitType.RAIDER),
    VINDICATOR(EntityType.VINDICATOR, 5, Material.VINDICATOR_SPAWN_EGG, EntityLimitType.RAIDER),

    /**
     * friendly
     */
    FOX(EntityType.FOX, 5, Material.FOX_SPAWN_EGG, EntityLimitType.FRIENDLY),
    CAT(EntityType.CAT, 5, Material.CAT_SPAWN_EGG, EntityLimitType.FRIENDLY),
    OCELOT(EntityType.OCELOT, 5, Material.OCELOT_SPAWN_EGG, EntityLimitType.FRIENDLY),
    DOLPHIN(EntityType.DOLPHIN, 5, Material.DOLPHIN_SPAWN_EGG, EntityLimitType.FRIENDLY),
    DONKEY(EntityType.DONKEY, 5, Material.DONKEY_SPAWN_EGG, EntityLimitType.FRIENDLY),
    HORSE(EntityType.HORSE, 5, Material.HORSE_SPAWN_EGG, EntityLimitType.FRIENDLY),
    LLAMA(EntityType.LLAMA, 5, Material.LLAMA_SPAWN_EGG, EntityLimitType.FRIENDLY),
    MULE(EntityType.MULE, 5, Material.MULE_SPAWN_EGG, EntityLimitType.FRIENDLY),
    PANDA(EntityType.PANDA, 5, Material.PANDA_SPAWN_EGG, EntityLimitType.FRIENDLY),
    PARROT(EntityType.PARROT, 5, Material.PARROT_SPAWN_EGG, EntityLimitType.FRIENDLY),
    POLAR_BEAR(EntityType.POLAR_BEAR, 5, Material.POLAR_BEAR_SPAWN_EGG, EntityLimitType.FRIENDLY),
    PUFFERFISH(EntityType.PUFFERFISH, 5, Material.PUFFERFISH_SPAWN_EGG, EntityLimitType.FRIENDLY),
    TRADER_LLAMA(EntityType.TRADER_LLAMA, 5, Material.TRADER_LLAMA_SPAWN_EGG, EntityLimitType.FRIENDLY),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, 10, Material.TROPICAL_FISH_SPAWN_EGG, EntityLimitType.FRIENDLY),
    WANDERING_TRADER(EntityType.WANDERING_TRADER, 2, Material.WANDERING_TRADER_SPAWN_EGG, EntityLimitType.FRIENDLY),
    WOLF(EntityType.WOLF, 5, Material.WOLF_SPAWN_EGG, EntityLimitType.FRIENDLY),

    /**
     * misc
     */
    BAT(EntityType.BAT, 5, Material.BAT_SPAWN_EGG, EntityLimitType.MISC),
    SILVERFISH(EntityType.SILVERFISH, 5, Material.SILVERFISH_SPAWN_EGG, EntityLimitType.MISC),
    PIG_ZOMBIE(EntityType.ZOMBIFIED_PIGLIN, 5, Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, EntityLimitType.MISC),
    ;

    private final EntityType type;
    private final int limit;
    private final Material spawnegg;
    private final EntityLimitType limitType;

    EntityLimit(EntityType type, int limit, Material spawnegg, EntityLimitType limitType) {
        this.type = type;
        this.limit = limit;
        this.spawnegg = spawnegg;
        this.limitType = limitType;
    }

    public static int getLimitByType(EntityType type) {
        for (EntityLimit limit : EntityLimit.values()) {
            if (limit.getType() == type) {
                return limit.getLimit();
            }
        }
        return -1;
    }

    public EntityType getType() {
        return type;
    }

    public int getLimit() {
        return limit;
    }

    public Material getSpawnegg() {
        return spawnegg;
    }

    public EntityLimitType getLimitType() {
        return limitType;
    }

}
