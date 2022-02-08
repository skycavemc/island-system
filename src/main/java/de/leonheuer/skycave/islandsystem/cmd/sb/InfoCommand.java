package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InfoCommand {

    public InfoCommand(Player player) {
        if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
            ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
            if (r == null) {
                player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                return;
            }

            Insel insel = new Insel(r.getId());
            player.sendMessage(Message.SB_SUBCOMMAND_INFO.getString()
                    .replace("{nummer}", r.getId().replace("sc_", ""))
                    .replace("{player}", Bukkit.getOfflinePlayer(insel.getOwner()).getName())
                    .get()
            );
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }
}
