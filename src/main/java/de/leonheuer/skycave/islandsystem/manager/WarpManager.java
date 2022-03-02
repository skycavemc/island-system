package de.leonheuer.skycave.islandsystem.manager;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {

    private final HashMap<String, Location> warps = new HashMap<>();
    private FileConfiguration config;

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/", "warps.yml"));
        for (String key : config.getKeys(false)) {
            warps.put(key, config.getObject(key, Location.class));
        }
    }

    private void saveConfig() {
        try {
            config.save(new File("plugins/SkyBeeIslandSystem/", "warps.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();
    }

    public void set(@NotNull String warp, @NotNull Location loc) {
        warps.put(warp, loc);
        config.set(warp, loc);
        saveConfig();
    }

    public void setMany(@NotNull Map<String, Location> warps) {
        for (Map.Entry<String, Location> entry : warps.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public void remove(@NotNull String warp) {
        warps.remove(warp);
        config.set(warp, null);
        saveConfig();
    }

    @Nullable
    public Location get(@NotNull String warp) {
        return warps.get(warp);
    }

    @NotNull
    public List<String> getNames() {
        return new ArrayList<>(this.warps.keySet());
    }

    public boolean exists(String warp) {
        return warps.containsKey(warp);
    }

}
