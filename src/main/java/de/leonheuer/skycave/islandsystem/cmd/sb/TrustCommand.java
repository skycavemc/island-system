package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TrustCommand {

    public TrustCommand(Player player, String @NotNull [] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.UNTRUST_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() != main.getIslandWorld()) {
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
            player.sendMessage(Message.TRUST_SELF.getString().get());
            return;
        }
        if (region.getMembers().contains(uuid) || region.getOwners().contains(uuid)) {
            player.sendMessage(Message.TRUST_ALREADY.getString().get());
            return;
        }
        if (island.getBannedPlayers().contains(uuid)) {
            player.sendMessage(Message.TRUST_BANNED.getString().get());
            return;
        }

        region.getMembers().addPlayer(uuid);
        player.sendMessage(Message.TRUST_SUCCESS.getString().replace("{player}", args[1]).get());
    }
}
