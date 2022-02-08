package de.leonheuer.skycave.islandsystem.config;

import de.leonheuer.skycave.islandsystem.models.InselID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class InselConfig {

    public InselConfig(String rg, InselID inselID, UUID owner, int radius) {
        FileConfiguration insel = YamlConfiguration.loadConfiguration(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));

        insel.addDefault("name", rg);
        insel.addDefault("interneid", inselID.getXanInt() + ";" + inselID.getZanInt());
        insel.addDefault("erstellt", System.currentTimeMillis());
        insel.addDefault("insel.radius", radius);
        insel.addDefault("insel.start.x", (inselID.getXanInt() * 4000 - radius));
        insel.addDefault("insel.start.y", 0);
        insel.addDefault("insel.start.z", (inselID.getZanInt() * 4000 - radius));
        insel.addDefault("insel.zentrum.x", (inselID.getXanInt() * 4000));
        insel.addDefault("insel.zentrum.y", 0);
        insel.addDefault("insel.zentrum.z", (inselID.getZanInt() * 4000));
        insel.addDefault("insel.end.x", (inselID.getXanInt() * 4000 + radius));
        insel.addDefault("insel.end.y", 255);
        insel.addDefault("insel.end.z", (inselID.getZanInt() * 4000 + radius));
        insel.addDefault("owner", owner.toString());
        insel.addDefault("money", 0.0);
        ArrayList<String> a = new ArrayList<>();
        insel.addDefault("member", a);

        insel.addDefault("tppunkt.x", (inselID.getXanInt() * 4000));
        insel.addDefault("tppunkt.y", 64);
        insel.addDefault("tppunkt.z", (inselID.getZanInt() * 4000));
        insel.addDefault("tppunkt.yaw", 0);
        insel.addDefault("tppunkt.pitch", 1);

        insel.addDefault("warppunkt.x", (inselID.getXanInt() * 4000));
        insel.addDefault("warppunkt.y", 64);
        insel.addDefault("warppunkt.z", (inselID.getZanInt() * 4000));
        insel.addDefault("warppunkt.yaw", 0);
        insel.addDefault("warppunkt.pitch", 1);


        insel.options().copyDefaults(true);
        insel.options().setHeader(List.of("Ändern von Werten kann zu Nichtfunktion des Plugins führen."));
        insel.options().parseComments(true);
        try {
            insel.save(new File("plugins/SkyBeeIslandSystem/insel/", rg + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}