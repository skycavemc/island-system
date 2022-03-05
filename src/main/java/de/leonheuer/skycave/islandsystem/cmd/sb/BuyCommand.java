package de.leonheuer.skycave.islandsystem.cmd.sb;

import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BuyCommand {

    public BuyCommand(Player player, IslandSystem main) {
        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem());
        GUI gui = main.getGuiFactory().createGUI(3, "§21. Sorte auswählen")
                .formatPattern(pattern.startAtLine(1))
                .formatPattern(pattern.startAtLine(3));
    }
}
