package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InfoAdminCommand {

    public InfoAdminCommand(Player player, IslandSystem main) {
        if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(Utils.getInselWorld()));
            if (rm == null || player.getLocation().getWorld() != Utils.getInselWorld()) {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
                return;
            }

            ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getParent() == null) {
                    if (r.getId().equalsIgnoreCase("sc_spawn")) {
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_SPAWN.getString().get());
                    } else {
                        Insel insel = new Insel(r.getId());
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_TITEL.getString().replace("{nummer}", r.getId().replace("sc_", "")).get(false));

                        StringBuilder message = new StringBuilder();
                        int countMember = 0;
                        for (String b : Utils.getInsel(r.getId()).getStringList("member")) {
                            message.append(Bukkit.getOfflinePlayer(UUID.fromString(b)).getName()).append("&8, &b");
                            ++countMember;
                        }

                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_OWNER.getString().replace("{player}", Bukkit.getOfflinePlayer(insel.getOwner()).getName()).get(false));

                        if (countMember == 1) {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_MEMBER.getString().get(false));
                        } else {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_MEMBER.getString()
                                    .replace("{member}", message.substring(0, (message.length() - 2))).get(false));
                        }
                    }
                }
            }
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }

}
