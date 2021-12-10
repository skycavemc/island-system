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

public class UntrustAdminCommand {

    @SuppressWarnings("deprecation")
    public UntrustAdminCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
                RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
                for (ProtectedRegion r : set.getRegions()) {
                    if (r.getParent() == null) {
                        Insel insel = new Insel(r.getId());
                        UUID offlineUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                        if (insel.isMember(offlineUUID)) {
                            insel.removeMember(offlineUUID);
                            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_ERFOLG.getString().replace("{player}", args[1]).get());
                        } else {
                            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_BEREITS.getString().get());
                        }
                    }
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_SYNTAX.getString().get());
        }
    }
}
