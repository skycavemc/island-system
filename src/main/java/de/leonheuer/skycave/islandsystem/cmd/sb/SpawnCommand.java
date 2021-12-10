package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.models.InselTP;
import org.bukkit.entity.Player;

public class SpawnCommand {

    public SpawnCommand(Player player, String[] args) {
        player.teleport(new InselTP("sc_spawn").getSpawnInsel());
    }
}
