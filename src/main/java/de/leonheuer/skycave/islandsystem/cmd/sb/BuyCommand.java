package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BuyCommand {

    public BuyCommand(Player player) {
        for (String str : Utils.game.getStringList("info.kaufen")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
        }
    }
}
