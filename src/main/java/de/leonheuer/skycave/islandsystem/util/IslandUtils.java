package de.leonheuer.skycave.islandsystem.util;

import org.jetbrains.annotations.NotNull;

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

}
