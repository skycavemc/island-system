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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class SBCommand implements TabExecutor {

    private final IslandSystem main;

    public SBCommand(IslandSystem main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "buy" -> new BuyCommand(player, main);
                case "trust" -> new TrustCommand(player, args, main);
                case "untrust" -> new UntrustCommand(player, args, main);
                case "setspawn" -> new SetSpawnCommand(player, main);
                case "tp" -> new TPCommand(player, args);
                case "spawn" -> new SpawnCommand(player);
                case "team" -> new TeamCommand(player, main);
                case "info" -> new InfoCommand(player, main);
                case "list" -> new ListCommand(player, main);
                case "kick" -> new KickCommand(player, args, main);
                case "warp", "warps" -> new WarpCommand(player, args, main);
                case "limits" -> new LimitsCommand(player);
                default -> sendHelp(player);
            }
        } else {
            sendHelp(player);
        }

        return true;
    }

    private void sendHelp(@NotNull Player player) {
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
        player.sendMessage(Message.CMD_SB_HELP_LIMITS.getString().get(false));
        player.sendMessage(Message.CMD_SB_HELP_ENDTITEL.getString().get(false));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
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
            arguments.add("warps");
            arguments.add("limits");
            arguments.add("kick");

            StringUtil.copyPartialMatches(args[0], arguments, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "trust", "untrust", "kick" -> {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(other)) {
                            arguments.add(other.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], arguments, completions);
                }
                case "warp" -> StringUtil.copyPartialMatches(args[1], main.getWarpManager().getNames(), completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
