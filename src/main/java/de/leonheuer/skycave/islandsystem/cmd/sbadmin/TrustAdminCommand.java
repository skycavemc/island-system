package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TrustAdminCommand {

    public TrustAdminCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 2) {
            if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
                RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(Utils.getInselWorld()));
                if (rm == null || player.getLocation().getWorld() != Utils.getInselWorld()) {
                    player.sendMessage(Message.MISC_NOINWORLD.getString().get());
                    return;
                }

                ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
                for (ProtectedRegion r : set.getRegions()) {
                    if (r.getParent() == null) {
                        Insel insel = new Insel(r.getId());
                        Player other = Bukkit.getPlayer(args[1]);
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
                    }
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_TRUST_SYNTAX.getString().get());
        }
    }
}
