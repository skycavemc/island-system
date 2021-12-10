package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Insel;
import de.leonheuer.skycave.islandsystem.models.InselTP;
import org.bukkit.entity.Player;

import java.io.File;

public class TPCommand {

    public TPCommand(Player player, String[] args) {
        if (args.length >= 2) {
            String number = args[1];
            if (number.startsWith("sc_")) {
                File file = new File("plugins/SkyBeeIslandSystem/insel/", number + ".yml");
                if (file.exists() && file.isFile()) {
                    Insel insel = new Insel(number);
                    if (insel.isOwner(player.getUniqueId()) || insel.isMember(player.getUniqueId())) {
                        player.teleport(new InselTP(number).getTP());
                        player.sendMessage(Message.SB_SUBCOMMAND_TP_ERFOLG.getString().get());
                    } else {
                        player.sendMessage(Message.SB_SUBCOMMAND_TP_NOMEMBER.getString().get());
                    }
                } else {
                    player.sendMessage(Message.SB_SUBCOMMAND_TP_NOEXIST.getString().get());
                }
            } else {
                if (number.length() == 1) {
                    number = "00" + number;
                } else if (args[1].length() == 2) {
                    number = "0" + number;
                }
                File file = new File("plugins/SkyBeeIslandSystem/insel/", "sc_" + number + ".yml");
                if (file.exists() && file.isFile()) {
                    Insel insel = new Insel("sc_" + number);
                    if (insel.isOwner(player.getUniqueId()) || insel.isMember(player.getUniqueId())) {
                        player.teleport(new InselTP("sc_" + number).getTP());
                        player.sendMessage(Message.SB_SUBCOMMAND_TP_ERFOLG.getString().get());
                    } else {
                        player.sendMessage(Message.SB_SUBCOMMAND_TP_NOMEMBER.getString().get());
                    }
                } else {
                    player.sendMessage(Message.SB_SUBCOMMAND_TP_NOEXIST.getString().get());
                }
            }
        } else {
            player.sendMessage(Message.SB_SUBCOMMAND_TP_SYNTAX.getString().get());
        }
    }
}
