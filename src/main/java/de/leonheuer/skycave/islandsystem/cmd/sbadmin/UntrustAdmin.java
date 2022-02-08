package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UntrustAdmin {

    public UntrustAdmin(Player player, String[] args) {
        if (args.length >= 2) {
            if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
                ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
                if (r == null) {
                    player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                    return;
                }

                Insel insel = new Insel(r.getId());
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (offlinePlayer == null) {
                    player.sendMessage(Message.PLAYER_NOFOUND.getString().replace("{player}", args[1]).get());
                    return;
                }

                if (insel.isMember(offlinePlayer.getUniqueId())) {
                    insel.removeMember(offlinePlayer.getUniqueId());
                    player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_ERFOLG.getString()
                            .replace("{player}", offlinePlayer.getName()).get());
                } else {
                    player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_BEREITS.getString().get());
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_SYNTAX.getString().get());
        }
    }
}
