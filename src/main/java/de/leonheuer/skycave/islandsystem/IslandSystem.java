package de.leonheuer.skycave.islandsystem;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonheuer.mcguiapi.gui.GUIFactory;
import de.leonheuer.skycave.islandsystem.cmd.SBAdminCommand;
import de.leonheuer.skycave.islandsystem.cmd.SBCommand;
import de.leonheuer.skycave.islandsystem.config.CacheConfig;
import de.leonheuer.skycave.islandsystem.manager.WarpManager;
import de.leonheuer.skycave.islandsystem.listener.CreatureSpawnListener;
import de.leonheuer.skycave.islandsystem.listener.PlayerCommandListener;
import de.leonheuer.skycave.islandsystem.listener.WorldLoadListener;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandSystem extends JavaPlugin {

    public static final String PREFIX = "&8❙ &6SB&fInseln &8» ";
    public static final int ISLAND_DISTANCE = 4000;
    private WarpManager warpManager;
    private CacheConfig cacheConfig;
    private RegionContainer regionContainer;
    private GUIFactory guiFactory;
    private MultiverseCore multiverse;
    private LimitManager limitManager;
    private World islandWorld;

    @Override
    public void onEnable() {
        cacheConfig = new CacheConfig();
        warpManager = new WarpManager();
        warpManager.reloadConfig();
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        guiFactory = new GUIFactory(this);
        multiverse = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");

        limitManager = new LimitManager(this);
        islandWorld = getServer().getWorld("skybeeisland");
        if (islandWorld != null) {
            limitManager.start(islandWorld);
        }

        registerCommand("sb", new SBCommand(this));
        registerCommand("sbadmin", new SBAdminCommand(this));

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerCommandListener(), this);
        pm.registerEvents(new WorldLoadListener(this), this);
        pm.registerEvents(new CreatureSpawnListener(this), this);
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand cmd = getCommand(command);
        if (cmd == null) {
            getLogger().severe("No entry for command " + command + " found in the plugin.yml.");
            return;
        }
        cmd.setExecutor(executor);
    }

    @Override
    public void onDisable() {
        limitManager.stopAll();
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public RegionContainer getRegionContainer() {
        return regionContainer;
    }

    public GUIFactory getGuiFactory() {
        return guiFactory;
    }

    public MultiverseCore getMultiverse() {
        return multiverse;
    }

    public LimitManager getLimitManager() {
        return limitManager;
    }

    public World getIslandWorld() {
        return islandWorld;
    }

    public void setIslandWorld(World islandWorld) {
        this.islandWorld = islandWorld;
    }
}
