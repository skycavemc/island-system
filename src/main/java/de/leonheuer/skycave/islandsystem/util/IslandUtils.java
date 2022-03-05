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

    /**
     * Transforms an island name into its according ID.
     * @param name The name to transform
     * @return The ID
     * @throws IllegalArgumentException If the name is not a valid island name.
     */
    public static int nameToId(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name " + name + " does not match pattern for Island names.");
        }
        String[] parts = name.split("_");
        return Integer.parseInt(parts[1]);
    }

    /**
     * Transforms an island ID into its according name.
     * @param id The ID to transform
     * @return The name
     */
    @NotNull
    public static String idToName(int id) {
        DecimalFormat format = new DecimalFormat("000");
        return "sc_" + format.format(id);
    }

    /**
     * Checks whether the given string is a valid island name.
     * @param name The name to check
     * @return Whether the name is valid
     */
    public static boolean isValidName(@NotNull String name) {
        return name.matches("^sc_\\d{3,}$");
    }

    /**
     * Gets the destination of the island save file. Optionally creates the file if it does not exist.
     * @param id The ID of the island
     * @param create Whether to create the file if it does not exist
     * @return The destination file
     */
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

    /**
     * Gets the WorldGuard region of an island at the given location. Returns null if there is none.
     * @param location The location to search around
     * @return The WorldGuard region
     */
    @Nullable
    public static ProtectedRegion getIslandRegionAt(@NotNull Location location) {
        RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (rm == null) {
            return null;
        }
        ApplicableRegionSet set = rm.getApplicableRegions(BukkitAdapter.asBlockVector(location));
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

    /**
     * Prints a schematic from the given file at the coordinates.
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @param schematic The file to load the schematic from
     * @return Whether the operation succeeded
     */
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

    /**
     * Creates a cuboid WorldGuard region at the given center x and z coordinates in the island world.
     * Reaches from bottom-most height to top-most height and will be scaled up to the radius around the x and z
     * coordinates. The region name must be provided. Will return null if the island world is not loaded.
     * @param x The center x coordinate
     * @param z The center z coordinate
     * @param radius The radius around the center
     * @param region The name of the region
     * @return The created region
     */
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
