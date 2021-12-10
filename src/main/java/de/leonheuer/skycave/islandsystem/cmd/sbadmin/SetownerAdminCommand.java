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

public class SetownerAdminCommand {


    public SetownerAdminCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
                RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
                for (ProtectedRegion r : set.getRegions()) {
                    if (r.getParent() == null) {
                        Insel insel = new Insel(r.getId());
                        Player other = Bukkit.getPlayerExact(args[1]);
                        if (other != null && player.canSee(other)) {
                            insel.setOwner(other.getUniqueId());
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETOWNER_ERFOLG.getString().replace("{player}", args[1]).get());
                        } else {
                            player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                        }
                    }
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETOWNER_SYNTAX.getString().get());
        }
    }
}