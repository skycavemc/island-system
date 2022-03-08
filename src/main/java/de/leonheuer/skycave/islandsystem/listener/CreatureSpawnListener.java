package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Set;
import java.util.UUID;

public class CreatureSpawnListener implements Listener {

    private final IslandSystem main;

    public CreatureSpawnListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        World world = event.getLocation().getWorld();
        if (!world.getName().equals("skybeeisland")) {
            return;
        }

        EntityLimit limit = EntityLimit.getLimitByType(event.getEntityType());
        if (limit == null) {
            return;
        }

        ProtectedRegion region = Utils.getIslandRegionAt(event.getLocation());
        if (region == null) {
            return;
        }

        LimitManager lm = main.getLimitManager();
        int count = lm.getEntityCount(region.getId(), event.getEntityType());
        if (count >= limit.getLimit()) {
            event.setCancelled(true);
            for (UUID uuid : region.getOwners().getUniqueIds()) {
                sendLimitMessage(uuid, limit, region);
            }
            for (UUID uuid : region.getMembers().getUniqueIds()) {
                sendLimitMessage(uuid, limit, region);
            }
        } else {
            lm.addToMap(lm.getEntityCountMap(), region.getId(), event.getEntityType());
        }
    }

    private void sendLimitMessage(UUID uuid, EntityLimit limit, ProtectedRegion region) {
        /*Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            return;
        }
        player.sendMessage(Message.LIMIT_REACHED.getString()
                .replace("{count}", "" + limit.getLimit())
                .replace("{entity}", Utils.entityTypeToString(limit.getType()))
                .replace("{id}", region.getId()).replace("sc_", "").get());*/
    }

}
