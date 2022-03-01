package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;

public class SetSpawnAdmin {

    public SetSpawnAdmin(Player player) {
        if (player.getLocation().getWorld().getName().equals("skybeeisland")) {
            Island island = Island.at(player.getLocation());
            if (island == null) {
                player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                return;
            }

            island.setSpawn(player.getLocation());
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETSPAWN_ERFOLG.getString().get());
        } else {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
        }
    }

}
