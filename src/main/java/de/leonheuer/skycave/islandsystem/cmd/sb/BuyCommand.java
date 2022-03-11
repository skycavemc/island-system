package de.leonheuer.skycave.islandsystem.cmd.sb;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import de.leonheuer.skycave.islandsystem.enums.Message;
import de.leonheuer.skycave.islandsystem.models.CreationResponse;
import de.leonheuer.skycave.islandsystem.models.Island;
import de.leonheuer.skycave.islandsystem.models.SelectionProfile;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BuyCommand {

    private final IslandSystem main;
    private final Player player;
    private volatile boolean done = false;

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
                    event.setCancelled(true);
                    Player p = (Player) event.getWhoClicked();
                    if (!economy.has(p, getCost())) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        String diff = format.format(getCost() - economy.getBalance(p)) + "$";
                        p.sendMessage(Message.BUY_NOT_ENOUGH_MONEY.getString().replace("{diff}", diff).get());
                        return;
                    }
                    int required = nestsRequired(p);
                    if (!hasNests(p, required)) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        p.sendMessage(Message.BUY_NOT_ENOUGH_NESTS.getString().replace("{amount}", "" + required).get());
                        return;
                    }
                    buy(p, required);
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

    private int nestsRequired(@NotNull Player p) {File dir = new File(main.getDataFolder(), "islands/");
        if (!dir.isDirectory()) {
            return -1;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return -1;
        }

        int islandCount = 0;

        for (File f : files) {
            String name = f.getName().replace(".yml", "");
            if (!IslandUtils.isValidName(name)) {
                continue;
            }
            Island island = Island.load(IslandUtils.nameToId(name));
            if (island == null) {
                continue;
            }
            ProtectedRegion region = island.getRegion();
            if (region == null) {
                continue;
            }

            if (region.getOwners().contains(p.getUniqueId())) {
                islandCount++;
            }
        }
        return 3 * (islandCount + 1);
    }

    @SuppressWarnings("deprecation")
    private boolean isNest(ItemStack item) {
        if (item == null) {
            return false;
        }
        boolean isHive = item.getType() == Material.BEEHIVE &&
                item.getItemMeta().getDisplayName().contains("§6Bienenstock §8» §eLevel §a300");
        boolean isNest = item.getType() == Material.BEE_NEST &&
                item.getItemMeta().getDisplayName().contains("§6Bienennest §8» §eLevel §a300");
        return isHive || isNest;
    }

    private boolean hasNests(@NotNull Player p, int required) {
        int amount = 0;
        for (ItemStack item : p.getInventory()) {
            if (isNest(item)) {
                amount = amount + item.getAmount();
            }
        }
        return amount >= required;
    }

    private void removeNests(@NotNull Player p, int required) {
        int amount = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (!isNest(item)) {
                continue;
            }
            if (amount + item.getAmount() > required) {
                item.setAmount(item.getAmount() - (required - amount));
                return;
            }
            amount = amount + item.getAmount();
            item.setAmount(0);
            if (amount >= required) {
                return;
            }
        }
    }

    private void buy(@NotNull Player p, int required) {
        SelectionProfile profile = main.getSelectionProfiles().get(p.getUniqueId());
        if (!profile.getTemplate().getFile().exists()) {
            p.sendMessage(Message.BUY_TEMPLATE_ERROR.getString().get());
            return;
        }

        p.closeInventory();
        int cost = getCost();
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        if (!main.getEconomy().withdrawPlayer(p, cost).transactionSuccess()) {
            p.sendMessage(Message.BUY_TRANSACTION_FAILED.getString().get());
            return;
        }
        removeNests(p, required);

        player.sendMessage(Message.BUY_WAIT.getString().get());
        int id = main.getConfiguration().getInt("current_island_id") + 1;
        int radius;
        if (profile.isLarge()) {
            radius = 500;
        } else {
            radius = 250;
        }

        CreationResponse response = Island.create(id, radius, profile.getTemplate());
        Island island = response.island();
        CompletableFuture<Boolean> generationTask = response.generationTask();
        if (response.type() != CreationResponse.ResponseType.SUCCESS || island == null) {
            player.sendMessage(Message.BUY_CREATION_ERROR.getString().replace("{type}", response.type().toString()).get());
            if (generationTask != null) {
                generationTask.cancel(true);
            }
            giveBack(p, cost, required);
            return;
        }

        ProtectedRegion region = island.getRegion();
        if (region == null) {
            player.sendMessage(Message.BUY_REGION_ERROR.getString().get());
            if (generationTask != null) {
                generationTask.cancel(true);
            }
            giveBack(p, cost, required);
            return;
        }

        if (generationTask == null) {
            p.sendMessage(Message.BUY_GENERATION_ERROR.getString().get());
            giveBack(p, cost, required);
            return;
        }

        BukkitTask completer = main.getServer().getScheduler().runTaskTimer(main, () -> {
            if (done || !generationTask.isDone()) {
                return;
            }
            region.getOwners().addPlayer(p.getUniqueId());
            main.getConfiguration().set("current_island_id", id);
            main.getLogger().info(p.getName() + " bought an island. Specifications: ID: " + id + ", Radius: " + radius + ", Type: " + profile.getTemplate().toString());

            player.sendMessage(Message.BUY_FINISHED.getString().replaceAll("{id}", "" + id).get());
            p.teleport(island.getSpawn());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            Location villagerLocation = island.getCenterLocation().add(0, 1, 0);
            main.getIslandWorld().spawnEntity(villagerLocation, EntityType.VILLAGER);
            main.getIslandWorld().spawnEntity(villagerLocation, EntityType.VILLAGER);
            done = true;
        }, 0, 2);
        main.getServer().getScheduler().runTaskLater(main, () -> {
            completer.cancel();
            if (!done) {
                p.sendMessage(Message.BUY_GENERATION_ERROR.getString().get());
                giveBack(p, cost, required);
            }
        }, 1200);
    }

    private void giveBack(Player p, int cost, int required) {
        main.getEconomy().depositPlayer(p, cost);
        p.getInventory().addItem(ItemBuilder.of(Material.BEEHIVE).amount(required)
                .name("§6Bienenstock §8» §eLevel §a300").asItem());
    }

}
