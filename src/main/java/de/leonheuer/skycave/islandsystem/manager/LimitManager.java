package de.leonheuer.skycave.islandsystem.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LimitManager {

    private final IslandSystem main;
    private Map<String, Map<EntityType, Integer>> entityCountMap = new HashMap<>();

    public LimitManager(IslandSystem main) {
        this.main = main;
    }

    public void start(@NotNull World world) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(world));
        if (rm == null) {
            return;
        }

        main.getLogger().info("Started limit manager for world " + world.getName());
        main.getServer().getScheduler().runTaskTimer(main, () -> {
            Map<String, Map<EntityType, Integer>> newMap = new HashMap<>();
            for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class)) {
                if (EntityLimit.getLimitByType(e.getType()) == -1) {
                    continue;
                }

                BlockVector3 loc = BukkitAdapter.asBlockVector(e.getLocation());
                Set<ProtectedRegion> regions = rm.getApplicableRegions(loc).getRegions();
                if (regions.isEmpty()) {
                    continue;
                }

                for (ProtectedRegion r : regions) {
                    if (!IslandUtils.isValidName(r.getId())) {
                        continue;
                    }
                    addToMap(newMap, r.getId(), e.getType());
                }
            }
            entityCountMap = newMap;
        }, 0L, 40L);
    }

    public void addToMap(Map<String, Map<EntityType, Integer>> map, String id, EntityType type) {
        if (!map.containsKey(id)) {
            Map<EntityType, Integer> entities = new HashMap<>();
            entities.put(type, 1);
            map.put(id, entities);
            return;
        }

        Map<EntityType, Integer> entities = map.get(id);
        if (!entities.containsKey(type)) {
            entities.put(type, 1);
            return;
        }

        entities.put(type, entities.get(type) + 1);
    }

    public int getEntityCount(String regionId, EntityType type) {
        if (!entityCountMap.containsKey(regionId)) {
            return 0;
        }
        return entityCountMap.get(regionId).getOrDefault(type, 0);
    }

    public Map<String, Map<EntityType, Integer>> getEntityCountMap() {
        return entityCountMap;
    }

}
