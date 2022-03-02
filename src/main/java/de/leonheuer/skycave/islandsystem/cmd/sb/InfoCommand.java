package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class InfoCommand {

    public InfoCommand(Player player, IslandSystem main) {
        if (player.getLocation().getWorld() == main.getIslandWorld()) {
            player.sendMessage(Message.NOT_IN_WORLD.getString().get());
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

        StringJoiner owners = new StringJoiner("&8, &b");
        for (String owner: region.getOwners().getPlayers()) {
            owners.add(owner);
        }

        player.sendMessage(Message.SB_SUBCOMMAND_INFO.getString()
                .replace("{id}", "" + island.getId()).replace("{owners}", owners.toString()).get());
    }
}
