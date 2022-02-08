package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.entity.Player;

import java.util.Set;

public class SetspawnCommand {

    public SetspawnCommand(Player player, IslandSystem main) {
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
                    insel.setTP(player.getLocation());
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETSPAWN_ERFOLG.getString().get());
                } else {
                    player.sendMessage(Message.MISC_NOOWNER.getString().get());
                }
            }
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }
}
