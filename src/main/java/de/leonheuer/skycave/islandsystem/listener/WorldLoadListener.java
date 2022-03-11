package de.leonheuer.skycave.islandsystem.listener;

import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.bukkit.World;
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
        String worldName = main.getConfiguration().getString("world_name");
        World world = event.getWorld();
        if (worldName != null && main.getIslandWorld() == null && world.getName().equals(worldName)) {
            main.setIslandWorld(world);
            main.getLimitManager().start(world);
            main.getLogger().info("§2Island world loaded.");
        }
    }

}
