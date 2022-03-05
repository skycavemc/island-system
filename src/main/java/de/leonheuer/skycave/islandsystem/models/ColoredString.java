package de.leonheuer.skycave.islandsystem.models;

import org.bukkit.ChatColor;

/**
 * Class that stores a raw string and returns the string with translated color codes and prefix.
 */
@SuppressWarnings("unused")
public class ColoredString {

    private final PrefixHolder prefixHolder;
    private String base;

    /**
     * Creates a new colored string instance from a PrefixHolder instance and a base string.
     * @param prefixHolder A class that holds a prefix
     * @param base The raw string to save
     */
    public ColoredString(PrefixHolder prefixHolder, String base) {
        this.prefixHolder = prefixHolder;
        this.base = base;
    }

    /**
     * Replaces the first occurrence of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    public ColoredString replace(String from, String to) {
        base = base.replace(from, to);
        return this;
    }

    /**
     * Replaces all occurrences of the given string with the second given string.
     * @param from String to replace
     * @param to New string
     * @return The ColoredString instance
     */
    public ColoredString replaceAll(String from, String to) {
        base = base.replaceAll(from, to);
        return this;
    }

    /**
     * Gets the string with prefix and translated Minecraft color codes.
     * @return The string output
     */
    public String get() {
        base = ChatColor.translateAlternateColorCodes('&', prefixHolder.getPrefix() + base);
        return base;
    }

    /**
     * Gets the string with translated Minecraft color codes and optionally with prefix.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @return The string output
     */
    public String get(boolean prefix) {
        if (prefix) {
            base = prefixHolder.getPrefix() + base;
        }
        base = ChatColor.translateAlternateColorCodes('&', base);
        return base;
    }

    /**
     * Gets the string, optionally with prefix and optionally with translated Minecraft color codes.
     * @param prefix Whether to start with the prefix of the prefix holder
     * @param formatted Whether to translate Minecraft color codes
     * @return The string output
     */
    public String get(boolean prefix, boolean formatted) {
        if (prefix) {
            base = prefixHolder.getPrefix() + base;
        }
        if (formatted) {
            base = ChatColor.translateAlternateColorCodes('&', base);
        }
        return base;
    }

}
