package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.config.InselConfig;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.InselID;
import de.leonheuer.skycave.islandsystem.models.InselTP;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;

public class CreateAdminCommand {

    WorldGuardPlatform wgp = WorldGuard.getInstance().getPlatform();

    public CreateAdminCommand(Player player, String[] args, IslandSystem main) {
        if (args.length >= 4) {
            if (args[1].equalsIgnoreCase("spawn")) {
                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_WAIT.getString().get());

                File file = new File(main.getDataFolder(), "sbInsel_Spawn.schem");
                if (file.exists()) {
                    Utils.printSchematic(0, 0, file);

                    ProtectedRegion rg = Utils.protectedRegion(wgp, 0, 0, 1000, "sc_spawn");
                    rg.setFlag(Flags.LEAF_DECAY, StateFlag.State.DENY);
                    rg.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);

                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_FERTIG_SPAWN.getString().get());
                } else {
                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_MISSING_SCHEMATIC.getString().get());
                }
            } else {

                Player other = Bukkit.getPlayerExact(args[1]);

                if (other != null) {

                    int grosse;
                    try {
                        grosse = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(Message.NO_NUMMER.getString().get());
                        return;
                    }
                    if (!(grosse <= 1500 && grosse > 0)) {
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OUTOFRANGE.getString().get());
                        return;
                    }

                    if (player.canSee(other)) {

                        File file = Utils.getSchematic(args[3]);

                        if (file == null) {
                            player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_TYPEERROR.getString().get());
                        } else {
                            if (file.exists()) {
                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_WAIT.getString().get());
                                InselID getID = Utils.getLastInternID();

                                Integer x = getID.getXanInt() * 4000;
                                Integer z = getID.getZanInt() * 4000;

                                Utils.printSchematic(x, z, file);

                                String a = "";
                                String b = "";
                                if (Utils.getLastID() < 100) {
                                    if (Utils.getLastID() < 10) {
                                        b = "0";
                                    }
                                    a = "0";
                                }

                                Utils.protectedRegion(wgp, x, z, grosse, "sc_" + a + b + Utils.getLastID()).getOwners().addPlayer(player.getUniqueId());

                                new InselConfig("sc_" + a + b + Utils.getLastID(), getID, player.getUniqueId(), grosse);

                                Location loc2 = new Location(Utils.getInselWorld(), ((getID.getXanInt() * 4000)), (64 + 2), ((getID.getZanInt() * 4000)));
                                Utils.getInselWorld().spawnEntity(loc2, EntityType.VILLAGER);
                                loc2.setX(loc2.getX() + 1);
                                Utils.getInselWorld().spawnEntity(loc2, EntityType.VILLAGER);

                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_FERTIG.getString().replace("{isid}", "" + Utils.getLastID()).get());
                                other.teleport((new InselTP("sc_" + a + b + Utils.getLastID())).getTP());
                                player.teleport((new InselTP("sc_" + a + b + Utils.getLastID())).getTP());
                                Utils.addLastID();
                                Utils.setLastInternID();
                            } else {
                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_MISSING_SCHEMATIC.getString().get());
                            }
                        }
                    } else {
                        player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                    }
                } else {
                    player.sendMessage(Message.PLAYER_NOONLINE.getString().replace("{player}", args[1]).get());
                }
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_SYNTAX.getString().get());
        }
    }

}