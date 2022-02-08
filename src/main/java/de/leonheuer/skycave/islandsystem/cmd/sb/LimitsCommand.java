package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.entity.Player;

public class LimitsCommand {

    public LimitsCommand(Player player, String[] args) {
        Utils.getLimitGui().show(player);
    }

}
