package de.leonheuer.skycave.islandsystem.enums;

import org.bukkit.Material;

public enum EntityLimitType {

    HOSTILE_FARMING("Feindliche Farmtiere", Material.ZOMBIE_SPAWN_EGG),
    FRIENDLY_FARMING("Freundliche Farmtiere", Material.PIG_SPAWN_EGG),
    HOSTILE("Feindlich", Material.PHANTOM_SPAWN_EGG),
    RAIDER("Überfälle", Material.PILLAGER_SPAWN_EGG),
    FRIENDLY("Freundlich", Material.TROPICAL_FISH_SPAWN_EGG),
    MISC("Sonstige", Material.BAT_SPAWN_EGG),
    ;

    private final String name;
    private final Material mat;

    EntityLimitType(String name, Material mat) {
        this.name = name;
        this.mat = mat;
    }

    public static EntityLimitType getByMat(Material mat) {
        for (EntityLimitType limitType : EntityLimitType.values()) {
            if (limitType.getMat() == mat) {
                return limitType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Material getMat() {
        return mat;
    }
}
