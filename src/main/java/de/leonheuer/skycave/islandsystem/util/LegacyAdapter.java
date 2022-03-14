package de.leonheuer.skycave.islandsystem.util;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.Islands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyAdapter {

    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    /**
     * Imports the old warps from legacy-format into the new format.
     * @param file The file to import from
     * @return Whether the operation succeeded
     */
    public static boolean importWarps(@NotNull File file) {
        if (!file.isFile()) {
            return false;
        }
        Map<String, Location> result = new HashMap<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> oldWarps = config.getStringList("warps");
        int i = 0;
        for (String warp : oldWarps) {
            String worldName = config.getString(warp + ".world");
            if (worldName == null) {
                continue;
            }
            Location loc = new Location(
                    Bukkit.getWorld(worldName),
                    config.getDouble(warp + ".x"),
                    config.getDouble(warp + ".y"),
                    config.getDouble(warp + ".z"),
                    (float) config.getDouble(warp + ".yaw"),
                    (float) config.getDouble(warp + ".pitch")
            );
            result.put(warp, loc);
            i++;
        }
        main.getWarpManager().setMany(result);
        main.getLogger().info("Imported " + i + " of " + oldWarps.size() + " warps.");
        return true;
    }

    /**
     * Imports the old islands from legacy-format into the new format.
     * @param dir The directory to import from
     * @return Whether the operation succeeded
     */
    public static boolean importIslands(@NotNull File dir) {
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }

        int i = 0;
        int count = files.length;
        for (File f : files) {
            String name = f.getName().replace(".yml", "");
            if (!IslandUtils.isValidName(name)) {
                count--;
                continue;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            int radius = config.getInt("insel.radius");
            Instant created = Instant.ofEpochMilli(config.getLong("erstellt"));
            Location spawn = new Location(
                    main.getIslandWorld(),
                    config.getDouble("tppunkt.x"),
                    config.getDouble("tppunkt.y"),
                    config.getDouble("tppunkt.z"),
                    (float) config.getDouble("tppunkt.yaw"),
                    (float) config.getDouble("tppunkt.pitch")
            );
            File file = IslandUtils.getIslandSaveLocation(IslandUtils.nameToId(name), true);
            if (file == null) {
                continue;
            }
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(file);

            Island island = Islands.importAndSave(IslandUtils.nameToId(name) ,newConfig, file, new ArrayList<>(), radius,
                    spawn, LocalDateTime.ofInstant(created, ZoneId.systemDefault()), null);
            if (island == null) {
                continue;
            }
            island.setRadius(radius);
            i++;
        }
        main.getLogger().info("Imported " + i + " of " + count + " islands.");
        return true;
    }

    /**
     * Imports the old cache config from legacy-format into the new format.
     * @param file The file to import from
     * @return Whether the operation succeeded
     */
    public static boolean importCacheConfig(@NotNull File file) {
        if (!file.isFile()) {
            return false;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        int id = config.getInt("lastid");
        main.getConfiguration().set("current_island_id", id - 1);
        main.getLogger().info("Imported cache config.");
        return true;
    }

}
