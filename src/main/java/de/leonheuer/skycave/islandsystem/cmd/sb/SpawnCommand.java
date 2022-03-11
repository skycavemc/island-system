package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand {

    public SpawnCommand(@NotNull Player player, @NotNull IslandSystem main) {
        World world = main.getIslandWorld();
        if (world == null) {
            player.sendMessage(Message.ISLAND_WORLD_UNLOADED.getString().get());
            return;
        }
        player.teleport(new Location(world, 0, 65, 0, 0, 1));
        player.sendMessage(Message.SPAWN.getString().get());
    }
}
