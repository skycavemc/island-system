package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamCommand {

    public TeamCommand(Player player, IslandSystem main) {
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

        if (!region.getMembers().contains(player.getUniqueId()) && !region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_NOMEMBER.getString().replace("{nummer}", "" + island.getId()).get());
            return;
        }

        player.sendMessage(Message.SB_SUBCOMMAND_TEAM_TITEL.getString().replace("{nummer}", "" + island.getId()).get(false));
        player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER.getString().get(false));
        for (String owner: region.getOwners().getPlayers()) {
            Player p = Bukkit.getPlayerExact(owner);
            if (p == null || !p.isOnline()) {
                player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER_LIST_OFF.getString().replace("{player}", owner).get(false));
                continue;
            }
            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER_LIST_ON.getString().replace("{player}", owner).get(false));
        }

        player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER.getString().get(false));
        for (String member: region.getMembers().getPlayers()) {
            Player p = Bukkit.getPlayerExact(member);
            if (p == null || !p.isOnline()) {
                player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER_LIST_OFF.getString().replace("{player}", member).get(false));
                continue;
            }
            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER_LIST_ON.getString().replace("{player}", member).get(false));
        }
        player.sendMessage("\n");
    }
}
