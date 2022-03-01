package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TPAdmin {

    public TPAdmin(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_SYNTAX.getString().get());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
            return;
        }

        Island island = Island.load(id);
        if (island == null) {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
            return;
        }

        if (args.length < 3) {
            player.teleport(island.getSpawn());
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
            return;
        }
        player.teleport(island.getCenterLocation());
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
    }
}
