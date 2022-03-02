package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetWarpAdmin {

    public SetWarpAdmin(Player player, String @NotNull [] args, IslandSystem main) {
        if (args.length >= 2) {
            if (main.getWarpManager().exists(args[1].toLowerCase())) {
                main.getWarpManager().remove(args[1].toLowerCase());
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_OVERRIDE.getString().replace("{warp}", args[1]).get());
            } else {
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            }
            main.getWarpManager().set(args[1].toLowerCase(), player.getLocation());
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETWARP_MISSING.getString().get());
        }
    }

}
