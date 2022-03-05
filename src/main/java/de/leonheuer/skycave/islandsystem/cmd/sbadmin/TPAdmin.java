package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPAdmin {

    public TPAdmin(Player player, String @NotNull [] args) {
        if (args.length < 2) {
            player.sendMessage(Message.ADMIN_TP_SYNTAX.getString().get());
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

        if (args.length < 3) {
            player.teleport(island.getSpawn());
            player.sendMessage(Message.ADMIN_TP_SUCCESS.getString().get());
            return;
        }
        player.teleport(island.getCenterLocation());
        player.sendMessage(Message.ADMIN_TP_SUCCESS.getString().get());
    }
}
