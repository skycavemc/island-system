package de.leonheuer.skycave.islandsystem.models;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.ChatColor;

public class FormattableString {

    private String result;

    public FormattableString(String base) {
        result = base;
    }

    public FormattableString replace(String from, String to) {
        result = result.replaceFirst(from, to);
        return this;
    }

    public FormattableString replaceAll(String from, String to) {
        result = result.replaceAll(from, to);
        return this;
    }

    public String get() {
        result = ChatColor.translateAlternateColorCodes('&', IslandSystem.PREFIX + result);
        return result;
    }

    public String get(Boolean prefix) {
        if (prefix) {
            result = IslandSystem.PREFIX + result;
        }
        result = ChatColor.translateAlternateColorCodes('&', result);
        return result;
    }

    public String get(Boolean prefix, Boolean formatted) {
        if (prefix) {
            result = IslandSystem.PREFIX + result;
        }
        if (formatted) {
            result = ChatColor.translateAlternateColorCodes('&', result);
        }
        return result;
    }

}
