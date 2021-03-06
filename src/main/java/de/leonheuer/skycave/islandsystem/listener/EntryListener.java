package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.Islands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class EntryListener implements Listener {

    private final IslandSystem main;

    public EntryListener(IslandSystem main) {
        this.main = main;
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
        Island island = Islands.at(event.getTo());
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

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        teleportSpawn(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerJoinEvent event) {
        teleportSpawn(event.getPlayer());
    }

    private void teleportSpawn(@NotNull Player player) {
        if (player.hasPermission("skycave.island.bypass.ban")) {
            return;
        }
        if (player.getLocation().getWorld() != main.getIslandWorld()) {
            return;
        }
        Island island = Islands.at(player.getLocation());
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

}
