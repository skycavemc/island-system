package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EntryListener implements Listener {

    private final IslandSystem main;

    public EntryListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skycave.island.bypass.ban")) {
            return;
        }
        if (player.getLocation().getWorld() != main.getIslandWorld()) {
            return;
        }
        Island island = Island.at(player.getLocation());
        if (island == null) {
            return;
        }
        ProtectedRegion region = island.getRegion();
        if (region == null) {
            return;
        }
        if (island.getBannedPlayers().contains(player.getUniqueId())) {
            player.teleport(main.getServerSpawn());
            player.sendMessage(Message.BAN_ENTRY.getString().replace("{id}", "" + island.getId()).get());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skycave.island.bypass.ban")) {
            return;
        }
        if (event.getTo().getWorld() != main.getIslandWorld()) {
            return;
        }
        Island island = Island.at(event.getTo());
        if (island == null) {
            return;
        }
        ProtectedRegion region = island.getRegion();
        if (region == null) {
            return;
        }
        if (island.getBannedPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Message.BAN_ENTRY.getString().replace("{id}", "" + island.getId()).get());
        }
    }

}
