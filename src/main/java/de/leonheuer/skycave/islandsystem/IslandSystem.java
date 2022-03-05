package de.leonheuer.skycave.islandsystem;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonheuer.mcguiapi.gui.GUIFactory;
import de.leonheuer.skycave.islandsystem.cmd.SBAdminCommand;
import de.leonheuer.skycave.islandsystem.cmd.SBCommand;
import de.leonheuer.skycave.islandsystem.listener.CreatureSpawnListener;
import de.leonheuer.skycave.islandsystem.listener.PlayerCommandListener;
import de.leonheuer.skycave.islandsystem.listener.WorldLoadListener;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import de.leonheuer.skycave.islandsystem.manager.WarpManager;
import de.leonheuer.skycave.islandsystem.models.AutoSaveConfig;
import de.leonheuer.skycave.islandsystem.models.PrefixHolder;
import de.leonheuer.skycave.islandsystem.util.FileUtils;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class IslandSystem extends JavaPlugin implements PrefixHolder {

    public static final int ISLAND_DISTANCE = 4000;
    private WarpManager warpManager;
    private RegionContainer regionContainer;
    private GUIFactory guiFactory;
    private MultiverseCore multiverse;
    private LimitManager limitManager;
    private World islandWorld;
    private AutoSaveConfig config;

    @Override
    public void onEnable() {
        // resources
        if (!reloadResources()) {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Unable to load all plugin resources.");
            return;
        }

        // other dependencies
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        multiverse = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        guiFactory = new GUIFactory(this);

        // managers
        warpManager = new WarpManager(this);

        limitManager = new LimitManager(this);
        String worldName = config.getString("world_name");
        if (worldName != null && (islandWorld = getServer().getWorld(worldName)) != null) {
            limitManager.start(islandWorld);
        }

        // commands
        registerCommand("sb", new SBCommand(this));
        registerCommand("sbadmin", new SBAdminCommand(this));

        // listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerCommandListener(this), this);
        pm.registerEvents(new WorldLoadListener(this), this);
        pm.registerEvents(new CreatureSpawnListener(this), this);
    }

    /**
     * Reloads all configurations of the plugin.
     * Copies resources of the plugin in the data folder if they are missing.
     * @return Whether reloading succeeded.
     */
    public boolean reloadResources() {
        if (!getDataFolder().isDirectory() && !getDataFolder().mkdirs()) {
            return false;
        }
        File islandDir = new File(getDataFolder(), "islands/");
        if (!islandDir.isDirectory() && !islandDir.mkdirs()) {
            return false;
        }

        File warpConfig = new File(getDataFolder(), "warps.yml");
        if (!warpConfig.isFile()) {
            try {
                if (!warpConfig.createNewFile()) {
                    return false;
                }
                warpManager.reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            warpManager.reloadConfig();
        }

        if (FileUtils.copyResource(this, "config.yml")) {
            config = new AutoSaveConfig(new File(getDataFolder(), "config.yml"));
        } else {
            return false;
        }
        return true;
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

    @NotNull
    public AutoSaveConfig getConfiguration() {
        return config;
    }

    @Override
    public String getPrefix() {
        return "&8❙ &6SB&fInseln &8» ";
    }
}
