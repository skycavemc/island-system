package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
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

public class UntrustCommand {

    public UntrustCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 2) {
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
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_UNTRUST_SYNTAX.getString().get());
        }
    }
}
