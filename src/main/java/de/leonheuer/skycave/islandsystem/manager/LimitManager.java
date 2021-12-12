package de.leonheuer.skycave.islandsystem.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.models.IslandCount;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LimitManager {

    private final IslandSystem main;
    private final HashMap<ProtectedRegion, IslandCount> limitsMap = new HashMap<>();

    public LimitManager(IslandSystem main) {
        this.main = main;
    }

    public void start(World world) {
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (rm == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                limitsMap.clear();
                HashMap<ProtectedRegion, IslandCount> count = new HashMap<>();
                for (Entity entity : Bukkit.getWorld("skybeeisland").getEntities()) {
                    if (!(entity instanceof LivingEntity)) {
                        continue;
                    }
                    if (EntityLimit.getLimitByType(entity.getType()) == -1) {
                        continue;
                    }
                    ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(entity.getLocation()));
                    if (set.getRegions().size() == 0) {
                        continue;
                    }
                    for (ProtectedRegion region : set.getRegions()) {
                        if (!region.getId().matches("^[s][c][_]\\d{3}$")) {
                            continue;
                        }
                        if (!count.containsKey(region)) {
                            count.put(region, new IslandCount());
                        }
                        count.get(region).incEntityCount(entity.getType());
                        setEntityCount(region, entity.getType(), count.get(region).getEntityCount(entity.getType()));
                    }
                }
            }
        }.runTaskTimer(main, 0L, 200L);
    }

    public void incEntityCount(ProtectedRegion region, EntityType type) {
        if (!limitsMap.containsKey(region)) {
            limitsMap.put(region, new IslandCount());
        }
        limitsMap.get(region).incEntityCount(type);
    }

    public void decEntityCount(ProtectedRegion region, EntityType type) {
        if (!limitsMap.containsKey(region)) {
            limitsMap.put(region, new IslandCount());
        }
        limitsMap.get(region).decEntityCount(type);
    }

    public int getEntityCount(ProtectedRegion region, EntityType type) {
        if (!limitsMap.containsKey(region)) {
            limitsMap.put(region, new IslandCount());
        }
        return limitsMap.get(region).getEntityCount(type);
    }

    public void setEntityCount(ProtectedRegion region, EntityType type, int count) {
        if (!limitsMap.containsKey(region)) {
            limitsMap.put(region, new IslandCount());
        }
        limitsMap.get(region).setEntityCount(type, count);
    }

}
