package de.leonheuer.skycave.islandsystem;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonheuer.mcguiapi.gui.GUIFactory;
import de.leonheuer.skycave.islandsystem.cmd.SBAdminCommand;
import de.leonheuer.skycave.islandsystem.cmd.SBCommand;
import de.leonheuer.skycave.islandsystem.listener.CreatureSpawnListener;
import de.leonheuer.skycave.islandsystem.listener.EntryListener;
import de.leonheuer.skycave.islandsystem.listener.PlayerCommandListener;
import de.leonheuer.skycave.islandsystem.listener.WorldLoadListener;
import de.leonheuer.skycave.islandsystem.manager.LimitManager;
import de.leonheuer.skycave.islandsystem.manager.WarpManager;
import de.leonheuer.skycave.islandsystem.models.AutoSaveConfig;
import de.leonheuer.skycave.islandsystem.models.PrefixHolder;
import de.leonheuer.skycave.islandsystem.models.SelectionProfile;
import de.leonheuer.skycave.islandsystem.models.User;
import de.leonheuer.skycave.islandsystem.util.FileUtils;
import net.milkbowl.vault.economy.Economy;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class IslandSystem extends JavaPlugin implements PrefixHolder {

    private final HashMap<UUID, SelectionProfile> selectionProfiles = new HashMap<>();
    private RegionContainer regionContainer;
    private MultiverseCore multiverse;
    private GUIFactory guiFactory;
    private Economy economy;
    private WarpManager warpManager;
    private LimitManager limitManager;
    private World islandWorld;
    private AutoSaveConfig config;
    private MongoClient mongoClient;
    private MongoCollection<User> users;
    private boolean working = false;

    @Override
    public void onEnable() {
        // dependencies
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        multiverse = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        guiFactory = new GUIFactory(this);
        if ((economy = setupEconomy()) == null) {
            getLogger().severe("Vault Economy no initialized. Some features of this plugin will not work.");
        }

        // database
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder().codecRegistry(codecRegistry).build();
        mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("island_system");
        users = db.getCollection("users", User.class);

        // managers
        warpManager = new WarpManager(this);
        limitManager = new LimitManager(this);

        // resources
        if (!reloadResources()) {
            getServer().getPluginManager().disablePlugin(this);
            getLogger().severe("Unable to load all plugin resources.");
            return;
        }
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
        pm.registerEvents(new EntryListener(this), this);
        pm.registerEvents(new CreatureSpawnListener(this), this);
    }

    @Override
    public void onDisable() {
        if (limitManager != null) {
            limitManager.stopAll();
        }
        mongoClient.close();
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
                    getLogger().info("The file warps.yml could not be created.");
                    return false;
                }
                getLogger().info("The file warps.yml has been created.");
                warpManager.reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().info("The file warps.yml could not be created.");
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

    private @Nullable Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }

    private void registerCommand(String command, CommandExecutor executor) {
        PluginCommand cmd = getCommand(command);
        if (cmd == null) {
            getLogger().severe("No entry for command " + command + " found in the plugin.yml.");
            return;
        }
        cmd.setExecutor(executor);
    }

    public HashMap<UUID, SelectionProfile> getSelectionProfiles() {
        return selectionProfiles;
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

    public Economy getEconomy() {
        return economy;
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

    @NotNull
    public MongoCollection<User> getUsers() {
        return users;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public boolean isWorking() {
        return working;
    }

    @Override
    public String getPrefix() {
        return "&8??? &6SB&fInseln &8?? ";
    }

    @NotNull
    public Location getServerSpawn() {
        return multiverse.getMVWorldManager().getMVWorld(config.getString("spawn_world_name")).getSpawnLocation();
    }

    public int getIslandDistance() {
        return config.getInt("island_distance");
    }
}
