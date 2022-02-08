package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UntrustCommand {

    public UntrustCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
                ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
                if (r == null) {
                    player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                    return;
                }

                Insel insel = new Insel(r.getId());
                if (insel.isOwner(player.getUniqueId())) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (offlinePlayer == null) {
                        player.sendMessage(Message.PLAYER_NOFOUND.getString().replace("{player}", args[1]).get());
                        return;
                    }

                    if (!(player.getName().equalsIgnoreCase(args[1]))) {
                        if (insel.isMember(offlinePlayer.getUniqueId())) {
                            insel.removeMember(offlinePlayer.getUniqueId());
                            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_ERFOLG.getString().replace("{player}", args[1]).get());
                        } else {
                            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_BEREITS.getString().get());
                        }
                    } else {
                        player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_NOYOU.getString().get());
                    }
                } else {
                    player.sendMessage(Message.MISC_NOOWNER.getString().get());
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_SYNTAX.getString().get());
        }
    }
}
