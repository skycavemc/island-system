package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.entity.Player;

public class SetSpawnAdmin {

    public SetSpawnAdmin(Player player) {
        if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
            ProtectedRegion r = Utils.getIslandRegionAt(player.getLocation());
            if (r == null) {
                player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                return;
            }

            Insel insel = new Insel(r.getId());
            insel.setTP(player.getLocation());
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETSPAWN_ERFOLG.getString().get());
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }

}
