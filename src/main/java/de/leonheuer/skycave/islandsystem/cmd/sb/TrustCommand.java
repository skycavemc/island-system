package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TrustCommand {

    public TrustCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
                ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
                if (r == null) {
                    player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                    return;
                }

                Insel insel = new Insel(r.getId());
                if (insel.isOwner(player.getUniqueId())) {
                    Player other = Bukkit.getPlayerExact(args[1]);
                    if (other != null && player.canSee(other)) {
                        if (!player.getName().equalsIgnoreCase(other.getName())) {
                            if (!insel.isMember(other.getUniqueId())) {
                                insel.addMember(other.getUniqueId());
                                player.sendMessage(Message.SB_SUBCOMMAND_TRUST_ERFOLG.getString().replace("{player}", args[1]).get());
                            } else {
                                player.sendMessage(Message.SB_SUBCOMMAND_TRUST_BEREITS.getString().get());
                            }
                        } else {
                            player.sendMessage(Message.SB_SUBCOMMAND_TRUST_NOYOU.getString().get());
                        }
                    } else {
                        player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                    }
                } else {
                    player.sendMessage(Message.MISC_NOOWNER.getString().get());
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_TRUST_SYNTAX.getString().get());
        }
    }
}
