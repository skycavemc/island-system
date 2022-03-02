package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CreatureSpawnListener implements Listener {

    private final IslandSystem main;

    public CreatureSpawnListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(@NotNull CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        World world = event.getLocation().getWorld();
        if (!world.getName().equals("skybeeisland")) {
            return;
        }

        int limit = EntityLimit.getLimitByType(event.getEntityType());
        if (limit == -1) {
            return;
        }

        ProtectedRegion region = IslandUtils.getIslandRegionAt(event.getLocation());
        if (region == null) {
            return;
        }

        LimitManager lm = main.getLimitManager();
        int count = lm.getEntityCount(region.getId(), event.getEntityType());
        if (count >= limit) {
            event.setCancelled(true);
        } else {
            lm.addToMap(lm.getEntityCountMap(), region.getId(), event.getEntityType());
        }
    }

}
