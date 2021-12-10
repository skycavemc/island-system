package de.leonheuer.skycave.islandsystem.cmd;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.cmd.sbadmin.*;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SBAdminCommand implements TabExecutor {

    private final IslandSystem main;

    public SBAdminCommand(IslandSystem main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "create" -> new CreateAdminCommand(player, args, main);
                case "setowner" -> new SetownerAdminCommand(player, args);
                case "setspawn" -> new SetspawnAdminCommand(player, args);
                case "tp" -> new TPAdminCommand(player, args);
                case "trust" -> new TrustAdminCommand(player, args);
                case "untrust" -> new UntrustAdminCommand(player, args);
                case "setradius" -> new SetradiusAdminCommand(player, args);
                case "setwarp" -> new SetWarpAdminCommand(player, args, main);
                case "delwarp" -> new DelWarpAdminCommand(player, args, main);
                case "info" -> new InfoAdminCommand(player, args);
                default -> sendHelp(player);
            }
        } else {
            sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Message.CMD_SBADMIN_HELP_TITEL.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_CREATE.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_SETOWNER.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_SETRADIUS.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_TRUST.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_UNTRUST.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_SETSPAWN.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_INFO.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_TP.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_SETWARP.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_DELWARP.getString().get(false));
        player.sendMessage(Message.CMD_SBADMIN_HELP_ENDTITEL.getString().get(false));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("create");
            arguments.add("setowner");
            arguments.add("setspawn");
            arguments.add("tp");
            arguments.add("trust");
            arguments.add("untrust");
            arguments.add("setradius");
            arguments.add("setwarp");
            arguments.add("delwarp");
            arguments.add("info");

            StringUtil.copyPartialMatches(args[0], arguments, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create", "setowner", "trust", "untrust" -> {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(other)) {
                            arguments.add(player.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], arguments, completions);
                }
                case "setwarp", "delwarp" -> StringUtil.copyPartialMatches(args[1], main.getWarpsConfig().getWarps(), completions);
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                arguments.add("Eis");
                arguments.add("Blume");
                arguments.add("Pilz");
                arguments.add("Wüste");

                StringUtil.copyPartialMatches(args[3], arguments, completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}