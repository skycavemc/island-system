package de.leonheuer.skycave.islandsystem.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LimitManager {

    private final IslandSystem main;
    private Map<String, Map<EntityType, Integer>> entityCountMap = new HashMap<>();
    private final List<BukkitTask> tasks = new ArrayList<>();

    public LimitManager(IslandSystem main) {
        this.main = main;
    }

    @Nullable
    public BukkitTask start(@NotNull World world) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(world));
        if (rm == null) {
            return null;
        }

        main.getLogger().info("Started limit manager for world " + world.getName());
        BukkitTask task = main.getServer().getScheduler().runTaskTimer(main, () -> {
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
                    if (!r.getId().matches("^[s][c][_]\\d{3}$")) {
                        continue;
                    }
                    addToMap(newMap, r.getId(), e.getType());
                }
            }
            for (Chunk c : world.getLoadedChunks()) {
                for (BlockState state : c.getTileEntities()) {
                    if (state instanceof Beehive hive) {
                        BlockVector3 loc = BukkitAdapter.asBlockVector(state.getLocation());
                        Set<ProtectedRegion> regions = rm.getApplicableRegions(loc).getRegions();
                        if (regions.isEmpty()) {
                            continue;
                        }

                        for (ProtectedRegion r : regions) {
                            if (!r.getId().matches("^[s][c][_]\\d{3}$")) {
                                continue;
                            }
                            addToMap(newMap, r.getId(), EntityType.BEE, hive.getEntityCount());
                        }
                    }
                }
            }
            entityCountMap = newMap;
        }, 0L, 40L);

        tasks.add(task);
        return task;
    }

    public void stopAll() {
        tasks.forEach(BukkitTask::cancel);
    }

    public void addToMap(@NotNull Map<String, Map<EntityType, Integer>> map, String id, EntityType type, int amount) {
        if (!map.containsKey(id)) {
            Map<EntityType, Integer> entities = new HashMap<>();
            entities.put(type, amount);
            map.put(id, entities);
            return;
        }

        Map<EntityType, Integer> entities = map.get(id);
        if (!entities.containsKey(type)) {
            entities.put(type, amount);
            return;
        }

        entities.put(type, entities.get(type) + amount);
    }

    public void addToMap(Map<String, Map<EntityType, Integer>> map, String id, EntityType type) {
        addToMap(map, id, type, 1);
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