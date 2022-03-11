package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.UUID;

public class InfoCommand {

    public InfoCommand(@NotNull Player player, @NotNull String[] args, @NotNull IslandSystem main, boolean admin) {
        if (player.getLocation().getWorld() != main.getIslandWorld()) {
            player.sendMessage(Message.NOT_IN_WORLD.getString().get());
            return;
        }

        Island island;
        ProtectedRegion region;

        if (admin && args.length >= 2) {
            int id;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.INVALID_NUMBER.getString().get());
                return;
            }

            island = Island.load(id);
            if (island == null) {
                player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                return;
            }

            region = island.getRegion();
            if (region == null) {
                player.sendMessage(Message.ISLAND_UNKNOWN.getString().get());
                return;
            }
        } else {
            island = Island.at(player.getLocation());
            if (island == null) {
                player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                return;
            }

            region = island.getRegion();
            if (region == null) {
                player.sendMessage(Message.NOT_ON_ISLAND.getString().get());
                return;
            }
        }

        player.sendMessage(Message.INFO_HEADER.getString()
                .replace("{nummer}", "" + island.getId()).get(false));
        player.sendMessage(Message.INFO_OWNER.getString()
                .replace("{owner}", uuidsToPlayers(region.getOwners().getUniqueIds())).get(false));
        player.sendMessage(Message.INFO_MEMBER.getString()
                .replace("{member}", uuidsToPlayers(region.getMembers().getUniqueIds())).get(false));
        player.sendMessage(Message.INFO_BANS.getString()
                .replace("{players}", uuidsToPlayers(island.getBannedPlayers().getUniqueIds())).get(false));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
        player.sendMessage(Message.INFO_CREATION.getString()
                .replace("{creation}", dtf.format(island.getCreated())).get(false));
        player.sendMessage(Message.INFO_TEMPLATE.getString()
                .replace("{type}", island.getTemplate().getAlternativeName()).get(false));
        player.sendMessage(Message.INFO_RADIUS.getString()
                .replace("{radius}", "" + island.getRadius()).get(false));
        player.sendMessage(Message.INFO_SPAWN.getString()
                .replace("{spawn}", Utils.locationAsString(island.getSpawn())).get(false));
        player.sendMessage(Message.INFO_CENTER.getString()
                .replace("{center}", Utils.locationAsString(island.getCenterLocation())).get(false));
        player.sendMessage(Message.INFO_START.getString().replace("{start}",
                Utils.locationAsString(island.getSpiralLocation().getStartVector(island.getRadius()))).get(false));
        player.sendMessage(Message.INFO_END.getString().replace("{end}",
                Utils.locationAsString(island.getSpiralLocation().getEndVector(island.getRadius()))).get(false));
    }

    private String uuidsToPlayers(@NotNull Collection<UUID> uuidSet) {
        int i = 0;
        StringJoiner players = new StringJoiner("&8, &b");
        for (UUID uuid : uuidSet) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.getName() == null) {
                continue;
            }
            players.add(player.getName());
            i++;
        }

        String result;
        if (i == 0) {
            result = "&r&7keine";
        } else {
            result = players.toString();
        }

        return result;
    }

}
