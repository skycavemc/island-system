package de.leonheuer.skycave.islandsystem.listener;

import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@SuppressWarnings("unused")
public class PlayerCommandListener implements Listener {

    @EventHandler
    public void onCMD(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("skybee.island.alert.manual")) {
            if (player.getLocation().getWorld().getName().equalsIgnoreCase("skybeeisland")) {
                if (e.getMessage().startsWith("/region") || e.getMessage().startsWith("/rg")) {
                    player.sendMessage(Message.PLUGIN_MANUAL_WARNUNG.getString().get());
                }
            }
        }
    }
}
