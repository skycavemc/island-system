package de.leonheuer.skycave.islandsystem.models;

import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class IslandCount {

    private final HashMap<EntityType, Integer> entityCount = new HashMap<>();

    public int getEntityCount(EntityType type) {
        return entityCount.getOrDefault(type, 0);
    }

    public void setEntityCount(EntityType type, int count) {
        entityCount.put(type, count);
    }

    public void incEntityCount(EntityType type) {
        entityCount.put(type, entityCount.getOrDefault(type, 0) + 1);
    }

    public void decEntityCount(EntityType type) {
        entityCount.put(type, entityCount.getOrDefault(type, 0) - 1);
    }

}
