package de.leonheuer.skycave.islandsystem.util;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.EntityLimit;
import de.leonheuer.skycave.islandsystem.enums.EntityLimitType;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Utils {

    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    /**
     * Gets the specific in-game view for checking the island limits.
     * @param region The region where to count entities
     * @param limitType The category of limit types
     * @return The created GUI
     */
    public static @NotNull GUI getLimitGui(ProtectedRegion region, EntityLimitType limitType) {
        GUI gui = main.getGuiFactory().createGUI(3, "§6§lSB§f-§lInsel §cLimits");

        List<EntityLimit> limits = new ArrayList<>();
        for (EntityLimit l : EntityLimit.values()) {
            if (l.getLimitType() != limitType) {
                continue;
            }
            limits.add(l);
        }
        Collections.sort(limits);

        int i = 0;
        if (region == null) {
            for (EntityLimit l : limits) {
                gui.setItem(i, ItemBuilder.of(l.getSpawnEgg())
                        .name("§e" + entityTypeAsString(l.getType()) + " §6(" + l.getLimit() + ")")
                        .asItem(), false);
                i++;
            }
        } else {
            for (EntityLimit l : limits) {
                int count = main.getLimitManager().getEntityCount(region.getId(), l.getType());

                String color;
                if (count >= l.getLimit()) {
                    color = "§c";
                } else {
                    color = "§a";
                }

                gui.setItem(i, ItemBuilder.of(l.getSpawnEgg())
                                .name("§e" + entityTypeAsString(l.getType()) + " " + color + "(" + count + "/" + l.getLimit() + ")")
                                .asItem(), false);
                i++;
            }
        }

        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem());
        gui.formatPattern(pattern.startAtLine(3))
                .setItem(3, 9, ItemBuilder.of(Material.OAK_DOOR).name("§cZurück").asItem(), false,
                        event -> {
                            Player player = (Player) event.getWhoClicked();
                            getLimitGui().show((Player) event.getWhoClicked());
                            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                        });
        return gui;
    }

    /**
     * Gets the main in-game view of the island limits.
     * @return The created GUI
     */
    public static @NotNull GUI getLimitGui() {
        GUI gui = main.getGuiFactory().createGUI(3, "§6§lSB§f-§lInsel §cLimits");

        int i = 9;
        for (EntityLimitType t : EntityLimitType.values()) {
            gui.setItem(i, ItemBuilder.of(t.getMat()).name("§6§l" + t.getName()).asItem(), false,
                    (event) -> {
                        Player player = (Player) event.getWhoClicked();
                        ProtectedRegion region = IslandUtils.getIslandRegionAt(player.getLocation());
                        getLimitGui(region, t).show(player);
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    });
            i++;
        }

        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem());
        gui.formatPattern(pattern.startAtLine(1)).formatPattern(pattern.startAtLine(3));
        return gui;
    }

    /**
     * Transforms a minecraft entity type to a well readable string.
     * @param type The entity type to transform
     * @return The result
     */
    public static String entityTypeAsString(@NotNull EntityType type) {
        StringJoiner sj = new StringJoiner(" ");
        String[] partial = type.toString().split("_");
        Arrays.stream(partial).forEach(part -> {
            part = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            sj.add(part);
        });
        return sj.toString();
    }

    /**
     * Transforms a location into a string containing the x, y and z components of the location.
     * @param location The location to transform
     * @return The result
     */
    public static @NotNull String locationAsString(@NotNull Location location) {
        return location.getX() + ", " +
                location.getY() + ", " +
                location.getZ();
    }

    /**
     * Transforms a BlockVector3 into a string containing the x, y and z components of the vector.
     * @param location The BlockVector3 to transform
     * @return The result
     */
    public static @NotNull String locationAsString(@NotNull BlockVector3 location) {
        return location.getX() + ", " +
                location.getY() + ", " +
                location.getZ();
    }

}
