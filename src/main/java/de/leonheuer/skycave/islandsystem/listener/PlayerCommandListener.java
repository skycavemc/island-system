package de.leonheuer.skycave.islandsystem.listener;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlayerCommandListener implements Listener {

    private final IslandSystem main;

    public PlayerCommandListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onCommand(@NotNull PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skybee.island.alert.manual") &&
                player.getLocation().getWorld() != main.getIslandWorld() &&
                (event.getMessage().startsWith("/region ") ||
                        event.getMessage().startsWith("/rg "))
        ) {
            player.sendMessage(Message.REGION_ACCESS.getString().get());
        }
    }
}
