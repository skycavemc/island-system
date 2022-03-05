package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyCommand {

    public BuyCommand(@NotNull Player player, @NotNull IslandSystem main) {
        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem());
        GUI gui = main.getGuiFactory().createGUI(3, "§21. Starter-Insel auswählen")
                .formatPattern(pattern.startAtLine(1))
                .formatPattern(pattern.startAtLine(3));
        for (IslandTemplate type : IslandTemplate.values()) {

        }
    }
}
