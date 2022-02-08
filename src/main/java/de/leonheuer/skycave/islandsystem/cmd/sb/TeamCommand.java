package de.leonheuer.skycave.islandsystem.cmd.sb;

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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class TeamCommand {

    public TeamCommand(Player player, IslandSystem main) {
        if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
            if (rm == null) {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
                return;
            }

            Set<ProtectedRegion> regions = rm.getApplicableRegions(
                    BukkitAdapter.asBlockVector(player.getLocation())
            ).getRegions();
            for (ProtectedRegion r : regions) {
                if (r.getParent() == null) {
                    if (r.getId().equalsIgnoreCase("sc_spawn")) {
                        player.sendMessage(Message.SB_SUBCOMMAND_INFO_SPAWN.getString().get());
                    } else {
                        Insel insel = new Insel(r.getId());
                        if (insel.isOwner(player.getUniqueId()) || insel.isMember(player.getUniqueId())) {
                            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_TITEL.getString().replace("{nummer}", r.getId().replace("sc_", "")).get(false));

                            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER.getString().get(false));
                            OfflinePlayer owner = Bukkit.getOfflinePlayer(insel.getOwner());
                            if (owner.isOnline()) {
                                player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER_LIST_ON.getString().replace("{player}", owner.getName()).get(false));
                            } else {
                                player.sendMessage(Message.SB_SUBCOMMAND_TEAM_OWNER_LIST_OFF.getString().replace("{player}", owner.getName()).get(false));
                            }

                            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER.getString().get(false));

                            for (String b : Utils.getInsel(r.getId()).getStringList("member")) {
                                OfflinePlayer listedMember = Bukkit.getOfflinePlayer(UUID.fromString(b));
                                if (listedMember.isOnline()) {
                                    player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER_LIST_ON.getString().replace("{player}", listedMember.getName()).get(false));
                                } else {
                                    player.sendMessage(Message.SB_SUBCOMMAND_TEAM_MEMBER_LIST_OFF.getString().replace("{player}", listedMember.getName()).get(false));
                                }
                            }

                            player.sendMessage("\n");
                        } else {
                            player.sendMessage(Message.SB_SUBCOMMAND_TEAM_NOMEMBER.getString().replace("{nummer}", r.getId().replace("sc_", "")).get());
                        }
                    }
                }
            }
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }
}
