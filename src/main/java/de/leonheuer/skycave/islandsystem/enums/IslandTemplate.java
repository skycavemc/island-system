package de.leonheuer.skycave.islandsystem.enums;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public enum IslandTemplate {

    FLOWER("Blume", "sbInsel_Blume.schem"),
    ICE("Eis", "sbInsel_Eis.schem"),
    MUSHROOM("Pilz", "sbInsel_Pilz.schem"),
    DESERT("Wüste", "sbInsel_Wuste.schem"),
    ;

    private final String alternativeName;
    private final String fileName;

    IslandTemplate(String alternativeName, String fileName) {
        this.alternativeName = alternativeName;
        this.fileName = fileName;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), fileName);
    }

    @Nullable
    public static IslandTemplate fromString(String name) {
        for (IslandTemplate template: IslandTemplate.values()) {
            if (template.toString().equalsIgnoreCase(name) ||
                    template.getAlternativeName().equalsIgnoreCase(name)) {
                return template;
            }
        }
        return null;
    }
}
