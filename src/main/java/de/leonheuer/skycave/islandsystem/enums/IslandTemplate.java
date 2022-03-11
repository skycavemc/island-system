package de.leonheuer.skycave.islandsystem.enums;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public enum IslandTemplate {

    ICE("Eis", "sbInsel_Eis.schem", Material.BLUE_ICE, 1000000),
    FLOWER("Blume", "sbInsel_Blume.schem", Material.ROSE_BUSH, 1200000),
    MUSHROOM("Pilz", "sbInsel_Pilz.schem", Material.RED_MUSHROOM_BLOCK, 1300000),
    DESERT("Wüste", "sbInsel_Wuste.schem", Material.SAND, 1500000),
    ;

    private final String alternativeName;
    private final String fileName;
    private final Material icon;
    private final int cost;

    IslandTemplate(String alternativeName, String fileName, Material icon, int cost) {
        this.alternativeName = alternativeName;
        this.fileName = fileName;
        this.icon = icon;
        this.cost = cost;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    @Contract(" -> new")
    @NotNull
    public File getFile() {
        File dir = new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "schematics/");
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        return new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "schematics/" + fileName);
    }

    public Material getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
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
