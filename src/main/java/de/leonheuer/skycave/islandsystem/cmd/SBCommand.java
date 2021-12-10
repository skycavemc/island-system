package de.leonheuer.skycave.islandsystem.cmd;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.cmd.sb.*;
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

public class SBCommand implements TabExecutor {

    private final IslandSystem main;

    public SBCommand(IslandSystem main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "buy" -> new BuyCommand(player, args);
                case "trust" -> new TrustCommand(player, args);
                case "untrust" -> new UntrustCommand(player, args);
                case "setspawn" -> new SetspawnCommand(player, args);
                case "tp" -> new TPCommand(player, args);
                case "spawn" -> new SpawnCommand(player, args);
                case "team" -> new TeamCommand(player, args);
                case "info" -> new InfoCommand(player, args);
                case "list" -> new ListCommand(player, args);
                case "kick" -> new KickCommand(player, args);
                case "warp" -> new WarpCommand(player, args, main);
                case "limits" -> new LimitsCommand(player, args);
                default -> sendHelp(player);
            }
        } else {
            sendHelp(player);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Message.CMD_SB_HELP_TITEL.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_BUY.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_TRUST.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_UNTRUST.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_LIST.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_TEAM.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_INFO.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_SETSPAWN.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_TP.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_SPAWN.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_WARP.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_ENDTITEL.getString().get(false));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("buy");
            arguments.add("trust");
            arguments.add("untrust");
            arguments.add("list");
            arguments.add("team");
            arguments.add("info");
            arguments.add("setspawn");
            arguments.add("tp");
            arguments.add("spawn");
            arguments.add("warp");
            arguments.add("limits");

            StringUtil.copyPartialMatches(args[0], arguments, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "trust":
                case "untrust":
                case "kick":
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(other)) {
                            arguments.add(player.getName());
                        }
                    }
                    break;
                case "warp":
                    StringUtil.copyPartialMatches(args[1], main.getWarpsConfig().getWarps(), completions);
                    break;
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
