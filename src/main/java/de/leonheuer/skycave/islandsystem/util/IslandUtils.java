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
import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class IslandUtils {

    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    public static int nameToId(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name " + name + " does not match pattern for Island names.");
        }
        String[] parts = name.split("_");
        return Integer.parseInt(parts[1]);
    }

    @NotNull
    public static String idToName(int id) {
        DecimalFormat format = new DecimalFormat("000");
        return "sc_" + format.format(id);
    }

    public static boolean isValidName(@NotNull String name) {
        return name.matches("^sc_\\d{3,}$");
    }

    @Nullable
    public static File getIslandSaveLocation(int id, boolean create) {
        File dir = new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "island/");
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            if (create) {
                return null;
            }
        }
        String name = IslandUtils.idToName(id);
        File file = new File(dir, name + ".yml");
        if (!file.isFile()) {
            if (create) {
                return null;
            }
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
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
                .world(BukkitAdapter.adapt(main.getIslandWorld()))
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
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(main.getIslandWorld()));
        if (rm == null) {
            return null;
        }

        BlockVector3 from = BlockVector3.at(x - radius, -64, z - radius);
        BlockVector3 to = BlockVector3.at(x + radius, 320, z + radius);
        ProtectedCuboidRegion pr = new ProtectedCuboidRegion(region, from, to);
        rm.addRegion(pr);
        return pr;
    }

}
