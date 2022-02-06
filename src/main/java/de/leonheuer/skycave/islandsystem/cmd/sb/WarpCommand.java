package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpCommand {

    public WarpCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 2) {
            Location loc = main.getWarpsConfig().getWarp(args[1].toLowerCase());
            if (loc == null) {
                player.sendMessage(Message.SB_SUBCOMMAND_WARP_NOEXIST.getString().replace("{warp}", args[1]).get());
            } else {
                player.teleport(loc);
                player.sendMessage(Message.SB_SUBCOMMAND_WARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_WARP_MISSING.getString()
                    .replace("{warps}", String.join("§c, §a", main.getWarpsConfig().getWarps()))
                    .get()
            );
        }
    }

}
