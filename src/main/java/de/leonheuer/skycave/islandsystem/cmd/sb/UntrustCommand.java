package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UntrustCommand {

    public UntrustCommand(Player player, String[] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() == main.getIslandWorld()) {
            player.sendMessage(Message.NOT_IN_WORLD.getString().get());
            return;
        }

        Island island = Island.at(player.getLocation());
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
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_NOYOU.getString().get());
            return;
        }
        if (!region.getMembers().contains(uuid)) {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_BEREITS.getString().get());
            return;
        }

        region.getMembers().removePlayer(uuid);
        player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_ERFOLG.getString().replace("{player}", args[1]).get());
    }
}
