package de.leonheuer.skycave.islandsystem.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarpsConfig {

    private final HashMap<String, Location> warps = new HashMap<>();
    private FileConfiguration config;

    public WarpsConfig() {
        loadConfig();
    }

    private void loadConfig() {
        config = YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/", "warps.yml"));
        for (String warp : config.getStringList("warps")) {
            if (config.getString(warp + ".world") != null) {
                Location loc = new Location(
                        Bukkit.getWorld(config.getString(warp + ".world")),
                        config.getDouble(warp + ".x"),
                        config.getDouble(warp + ".y"),
                        config.getDouble(warp + ".z"),
                        (float) config.getDouble(warp + ".yaw"),
                        (float) config.getDouble(warp + ".pitch")
                );
                warps.put(warp, loc);
            } else {
                System.out.println("[SkyBeeIslandSystem] Welt für den Warp " + warp + " nicht gefunden!");
            }
        }
    }

    private void saveConfig() {
        try {
            config.save(new File("plugins/SkyBeeIslandSystem/", "warps.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadConfig();
    }

    public void setWarp(String warp, Location loc) {
        warps.put(warp, loc);

        List<String> warpList = new ArrayList<>(config.getStringList("warps"));
        warpList.add(warp);
        config.set("warps", warpList);

        config.set(warp + ".world", loc.getWorld().getName());
        config.set(warp + ".x", loc.getX());
        config.set(warp + ".y", loc.getY());
        config.set(warp + ".z", loc.getZ());
        config.set(warp + ".yaw", loc.getYaw());
        config.set(warp + ".pitch", loc.getPitch());

        saveConfig();
    }

    public void removeWarp(String warp) {
        warps.remove(warp);

        List<String> warpList = new ArrayList<>(config.getStringList("warps"));
        warpList.remove(warp);
        config.set("warps", warpList);

        config.set(warp + ".world", null);
        config.set(warp + ".x", null);
        config.set(warp + ".y", null);
        config.set(warp + ".z", null);
        config.set(warp + ".yaw", null);
        config.set(warp + ".pitch", null);

        saveConfig();
    }

    public Location getWarp(String warp) {
        return warps.get(warp);
    }

    public List<String> getWarps() {
        return new ArrayList<>(this.warps.keySet());
    }

    public boolean isSet(String warp) {
        return warps.containsKey(warp);
    }

}
