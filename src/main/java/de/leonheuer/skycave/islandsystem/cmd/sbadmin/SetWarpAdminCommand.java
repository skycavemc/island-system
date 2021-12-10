package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;

public class SetWarpAdminCommand {

    public SetWarpAdminCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 2) {
            if (main.getWarpsConfig().isSet(args[1].toLowerCase())) {
                main.getWarpsConfig().removeWarp(args[1].toLowerCase());
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_OVERRIDE.getString().replace("{warp}", args[1]).get());
            } else {
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            }
            main.getWarpsConfig().setWarp(args[1].toLowerCase(), player.getLocation());
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_MISSING.getString().get());
        }
    }

}
