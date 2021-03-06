package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.Islands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BanCommand {

    public BanCommand(@NotNull Player player, String @NotNull [] args, @NotNull IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.BAN_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() != main.getIslandWorld()) {
            player.sendMessage(Message.NOT_IN_WORLD.getString().get());
            return;
        }

        Island island = Islands.at(player.getLocation());
        if (island == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        ProtectedRegion region = island.getRegion();
        if (region == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        if (!region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(Message.NO_OWNER.getString().get());
            return;
        }

        OfflinePlayer other = Bukkit.getOfflinePlayerIfCached(args[1]);
        if (other == null) {
            player.sendMessage(Message.PLAYER_UNKNOWN.getString().replace("{player}", args[1]).get());
            return;
        }
        UUID uuid = other.getUniqueId();

        if (player.getUniqueId() == uuid) {
            player.sendMessage(Message.BAN_SELF.getString().get());
            return;
        }
        if (region.getMembers().contains(uuid) || region.getOwners().contains(uuid)) {
            player.sendMessage(Message.BAN_MEMBER.getString().get());
            return;
        }
        if (island.getBannedPlayers().contains(uuid)) {
            player.sendMessage(Message.BAN_ALREADY.getString().get());
            return;
        }

        island.getBannedPlayers().add(uuid);
        Player bannedPlayer = other.getPlayer();
        if (bannedPlayer != null && bannedPlayer.isOnline() &&
                region.contains(BukkitAdapter.asBlockVector(bannedPlayer.getLocation()))
        ) {
            bannedPlayer.teleport(main.getServerSpawn());
            bannedPlayer.sendMessage(Message.BAN_ALERT.getString()
                    .replace("{player}", player.getName()).replace("{id}", "" + island.getId()).get());
        }
        player.sendMessage(Message.BAN_SUCCESS.getString().replace("{player}", args[1]).get());
    }

}
