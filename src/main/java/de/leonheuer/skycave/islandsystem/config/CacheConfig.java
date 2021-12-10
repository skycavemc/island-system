package de.leonheuer.skycave.islandsystem.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CacheConfig {

    public CacheConfig() {
        FileConfiguration cache = YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));

        cache.addDefault("lastinternid", "1;1");
        cache.addDefault("lastid", "1");

        cache.options().copyDefaults(true);
        try {
            cache.save(new File("plugins/SkyBeeIslandSystem/", "cache.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
