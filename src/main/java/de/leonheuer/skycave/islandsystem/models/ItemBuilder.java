package de.leonheuer.skycave.islandsystem.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(Material mat, int amount, String title) {
        item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }

}
