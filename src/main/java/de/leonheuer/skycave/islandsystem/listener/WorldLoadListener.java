package de.leonheuer.skycave.islandsystem.listener;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

@SuppressWarnings("unused")
public class WorldLoadListener implements Listener {

    private final IslandSystem main;

    public WorldLoadListener(IslandSystem main) {
        this.main = main;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (main.getIslandWorld() == null && event.getWorld().getName().equals("skybeeisland")) {
            main.getLimitManager().start(event.getWorld());
            main.setIslandWorld(event.getWorld());
            main.getLogger().info("§2Island world loaded.");
        }
    }

}
