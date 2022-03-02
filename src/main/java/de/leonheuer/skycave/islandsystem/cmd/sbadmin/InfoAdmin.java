package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class InfoAdmin {

    public InfoAdmin(@NotNull Player player, @NotNull IslandSystem main) {
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

        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_TITEL.getString()
                .replace("{nummer}", "" + island.getId()).get(false));

        StringJoiner owners = new StringJoiner("&8, &b");
        for (String owner: region.getOwners().getPlayers()) {
            owners.add(owner);
        }
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_OWNER.getString()
                .replace("{owner}", owners.toString()).get(false));

        StringJoiner members = new StringJoiner("&8, &b");
        for (String member: region.getMembers().getPlayers()) {
            members.add(member);
        }
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_MEMBER.getString()
                .replace("{member}", members.toString()).get(false));

        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_RADIUS.getString()
                .replace("{radius}", "" + island.getRadius()).get());
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_SPAWN_LOC.getString()
                .replace("{spawn}", Utils.locationAsString(island.getSpawn())).get());
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_CENTER_LOC.getString()
                .replace("{center}", Utils.locationAsString(island.getCenterLocation())).get());
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_START_LOC.getString().replace("{start}",
                Utils.locationAsString(island.getSpiralLocation().getStartVector(island.getRadius()))).get());
        player.sendMessage(Message.SBADMIN_SUBCOMMAND_INFO_END_LOC.getString().replace("{end}",
                Utils.locationAsString(island.getSpiralLocation().getEndVector(island.getRadius()))).get());
    }

}
