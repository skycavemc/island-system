package de.leonheuer.skycave.islandsystem;

import de.leonheuer.skycave.islandsystem.cmd.SBAdminCommand;
import de.leonheuer.skycave.islandsystem.cmd.SBCommand;
import de.leonheuer.skycave.islandsystem.config.CacheConfig;
import de.leonheuer.skycave.islandsystem.config.GameConfig;
import de.leonheuer.skycave.islandsystem.config.WarpsConfig;
import de.leonheuer.skycave.islandsystem.listener.CreatureSpawnListener;
import de.leonheuer.skycave.islandsystem.listener.EntityDeathListener;
import de.leonheuer.skycave.islandsystem.listener.InventoryClickListener;
import de.leonheuer.skycave.islandsystem.listener.PlayerCommandListener;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandSystem extends JavaPlugin {

    public static final String PREFIX = "&8❙ &fSky&6Bee&fIsland &8» ";
    private WarpsConfig warpsConfig;
    private GameConfig gameConfig;
    private CacheConfig cacheConfig;
    private LimitManager limitManager;

    @Override
    public void onEnable() {
        gameConfig = new GameConfig();
        cacheConfig = new CacheConfig();
        warpsConfig = new WarpsConfig();
        limitManager = new LimitManager(this);

        this.getCommand("sb").setExecutor(new SBCommand(this));
        this.getCommand("sbadmin").setExecutor(new SBAdminCommand(this));

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerCommandListener(), this);
        pm.registerEvents(new CreatureSpawnListener(this), this);
        pm.registerEvents(new EntityDeathListener(this), this);
        pm.registerEvents(new InventoryClickListener(), this);
    }

    public WarpsConfig getWarpsConfig() {
        return warpsConfig;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public LimitManager getLimitManager() {
        return limitManager;
    }
}
