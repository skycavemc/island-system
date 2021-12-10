package de.leonheuer.skycave.islandsystem.listener;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.enums.EntityLimitType;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTitle().equals("§6§lSB§f-§lInsel §cLimits")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) {
                return;
            }

            EntityLimitType limitType = EntityLimitType.getByMat(event.getCurrentItem().getType());
            if (limitType == null) {
                if (event.getCurrentItem().getType() == Material.OAK_DOOR) {
                    player.openInventory(Utils.getLimitGui());
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                }
                return;
            }
            ProtectedRegion islandRegion = Utils.getIslandRegionAt(player.getLocation());
            player.openInventory(Utils.getLimitGui(islandRegion, limitType));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

}
