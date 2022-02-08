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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UntrustAdminCommand {

    public UntrustAdminCommand(Player player, String[] args, IslandSystem main) {
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
