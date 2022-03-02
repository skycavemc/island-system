package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;

public class SetSpawnCommand {

    public SetSpawnCommand(Player player, IslandSystem main) {
        if (player.getLocation().getWorld() == main.getIslandWorld()) {
            player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            return;
        }

        Island island = Island.at(player.getLocation());
        if (island == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        ProtectedRegion region = island.getRegion();
        if (region == null) {
            player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
            return;
        }

        if (!region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(Message.MISC_NOOWNER.getString().get());
            return;
        }

        player.sendMessage(Message.SB_SUBCOMMAND_SETSPAWN_ERFOLG.getString().get());
        island.setSpawn(player.getLocation());
    }
}
