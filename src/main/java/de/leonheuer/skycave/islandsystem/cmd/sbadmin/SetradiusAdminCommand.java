package de.leonheuer.skycave.islandsystem.cmd.sbadmin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetradiusAdminCommand {


    public SetradiusAdminCommand(Player player, String[] args) {
        if (args.length >= 2) {
            if (Utils.getInselWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
                WorldGuardPlatform wgp = WorldGuard.getInstance().getPlatform();
                RegionManager ctxRM1 = wgp.getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
                ApplicableRegionSet set = ctxRM1.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));

                for (ProtectedRegion r : set.getRegions()) {
                    if (r.getParent() == null) {
                        int g;
                        try {
                            g = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(Message.NO_NUMMER.getString().get());
                            return;
                        }
                        if (args.length >= 3) {
                            if (!args[2].equalsIgnoreCase("true")) {
                                if (Utils.getInsel(r.getId()).getInt("insel.radius") <= g) {
                                    player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETRADIUS_OUTOFRANGE.getString().get());
                                    return;
                                }
                            }
                            if (!(g <= 1500 && g > 0)) {
                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OUTOFRANGE.getString().get());
                                return;
                            }
                        } else {
                            if (!(g <= 1500 && g > 0)) {
                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_CREATE_OUTOFRANGE.getString().get());
                                return;
                            }
                            if (Utils.getInsel(r.getId()).getInt("insel.radius") <= g) {
                                player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETRADIUS_OUTOFRANGE.getString().get());
                                return;
                            }
                        }

                        int x = Utils.getInsel(r.getId()).getInt("insel.zentrum.x");
                        int z = Utils.getInsel(r.getId()).getInt("insel.zentrum.z");
                        BlockVector3 min = BlockVector3.at((x - g), 0, (z - g));
                        BlockVector3 max = BlockVector3.at((x + g), 255, (z + g));
                        ProtectedCuboidRegion cr = new ProtectedCuboidRegion(r.getId(), min, max);
                        cr.getOwners().addAll(r.getOwners());
                        cr.getMembers().addAll(r.getMembers());
                        cr.getFlags().putAll(r.getFlags());
                        cr.setPriority(r.getPriority());
                        cr.setDirty(r.isDirty());
                        try {
                            cr.setParent(r.getParent());
                        } catch (ProtectedRegion.CircularInheritanceException e) {
                            player.sendMessage("Error 1");
                        }
                        RegionManager ctxRM2 = wgp.getRegionContainer().get(BukkitAdapter.adapt(Utils.getInselWorld()));
                        ctxRM2.removeRegion(r.getId());
                        ctxRM2.addRegion(cr);
                        FileConfiguration i = Utils.getInsel(cr.getId());
                        i.set("insel.radius", g);
                        i.set("insel.start.x", (x - g));
                        i.set("insel.start.y", 0);
                        i.set("insel.start.z", (z - g));
                        i.set("insel.end.x", (x + g));
                        i.set("insel.end.y", g);
                        i.set("insel.end.z", (z + g));
                        Utils.saveInsel(cr.getId(), i);
                        player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETRADIUS_ERFOLG.getString().get());
                    }
                }
            } else {
                player.sendMessage(Message.MISC_NOINWORLD.getString().get());
            }
        } else {
            player.sendMessage(Message.SBADMIN_SUBCOMMAND_SETRADIUS_SYNTAX.getString().get());
        }
    }
}