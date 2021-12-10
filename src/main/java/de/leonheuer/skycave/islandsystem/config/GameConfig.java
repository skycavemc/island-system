package de.leonheuer.skycave.islandsystem.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameConfig {

    public GameConfig() {
        FileConfiguration game = YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/", "game.yml"));
        ArrayList<String> kaufinfo = new ArrayList<>();

        kaufinfo.add("\n");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("KAUFINFO");
        kaufinfo.add("FORUM");
        kaufinfo.add("\n");

        game.addDefault("info.kaufen", kaufinfo);

        game.options().copyDefaults(true);
        try {
            game.save(new File("plugins/SkyBeeIslandSystem/", "game.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}