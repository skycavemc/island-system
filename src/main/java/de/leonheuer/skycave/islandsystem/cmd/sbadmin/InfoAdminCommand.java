package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InfoAdminCommand {

    public InfoAdminCommand(Player player, String[] args) {
        if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getParent() == null) {
                    if (r.getId().equalsIgnoreCase("sc_spawn")) {
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_SPAWN.getString().get());
                    } else {
                        Insel insel = new Insel(r.getId());
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_TITEL.getString().replace("{nummer}", r.getId().replace("sc_", "")).get(false));

                        StringBuilder sb = new StringBuilder();
                        int countMember = 0;
                        for (String b : Utils.getInsel(r.getId()).getStringList("member")) {
                            sb.append(Bukkit.getOfflinePlayer(UUID.fromString(b)).getName() + "&8, &b");
                            ++countMember;
                        }

                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_OWNER.getString().replace("{player}", Bukkit.getOfflinePlayer(insel.getOwner()).getName()).get(false));

                        if (countMember == 1) {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_MEMBER.getString().get(false));
                        } else {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_MEMBER.getString().replace("{member}", sb.substring(0, (sb.length() - 2))).get(false));
                        }
                    }
                }
            }
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }

}
