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

public class InfoCommand {

    public InfoCommand(Player player, String[] args) {
        if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getParent() == null) {
                    if (r.getId().equalsIgnoreCase("sc_spawn")) {
                        player.sendMessage(Message.SB_SUBCOMMAND_INFO_SPAWN.getString().get());
                    } else {
                        Insel insel = new Insel(r.getId());
                        player.sendMessage(Message.SB_SUBCOMMAND_INFO.getString()
                                .replace("{nummer}", r.getId().replace("sc_", ""))
                                .replace("{player}", Bukkit.getOfflinePlayer(insel.getOwner()).getName())
                                .get()
                        );
                    }
                }
            }
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }
}
