package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelWarpAdmin {

    public DelWarpAdmin(Player player, String @NotNull [] args, IslandSystem main) {
        if (args.length >= 2) {
            if (main.getWarpManager().exists(args[1].toLowerCase())) {
                main.getWarpManager().remove(args[1].toLowerCase());
                player.sendMessage(Message.ADMIN_DELWARP_SUCCESS.getString().replace("{warp}", args[1]).get());
            } else {
                player.sendMessage(Message.ADMIN_DELWARP_UNKNOWN.getString().replace("{warp}", args[1]).get());
            }
        } else {
            player.sendMessage(Message.ADMIN_DELWARP_MISSING.getString()
                    .replace("{warps}", String.join("§c, §7", main.getWarpManager().getNames()))
                    .get()
            );
        }
    }

}
