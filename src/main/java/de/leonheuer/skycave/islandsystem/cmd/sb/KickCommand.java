package de.leonheuer.skycave.islandsystem.cmd.sb;

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

public class KickCommand {

    public KickCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Message.SB_SUBCOMMAND_KICK_SYNTAX.getString().get());
            return;
        }
        if (!Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            return;
        }

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));

        for (ProtectedRegion r : set.getRegions()) {
            Insel insel = new Insel(r.getId());
            if (!insel.isOwner(player.getUniqueId())) {
                player.sendMessage(Message.MISC_NOOWNER.getString().get());
                return;
            }

            Player other = Bukkit.getPlayerExact(args[1]);
            if (other == null || !player.canSee(other)) {
                player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                return;
            }
            if (player.getName().equalsIgnoreCase(other.getName())) {
                player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOYOU.getString().get());
                return;
            }
            if (insel.isMember(other.getUniqueId())) {
                player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOMEMBER.getString().get());
                return;
            }
            if (!player.getLocation().getWorld().getName().equals(Utils.getInselWorld().getName())) {
                player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOONISLAND.getString().get());
                return;
            }

            boolean isKicked = false;
            ApplicableRegionSet set2 = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));

            for (ProtectedRegion r2 : set2.getRegions()) {
                if (r == r2) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + player.getName() + " Builder");
                    player.sendMessage(Message.SB_SUBCOMMAND_KICK_TOOTHER.getString().get());
                    isKicked = true;
                }
            }

            if (isKicked) {
                player.sendMessage(Message.SB_SUBCOMMAND_KICK_ERFOLG.getString().replace("{player}", args[1]).get());
            } else {
                player.sendMessage(Message.SB_SUBCOMMAND_KICK_NOONISLAND.getString().get());
            }
        }
    }

}
