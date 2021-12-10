package de.leonheuer.skycave.islandsystem.models;

import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class InselTP {

    private String identifier;

    public InselTP(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Location getTP() {
        FileConfiguration insel = Utils.getInsel(identifier);
        return new Location(
                Utils.getInselWorld(),
                insel.getDouble("tppunkt.x"),
                insel.getDouble("tppunkt.y"),
                insel.getDouble("tppunkt.z"),
                (float) insel.getDouble("tppunkt.yaw"),
                (float) insel.getDouble("tppunkt.pitch")
        );
    }

    public Location getZentrum() {
        FileConfiguration insel = Utils.getInsel(identifier);
        return new Location(
                Utils.getInselWorld(),
                insel.getDouble("insel.zentrum.x"),
                insel.getDouble("insel.zentrum.y"),
                insel.getDouble("insel.zentrum.z"),
                (float) 0,
                (float) 1
        );
    }

    public Location getSpawnInsel() {
        return new Location(Utils.getInselWorld(), 0d, 67d, 0d, 0f, 1f);
    }

}
