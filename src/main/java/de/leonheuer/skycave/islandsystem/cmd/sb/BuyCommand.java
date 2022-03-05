package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.SelectionProfile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class BuyCommand {

    private final IslandSystem main;
    private final Player player;

    public BuyCommand(@NotNull Player player, @NotNull IslandSystem main) {
        this.main = main;
        this.player = player;

        Economy economy = main.getEconomy();
        if (economy == null) {
            player.sendMessage(Message.TEMP_DISABLED.getString().get());
            return;
        }

        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem());
        GUI gui = main.getGuiFactory().createGUI(6, "§2Inselkauf")
                .formatPattern(pattern.startAtLine(1))
                .formatPattern(pattern.startAtLine(6));

        main.getSelectionProfiles().put(player.getUniqueId(), new SelectionProfile(IslandTemplate.ICE, false));

        ItemStack selected = ItemBuilder.of(Material.LIME_DYE).name("&aausgewählt").asItem();
        ItemStack notSelected = ItemBuilder.of(Material.GRAY_DYE).name("&7nicht ausgewählt").asItem();
        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);


        int i = 19;
        for (IslandTemplate type : IslandTemplate.values()) {
            Consumer<InventoryClickEvent> changeType = event -> {
                Player p = (Player) event.getWhoClicked();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                main.getSelectionProfiles().get(p.getUniqueId()).setTemplate(type);
                int j = 28;
                for (IslandTemplate t : IslandTemplate.values()) {
                    if (t == type) {
                        gui.setItem(j, selected);
                    } else {
                        gui.setItem(j, notSelected);
                    }
                    j++;
                }
                gui.setItem(6, 8, getConfirmItemStack());
            };

            ItemStack item = ItemBuilder.of(type.getIcon())
                    .name("&e" + type.getAlternativeName())
                    .description("&7Grundkosten: &b" + format.format(type.getCost()) + "$")
                    .asItem();

            gui.setItem(i, item, changeType);
            gui.setItem(i + 9, notSelected, changeType);
            if (type == IslandTemplate.ICE) {
                gui.setItem(i + 9, selected);
            }
            i++;
        }

        ItemStack small = ItemBuilder.of(Material.MEDIUM_AMETHYST_BUD)
                .name("&eGröße: klein (500x500)")
                .description("&7Kostenmultiplikator: &b1x")
                .asItem();
        ItemStack large = ItemBuilder.of(Material.AMETHYST_CLUSTER)
                .name("&eGröße: groß (1000x1000)")
                .description("&7Kostenmultiplikator: &b2x")
                .asItem();

        gui.setItem(3, 7, small, event -> {
            Player p = (Player) event.getWhoClicked();
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            boolean isLarge = main.getSelectionProfiles().get(p.getUniqueId()).isLarge();
            if (isLarge) {
                gui.setItem(3, 7, small);
            } else {
                gui.setItem(3, 7, large);
            }
            main.getSelectionProfiles().get(p.getUniqueId()).setLarge(!isLarge);
            gui.setItem(6, 8, getConfirmItemStack());
        }).setItem(6, 7, ItemBuilder.of(Material.RED_CONCRETE).name("&cAbbrechen").asItem(), event -> {
                    Player p = (Player) event.getWhoClicked();
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                }
        ).setItem(6, 8, getConfirmItemStack(), event -> {
                    Player p = (Player) event.getWhoClicked();
                    if (economy.has(p, getCost())) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        if (economy.withdrawPlayer(p, getCost()).transactionSuccess()) {
                            p.sendMessage(Message.BUY_SUCCESS.getString().get());
                        } else {
                            p.sendMessage(Message.BUY_ERROR.getString().get());
                        }
                        p.closeInventory();
                    } else {
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        String diff = format.format(getCost() - economy.getBalance(p)) + "$";
                        p.sendMessage(Message.BUY_NOT_ENOUGH.getString().replace("{diff}", diff).get());
                    }
                }
        ).show(player);
    }

    private int getCost() {
        SelectionProfile profile = main.getSelectionProfiles().get(player.getUniqueId());
        if (profile.isLarge()) {
            return profile.getTemplate().getCost() * 2;
        }
        return profile.getTemplate().getCost();
    }

    private ItemStack getConfirmItemStack() {
        SelectionProfile profile = main.getSelectionProfiles().get(player.getUniqueId());
        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
        String size;
        String cost = format.format(getCost()) + "$";
        if (profile.isLarge()) {
            size = "groß &8(&71000x1000&8)";
        } else {
            size = "klein &8(&7500x500&8)";
        }
        return ItemBuilder.of(Material.LIME_CONCRETE)
                .name("&aBestätigen")
                .description(
                        "&7Starter-Insel: &e" + profile.getTemplate().getAlternativeName(),
                        "&7Größe: &e" + size,
                        "&7Kosten: &e" + cost,
                        "&cDer Betrag wird direkt abgebucht!"
                ).asItem();
    }

}
