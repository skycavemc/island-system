package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;

public class TPCommand {

    public TPCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Message.TP_SYNTAX.getString().get());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INVALID_NUMBER.getString().get());
            return;
        }

        Island island = Island.load(id);
        if (island == null) {
            player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
            return;
        }

        ProtectedRegion region = island.getRegion();
        if (region == null) {
            player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
            return;
        }

        if (!region.getMembers().contains(player.getUniqueId()) && !region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(Message.NO_MEMBER.getString().get());
            return;
        }
        player.teleport(island.getSpawn());
        player.sendMessage(Message.TP_SUCCESS.getString().get());
    }
}
