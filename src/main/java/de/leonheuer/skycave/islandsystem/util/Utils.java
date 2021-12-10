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
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.EntityLimitType;
import de.leonheuer.skycave.islandsystem.models.InselID;
import de.leonheuer.skycave.islandsystem.models.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {

    public static final FileConfiguration game = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "game.yml"));
    public static final FileConfiguration cache = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    public static FileConfiguration getInsel(String rg) {
        return YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));
    }

    public static boolean saveInsel(String rg, FileConfiguration file) {
        try {
            file.save(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static InselID getLastInternID() {
        String[] l = cache.getString("lastinternid").split(";");
        return new InselID(Integer.parseInt(l[0]), Integer.parseInt(l[1]));
    }

    public static int getLastID() {
        return cache.getInt("lastid");
    }

    public static boolean setLastInternID() {
        String[] l = cache.getString("lastinternid").split(";");
        InselID get = new InselID(Integer.parseInt(l[0]), Integer.parseInt(l[1]));
        InselID neu = getNextInselID(get);
        cache.set("lastinternid", neu.getXanInt() + ";" + neu.getZanInt());
        try {
            cache.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addLastID() {
        int f = cache.getInt("lastid") + 1;
        cache.set("lastid", f);
        try {
            cache.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            cc = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
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

    public static ProtectedRegion protectedRegion(WorldGuardPlatform wgp, Integer x, Integer z, Integer grosse, String rgName) {
        RegionManager rm = wgp.getRegionContainer().get(BukkitAdapter.adapt(getInselWorld()));
        BlockVector3 min2 = BlockVector3.at(x - grosse, 0, z - grosse);
        BlockVector3 max2 = BlockVector3.at(x + grosse, 255, z + grosse);
        ProtectedCuboidRegion pr = new ProtectedCuboidRegion(rgName, min2, max2);
        rm.addRegion(pr);

        return rm.getRegion(rgName);
    }

    @Nullable
    public static ProtectedRegion getIslandRegionAt(Location loc) {
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
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

    public static Inventory getLimitGui(ProtectedRegion region, EntityLimitType limitType) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lSB§f-§lInsel §cLimits");
        List<EntityLimit> limits = Arrays.stream(EntityLimit.values())
                .filter(entityLimit -> entityLimit.getLimitType() == limitType)
                .sorted((limit1, limit2) -> String.CASE_INSENSITIVE_ORDER.compare(limit1.getType().toString(), limit2.getType().toString()))
                .collect(Collectors.toList());

        if (region == null) {
            limits.forEach(limit -> gui.addItem(new ItemBuilder(limit.getSpawnegg(), 1,
                    "§e" + entityTypeToString(limit.getType()) + " §6(" + limit.getLimit() + ")"
            ).getItem()));
        } else {
            limits.forEach(limit -> {
                int count = main.getLimitManager().getEntityCount(region, limit.getType());
                String color = "§a";
                if (count >= limit.getLimit()) {
                    color = "§c";
                }
                gui.addItem(new ItemBuilder(limit.getSpawnegg(), 1,
                        "§e" + entityTypeToString(limit.getType()) + " " + color + "(" +
                                main.getLimitManager().getEntityCount(region, limit.getType()) +
                                "/" + limit.getLimit() + ")"
                ).getItem());
            });
        }

        for (int i = 18; i < 27; i++) {
            gui.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, "§0").getItem());
        }
        gui.setItem(26, new ItemBuilder(Material.OAK_DOOR, 1, "§cZurück").getItem());

        return gui;
    }

    public static Inventory getLimitGui() {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lSB§f-§lInsel §cLimits");
        AtomicInteger slot = new AtomicInteger(9);
        Arrays.stream(EntityLimitType.values()).forEach(entityLimit -> {
            gui.setItem(slot.get(), new ItemBuilder(entityLimit.getMat(), 1, "§6§l" + entityLimit.getName()).getItem());
            slot.getAndIncrement();
        });
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, "§0").getItem());
        }
        for (int i = 18; i < 27; i++) {
            gui.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, "§0").getItem());
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
