package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.InselTP;
import org.bukkit.entity.Player;

import java.io.File;

public class TPAdminCommand {

    public TPAdminCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (args.length >= 3) {
                if (args[2].equalsIgnoreCase("zentrum")) {
                    String h = args[1];
                    if (args[1].startsWith("sc_")) {
                        File file = new File("plugins/SkyBeeIslandSystem/insel/", h + ".yml");
                        if (file.exists() && file.isFile()) {
                            player.teleport(new InselTP(h).getZentrum());
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
                        } else {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
                        }
                    } else {
                        if (args[1].length() == 1) {
                            h = "00" + h;
                        } else if (args[1].length() == 2) {
                            h = "0" + h;
                        }
                        File file = new File("plugins/SkyBeeIslandSystem/insel/", "sc_" + h + ".yml");
                        if (file.exists() && file.isFile()) {
                            player.teleport(new InselTP("sc_" + h).getZentrum());
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
                        } else {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
                        }
                    }
                }
            } else {
                if (args[1].startsWith("sc_")) {
                    String h = args[1];
                    File file = new File("plugins/SkyBeeIslandSystem/insel/", h + ".yml");
                    if (file.exists() && file.isFile()) {
                        player.teleport(new InselTP(h).getTP());
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
                    } else {
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
                    }
                } else {
                    String h = args[1];
                    if (args[1].length() == 1) {
                        h = "00" + h;
                    } else if (args[1].length() == 2) {
                        h = "0" + h;
                    }
                    File file = new File("plugins/SkyBeeIslandSystem/insel/", "sc_" + h + ".yml");
                    if (file.exists() && file.isFile()) {
                        player.teleport(new InselTP("sc_" + h).getTP());
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_ERFOLG.getString().get());
                    } else {
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_NOEXIST.getString().get());
                    }
                }
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_TP_SYNTAX.getString().get());
        }
    }
}
