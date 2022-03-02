package de.leonheuer.skycave.islandsystem.util;

import de.leonheuer.skycave.islandsystem.models.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyAdapter {

    @Nullable
    public static Map<String, Location> importWarps(@NotNull File file) {
        if (!file.isFile()) {
            return null;
        }
        Map<String, Location> result = new HashMap<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String warp : config.getStringList("warps")) {
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
        }
        return result;
    }

    @Nullable
    public static List<Island> importIslands(@NotNull File dir) {
        if (!dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        List<Island> result = new ArrayList<>();

        for (File f : files) {
            String name = f.getName().replace(".yml", "");
            if (!IslandUtils.isValidName(name)) {
                continue;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

            Instant created = Instant.ofEpochMilli(config.getLong("erstellt"));

            Location spawn = null;
            String worldName = config.getString("tppunkt.world");
            if (worldName != null) {
                spawn = new Location(
                        Bukkit.getWorld(worldName),
                        config.getDouble("tppunkt.x"),
                        config.getDouble("tppunkt.y"),
                        config.getDouble("tppunkt.z"),
                        (float) config.getDouble("tppunkt.yaw"),
                        (float) config.getDouble("tppunkt.pitch")
                );
            }

            File file = IslandUtils.getIslandSaveLocation(IslandUtils.nameToId(name), true);
            if (file == null) {
                continue;
            }
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(new File(dir, name + ".yml"));

            Island island = new Island(IslandUtils.nameToId(name), config.getInt("insel.radius"), spawn,
                    LocalDateTime.ofInstant(created, ZoneId.systemDefault()), newConfig, file);
            result.add(island);
        }
        return result;
    }

    // TODO import cache config

}
