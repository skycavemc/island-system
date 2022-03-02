package de.leonheuer.skycave.islandsystem.models;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public class ColoredStringBuilder {

    private String result;

    public ColoredStringBuilder(String base) {
        result = base;
    }

    public ColoredStringBuilder replace(String from, String to) {
        result = result.replace(from, to);
        return this;
    }

    public ColoredStringBuilder replaceAll(String from, String to) {
        result = result.replaceAll(from, to);
        return this;
    }

    public String get() {
        result = ChatColor.translateAlternateColorCodes('&', IslandSystem.PREFIX + result);
        return result;
    }

    public String get(boolean prefix) {
        if (prefix) {
            result = IslandSystem.PREFIX + result;
        }
        result = ChatColor.translateAlternateColorCodes('&', result);
        return result;
    }

    public String get(boolean prefix, boolean formatted) {
        if (prefix) {
            result = IslandSystem.PREFIX + result;
        }
        if (formatted) {
            result = ChatColor.translateAlternateColorCodes('&', result);
        }
        return result;
    }

}
