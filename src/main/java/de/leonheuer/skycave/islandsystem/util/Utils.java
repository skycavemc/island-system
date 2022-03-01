package de.leonheuer.skycave.islandsystem.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.EntityLimitType;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Utils {

    public static final FileConfiguration GAME_CONFIG = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "game.yml"));
    public static final FileConfiguration CACHE_CONFIG = YamlConfiguration.loadConfiguration(
            new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);
    public static final World ISLAND_WORLD = Bukkit.getWorld(main.getIslandWorld().getUID());

    public static int getLastID() {
        return CACHE_CONFIG.getInt("lastid");
    }

    public static void increaseLastID() {
        int f = CACHE_CONFIG.getInt("lastid") + 1;
        CACHE_CONFIG.set("lastid", f);
        try {
            CACHE_CONFIG.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean printSchematic(int x, int y, int z, File schematic) {
        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        if (format == null) {
            return false;
        }

        Clipboard cc;
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            cc = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (cc == null) {
            return false;
        }

        BlockVector3 location = BlockVector3.at(x, y, z);
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(BukkitAdapter.adapt(ISLAND_WORLD))
                .maxBlocks(-1)
                .build();
        try {
            Operation operation = new ClipboardHolder(cc).createPaste(editSession).to(location).build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static @Nullable ProtectedRegion protectedRegion(int x, int z, int radius, String region) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(ISLAND_WORLD));
        if (rm == null) {
            return null;
        }

        BlockVector3 min2 = BlockVector3.at(x - radius, 0, z - radius);
        BlockVector3 max2 = BlockVector3.at(x + radius, 255, z + radius);
        ProtectedCuboidRegion pr = new ProtectedCuboidRegion(region, min2, max2);
        rm.addRegion(pr);
        return rm.getRegion(region);
    }

    @Nullable
    public static ProtectedRegion getIslandRegionAt(@NotNull Location loc) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        if (rm == null) {
            return null;
        }

        ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        if (set.getRegions().size() == 0) {
            return null;
        }

        for (ProtectedRegion region : set.getRegions()) {
            if (IslandUtils.isValidName(region.getId())) {
                return region;
            }
        }
        return null;
    }

    public static @NotNull GUI getLimitGui(ProtectedRegion region, EntityLimitType limitType) {
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
                gui.set(i, new ItemBuilder(l.getSpawnEgg(), 1)
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

                gui.set(i, new ItemBuilder(l.getSpawnEgg(), 1)
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

    public static @NotNull GUI getLimitGui() {
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

    public static String entityTypeToString(@NotNull EntityType type) {
        StringJoiner sj = new StringJoiner(" ");
        String[] partial = type.toString().split("_");
        Arrays.stream(partial).forEach(part -> {
            part = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            sj.add(part);
        });
        return sj.toString();
    }

    public static String locationAsString(Location location) {
        return location.getX() + ", " +
                location.getY() + ", " +
                location.getZ();
    }

    public static String locationAsString(BlockVector3 location) {
        return location.getX() + ", " +
                location.getY() + ", " +
                location.getZ();
    }

}
