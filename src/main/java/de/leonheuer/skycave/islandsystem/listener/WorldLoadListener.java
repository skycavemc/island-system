package de.leonheuer.skycave.islandsystem.listener;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    private final IslandSystem main;

    public WorldLoadListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (event.getWorld().getName().equals("skybeeisland")) {
            main.getLimitManager().start(event.getWorld());
        }
    }

}
