package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetRadiusAdmin {

    public SetRadiusAdmin(Player player, String @NotNull [] args, IslandSystem main) {
        if (args.length < 2) {
            player.sendMessage(Message.ADMIN_SETRADIUS_SYNTAX.getString().get());
            return;
        }

        if (player.getLocation().getWorld() != main.getIslandWorld()) {
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

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INVALID_NUMBER.getString().get());
            return;
        }

        if (radius > 1500 || radius < 100) {
            player.sendMessage(Message.ADMIN_SETRADIUS_OUTOFRANGE.getString().get());
            return;
        }
        island.setRadius(radius);
        player.sendMessage(Message.ADMIN_SETRADIUS_ERFOLG.getString().get());
    }
}