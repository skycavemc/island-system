package de.leonheuer.skycave.islandsystem.cmd;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.cmd.sb.*;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
                case "ban" -> new BanCommand(player, args, main);
                case "unban", "pardon" -> new UnbanCommand(player, args, main);
                case "kick" -> new KickCommand(player, args, main);
                case "list" -> new ListCommand(player, main);
                case "info" -> new InfoCommand(player, args, main, false);
                case "setspawn" -> new SetSpawnCommand(player, main);
                case "tp" -> new TPCommand(player, args);
                case "spawn" -> new SpawnCommand(player, main);
                case "warp", "warps" -> new WarpCommand(player, args, main);
                case "limits" -> new LimitsCommand(player);
                case "notify" -> new NotifyCommand(player, args, main);
                default -> sendHelp(player);
            }
        } else {
            sendHelp(player);
        }

        return true;
    }

    private void sendHelp(@NotNull Player player) {
        player.sendMessage(Message.HELP_HEADER.getString().get(false));
        player.sendMessage(Message.HELP_BUY.getString().get(false));
        player.sendMessage(Message.HELP_TRUST.getString().get(false));
        player.sendMessage(Message.HELP_UNTRUST.getString().get(false));
        player.sendMessage(Message.HELP_BAN.getString().get(false));
        player.sendMessage(Message.HELP_UNBAN.getString().get(false));
        player.sendMessage(Message.HELP_KICK.getString().get(false));
        player.sendMessage(Message.HELP_LIST.getString().get(false));
        player.sendMessage(Message.HELP_INFO.getString().get(false));
        player.sendMessage(Message.HELP_SETSPAWN.getString().get(false));
        player.sendMessage(Message.HELP_TP.getString().get(false));
        player.sendMessage(Message.HELP_SPAWN.getString().get(false));
        player.sendMessage(Message.HELP_WARP.getString().get(false));
        player.sendMessage(Message.HELP_LIMITS.getString().get(false));
        player.sendMessage(Message.HELP_NOTIFY.getString().get(false));
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
            arguments.add("ban");
            arguments.add("unban");
            arguments.add("kick");
            arguments.add("list");
            arguments.add("info");
            arguments.add("setspawn");
            arguments.add("tp");
            arguments.add("spawn");
            arguments.add("warp");
            arguments.add("warps");
            arguments.add("limits");
            arguments.add("notify");

            StringUtil.copyPartialMatches(args[0], arguments, completions);
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "trust", "untrust", "ban", "unban", "pardon", "kick" -> {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(other)) {
                            arguments.add(other.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], arguments, completions);
                }
                case "warp", "warps" -> StringUtil.copyPartialMatches(args[1], main.getWarpManager().getNames(), completions);
                case "notify" -> {
                    arguments.add("on");
                    arguments.add("off");
                    StringUtil.copyPartialMatches(args[1], arguments, completions);
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("notify")) {
                arguments.addAll(Arrays.stream(EntityLimit.values()).map(limit -> limit.toString().toLowerCase()).toList());
                StringUtil.copyPartialMatches(args[3], arguments, completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
