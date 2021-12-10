package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    private final IslandSystem main;

    public CreatureSpawnListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getLocation().getWorld() != Utils.getInselWorld()) {
            return;
        }

        ProtectedRegion islandRegion = Utils.getIslandRegionAt(event.getEntity().getLocation());
        if (islandRegion == null) {
            return;
        }

        int limit = EntityLimit.getLimitByType(event.getEntityType());
        if (limit == -1) {
            return;
        }

        main.getLimitManager().incEntityCount(islandRegion, event.getEntityType());
        int count = main.getLimitManager().getEntityCount(islandRegion, event.getEntityType());
        if (count >= limit) {
            event.setCancelled(true);
            main.getLimitManager().decEntityCount(islandRegion, event.getEntityType());
        }
    }

}
