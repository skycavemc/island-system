package de.leonheuer.skycave.islandsystem.util;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class IslandUtils {

    public static int nameToId(String name) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name " + name + " does not match pattern for Island names.");
        }
        String[] parts = name.split("_");
        return Integer.parseInt(parts[1]);
    }

    @NotNull
    public static String idToName(int id) {
        DecimalFormat format = new DecimalFormat("000");
        return "sc_" + format.format(id);
    }

    public static boolean isValidName(@NotNull String name) {
        return name.matches("^sc_\\d{3,}$");
    }

    @Nullable
    public static File getIslandSaveLocation(int id, boolean create) {
        File dir = new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "island/");
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            if (create) {
                return null;
            }
        }
        String name = IslandUtils.idToName(id);
        File file = new File(dir, name + ".yml");
        if (!file.isFile()) {
            if (create) {
                return null;
            }
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

}
