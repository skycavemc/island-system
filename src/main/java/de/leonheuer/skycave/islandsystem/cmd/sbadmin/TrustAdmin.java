package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TrustAdmin {

    public TrustAdmin(Player player, String[] args) {
        if (args.length >= 2) {
            if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
                ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
                if (r == null) {
                    player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                    return;
                }

                Insel insel = new Insel(r.getId());
                Player other = Bukkit.getPlayerExact(args[1]);
                if (other == null) {
                    player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                    return;
                }

                if (player.canSee(other)) {
                    if (!insel.isOwner(player.getUniqueId())) {
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
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_TRUST_SYNTAX.getString().get());
        }
    }
}
