package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnCommand {

    public SpawnCommand(Player player) {
        player.teleport(new Location(Utils.ISLAND_WORLD, 0d, 67d, 0d, 0f, 1f));
    }
}
