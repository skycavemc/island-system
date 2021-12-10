package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;

public class DelWarpAdminCommand {

    public DelWarpAdminCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 2) {
            if (main.getWarpsConfig().isSet(args[1].toLowerCase())) {
                main.getWarpsConfig().removeWarp(args[1].toLowerCase());
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_DELWARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            } else {
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_DELWARP_NOEXIST.getString().replace("{warp}", args[1]).get());
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_DELWARP_MISSING.getString()
                    .replace("{warps}", String.join("§c, §7", main.getWarpsConfig().getWarps()))
                    .get()
            );
        }
    }

}
