package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand {

    public WarpCommand(Player player, String @NotNull [] args, IslandSystem main) {
        if (args.length >= 2) {
            Location loc = main.getWarpManager().get(args[1].toLowerCase());
            if (loc == null) {
                player.sendMessage(Message.WARP_UNKNOWN.getString().replace("{warp}", args[1]).get());
            } else {
                player.teleport(loc);
                player.sendMessage(Message.WARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            }
        } else {
            player.sendMessage(Message.WARP_MISSING.getString()
                    .replace("{warps}", String.join("§c, §a", main.getWarpManager().getNames()))
                    .get()
            );
        }
    }

}
