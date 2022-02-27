package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.config.InselConfig;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CreateAdmin {

    public CreateAdmin(Player player, String[] args, IslandSystem main) {
        if (args.length >= 4) {
            if (args[1].equalsIgnoreCase("spawn")) {
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_WAIT.getString().get());

                File file = new File(main.getDataFolder(), "sbInsel_Spawn.schem");
                if (file.exists()) {
                    Utils.printSchematic(0, 64, 0, file);

                    ProtectedRegion rg = Utils.protectedRegion(0, 0, 1000, "sc_spawn");
                    if (rg == null) {
                        return;
                    }
                    rg.setFlag(Flags.LEAF_DECAY, StateFlag.State.DENY);
                    rg.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);

                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_FERTIG_SPAWN.getString().get());
                } else {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_MISSING_SCHEMATIC.getString().get());
                }
            } else {
                Player other = Bukkit.getPlayerExact(args[1]);
                if (other == null || !player.canSee(other)) {
                    player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                    return;
                }

                int radius;
                try {
                    radius = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Message.NO_NUMMER.getString().get());
                    return;
                }

                if (radius > 1500 || radius < 100) {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OUTOFRANGE.getString().get());
                    return;
                }

                IslandTemplate template = IslandTemplate.fromString(args[3]);
                if (template == null) {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_TYPEERROR.getString().get());
                    return;
                }
                if (!template.getFile().exists()) {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_MISSING_SCHEMATIC.getString().get());
                    return;
                }

                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_WAIT.getString().get());
                Island island;
                try {
                    island = Island.create(Utils.getLastID(), radius, template);
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OTHER_ERROR.getString().get());
                    return;
                }
                if (island == null) {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OTHER_ERROR.getString().get());
                    return;
                }
                ProtectedRegion region = island.getRegion();
                if (region == null) {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OTHER_ERROR.getString().get());
                    return;
                }

                region.getOwners().addPlayer(other.getUniqueId());

                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_FERTIG.getString().replace("{isid}", "" + Utils.getLastID()).get());
                other.teleport(island.getCenterLocation());
                player.teleport(island.getCenterLocation());
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_SYNTAX.getString().get());
        }
    }

}