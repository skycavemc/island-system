package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private final IslandSystem main;

    public EntityDeathListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getWorld() != Utils.getInselWorld()) {
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
        main.getLimitManager().decEntityCount(islandRegion, event.getEntityType());
    }

}
