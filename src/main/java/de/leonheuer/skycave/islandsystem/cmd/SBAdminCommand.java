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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class SBAdminCommand implements TabExecutor {

    private final IslandSystem main;

    public SBAdminCommand(IslandSystem main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String arg, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "create" -> new CreateAdmin(player, args, main);
                case "tp" -> new TPAdmin(player, args);
                case "trust" -> new TrustAdmin(player, args, main);
                case "untrust" -> new UntrustAdmin(player, args, main);
                case "ban" -> new BanAdmin(player, args, main);
                case "unban" -> new UnbanAdmin(player, args, main);
                case "info" -> new InfoAdmin(player, args, main);
                case "setspawn" -> new SetSpawnAdmin(player, main);
                case "setowner" -> new SetOwnerAdmin(player, args, main);
                case "setradius" -> new SetRadiusAdmin(player, args, main);
                case "setwarp" -> new SetWarpAdmin(player, args, main);
                case "delwarp" -> new DelWarpAdmin(player, args, main);
                case "import" -> new ImportAdmin(player);
                default -> sendHelp(player);
            }
        } else {
            sendHelp(player);
        }

        return true;
    }

    private void sendHelp(@NotNull Player player) {
        player.sendMessage(Message.ADMIN_HELP_HEADER.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_CREATE.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_TP.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_TRUST.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_UNTRUST.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_BAN.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_UNBAN.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_INFO.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_SETSPAWN.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_SETOWNER.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_SETRADIUS.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_SETWARP.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_DELWARP.getString().get(false));
        player.sendMessage(Message.ADMIN_HELP_IMPORT.getString().get(false));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        Player player = (Player) sender;
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            arguments.add("create");
            arguments.add("tp");
            arguments.add("trust");
            arguments.add("untrust");
            arguments.add("ban");
            arguments.add("unban");
            arguments.add("pardon");
            arguments.add("info");
            arguments.add("setradius");
            arguments.add("setowner");
            arguments.add("setspawn");
            arguments.add("setwarp");
            arguments.add("delwarp");
            arguments.add("import");

            StringUtil.copyPartialMatches(args[0], arguments, completions);
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create", "setowner", "trust", "untrust", "ban", "unban", "pardon" -> {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(other)) {
                            arguments.add(other.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], arguments, completions);
                }
                case "setwarp", "delwarp" -> StringUtil.copyPartialMatches(args[1], main.getWarpManager().getNames(), completions);
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