package de.leonheuer.skycave.islandsystem.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.EntityLimitType;
import de.leonheuer.skycave.islandsystem.models.InselID;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Utils {

    public static final FileConfiguration game = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "game.yml"));
    public static final FileConfiguration cache = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    public static FileConfiguration getInsel(String rg) {
        return YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));
    }

    public static void saveInsel(String rg, FileConfiguration file) {
        try {
            file.save(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InselID getLastInternID() {
        String id = cache.getString("lastinternid");
        if (id != null) {
            String[] idParts = id.split(";");
            return new InselID(Integer.parseInt(idParts[0]), Integer.parseInt(idParts[1]));
        }
        return null;
    }

    public static int getLastID() {
        return cache.getInt("lastid");
    }

    public static void setLastInternID() {
        InselID oldId = getLastInternID();
        if (oldId == null) {
            return;
        }
        InselID newId = getNextInselID(oldId);
        cache.set("lastinternid", newId.getXanInt() + ";" + newId.getZanInt());
        try {
            cache.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLastID() {
        int f = cache.getInt("lastid") + 1;
        cache.set("lastid", f);
        try {
            cache.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InselID getNextInselID(InselID id) {
        int absX = Math.abs(id.getXanInt());
        int absY = Math.abs(id.getZanInt());
        if (absX > absY) {
            if (id.getXanInt() > 0) {
                return new InselID(id.getXanInt(), id.getZanInt() + 1);
            } else {
                return new InselID(id.getXanInt(), id.getZanInt() - 1);
            }
        } else if (absY > absX) {
            if (id.getZanInt() > 0) {
                return new InselID(id.getXanInt() - 1, id.getZanInt());
            } else {
                return new InselID(id.getXanInt() + 1, id.getZanInt());
            }
        } else {
            if (id.getXanInt() == id.getZanInt() && id.getXanInt() > 0) {
                return new InselID(id.getXanInt(), id.getZanInt() + 1);
            }
            if (id.getXanInt() == absX) {
                return new InselID(id.getXanInt(), id.getZanInt() + 1);
            }
            if (id.getZanInt() == absY) {
                return new InselID(id.getXanInt(), id.getZanInt() - 1);
            }
            return new InselID(id.getXanInt() + 1, id.getZanInt());
        }
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public static World getInselWorld() {
        return Bukkit.getWorld("skybeeisland");
    }

    public static File getSchematic(String schem) {
        String schematic;

        switch (schem) {
            case "Blume":
                schematic = "sbInsel_Blume.schem";
                break;
            case "Eis":
                schematic = "sbInsel_Eis.schem";
                break;
            case "Pilz":
                schematic = "sbInsel_Pilz.schem";
                break;
            case "Wüste":
                schematic = "sbInsel_Wuste.schem";
                break;
            default:
                return null;
        }

        return new File("plugins/SkyBeeIslandSystem", schematic);
    }

    public static void printSchematic(Integer x, Integer z, File schematic) {
        Clipboard cc = null;
        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        if (format == null) {
            return;
        }
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            cc = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cc == null) {
            return;
        }

        BlockVector3 ctxESBlockVector2 = BlockVector3.at(x, 64, z);
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(new BukkitWorld(getInselWorld()))
                .maxBlocks(-1)
                .build();
        try {
            Operation operation = new ClipboardHolder(cc).createPaste(editSession).to(ctxESBlockVector2).build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static ProtectedRegion protectedRegion(Integer x, Integer z, Integer grosse, String rgName) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(getInselWorld()));
        if (rm == null) {
            return null;
        }

        BlockVector3 min2 = BlockVector3.at(x - grosse, 0, z - grosse);
        BlockVector3 max2 = BlockVector3.at(x + grosse, 255, z + grosse);
        ProtectedCuboidRegion pr = new ProtectedCuboidRegion(rgName, min2, max2);
        rm.addRegion(pr);
        return rm.getRegion(rgName);
    }

    @Nullable
    public static ProtectedRegion getIslandRegionAt(Location loc) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        if (rm == null) {
            return null;
        }

        ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        if (set.getRegions().size() == 0) {
            return null;
        }

        ProtectedRegion result = null;
        for (ProtectedRegion region : set.getRegions()) {
            if (region.getId().matches("^[s][c][_]\\d{3}$")) {
                result = region;
                break;
            }
        }
        return result;
    }

    public static GUI getLimitGui(ProtectedRegion region, EntityLimitType limitType) {
        GUI gui = main.getGuiFactory().createGUI(3, "§6§lSB§f-§lInsel §cLimits");

        List<EntityLimit> limits = new ArrayList<>();
        for (EntityLimit l : EntityLimit.values()) {
            if (l.getLimitType() != limitType) {
                continue;
            }
            limits.add(l);
        }
        Collections.sort(limits);

        int i = 0;
        if (region == null) {
            for (EntityLimit l : limits) {
                gui.set(i, new ItemBuilder(l.getSpawnegg(), 1)
                                .name("§e" + entityTypeToString(l.getType()) + " §6(" + l.getLimit() + ")")
                                .getResult(),
                        (event) -> event.setCancelled(true));
                i++;
            }
        } else {
            for (EntityLimit l : limits) {
                int count = main.getLimitManager().getEntityCount(region.getId(), l.getType());

                String color;
                if (count >= l.getLimit()) {
                    color = "§c";
                } else {
                    color = "§a";
                }

                gui.set(i, new ItemBuilder(l.getSpawnegg(), 1)
                                .name("§e" + entityTypeToString(l.getType()) + " " + color + "(" + count + "/" + l.getLimit() + ")")
                                .getResult(),
                        (event) -> event.setCancelled(true));
                i++;
            }
        }

        for (int j = 18; j < 27; j++) {
            gui.set(j, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                            .name("§0")
                            .getResult(),
                    (event) -> event.setCancelled(true));
        }
        gui.set(26, new ItemBuilder(Material.OAK_DOOR, 1)
                        .name("§cZurück")
                        .getResult(),
                (event) -> {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    getLimitGui().show((Player) event.getWhoClicked());
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                });

        return gui;
    }

    public static GUI getLimitGui() {
        GUI gui = main.getGuiFactory().createGUI(3, "§6§lSB§f-§lInsel §cLimits");

        int i = 9;
        for (EntityLimitType t : EntityLimitType.values()) {
            gui.set(i, new ItemBuilder(t.getMat(), 1)
                            .name("§6§l" + t.getName())
                            .getResult(),
                    (event) -> {
                        event.setCancelled(true);
                        Player player = (Player) event.getWhoClicked();
                        ProtectedRegion region = Utils.getIslandRegionAt(player.getLocation());
                        getLimitGui(region, t).show(player);
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    });
            i++;
        }
        for (int j = 0; j < 9; j++) {
            gui.set(j, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                            .name("§0")
                            .getResult(),
                    (event) -> event.setCancelled(true));
        }
        for (int j = 18; j < 27; j++) {
            gui.set(j, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                            .name("§0")
                            .getResult(),
                    (event) -> event.setCancelled(true));
        }
        return gui;
    }

    public static String entityTypeToString(EntityType type) {
        StringJoiner sj = new StringJoiner(" ");
        String[] partial = type.toString().split("_");
        Arrays.stream(partial).forEach(part -> {
            part = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            sj.add(part);
        });
        return sj.toString();
    }

}
