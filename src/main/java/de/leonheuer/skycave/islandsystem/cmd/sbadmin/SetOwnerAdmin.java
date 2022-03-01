package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetOwnerAdmin {

    public SetOwnerAdmin(Player player, String[] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETOWNER_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() == main.getIslandWorld()) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
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

        OfflinePlayer other = Bukkit.getOfflinePlayerIfCached(args[1]);
        if (other == null) {
            player.sendMessage(Message.PLAYER_NOFOUND.getString().replace("{player}", args[1]).get());
            return;
        }
        UUID uuid = other.getUniqueId();

        if (region.getOwners().contains(uuid)) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETOWNER_ALREADY.getString().replace("{player}", args[1]).get());
            return;
        }

        region.getOwners().removeAll();
        region.getOwners().addPlayer(uuid);
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETOWNER_ERFOLG.getString().replace("{player}", args[1]).get());
    }
}