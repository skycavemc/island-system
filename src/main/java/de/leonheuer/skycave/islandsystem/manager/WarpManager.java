package de.leonheuer.skycave.islandsystem.manager;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.models.AutoSaveConfig;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {

    private final IslandSystem main;
    private final HashMap<String, Location> warps = new HashMap<>();
    private AutoSaveConfig config;

    public WarpManager(IslandSystem main) {
        this.main = main;
    }

    public void reloadConfig() {
        config = new AutoSaveConfig(new File(main.getDataFolder(), "warps.yml"));
        warps.clear();
        for (String key : config.getKeys(false)) {
            warps.put(key, config.getObject(key, Location.class));
        }
        main.getLogger().info("Warp manager config successfully reloaded.");
    }

    public void set(@NotNull String warp, @NotNull Location loc) {
        warps.put(warp, loc);
        config.set(warp, loc);
    }

    public void setMany(@NotNull Map<String, Location> warps) {
        for (Map.Entry<String, Location> entry : warps.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public void remove(@NotNull String warp) {
        warps.remove(warp);
        config.set(warp, null);
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
