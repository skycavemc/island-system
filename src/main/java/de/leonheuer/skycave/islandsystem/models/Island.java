package de.leonheuer.skycave.islandsystem.models;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Island {

    private static final IslandSystem main = JavaPlugin.getPlugin(IslandSystem.class);
    private final int id;
    private final SpiralLocation spiralLocation;
    private final YamlConfiguration config;
    private final File file;
    private int radius;
    private Location spawn;
    private LocalDateTime created;

    // For internal use only
    private Island(int id, int radius, Location spawn, @NotNull LocalDateTime created,
                  @NotNull YamlConfiguration config, @NotNull File file) {
        this.id = id;
        this.spiralLocation = SpiralLocation.of(id, 1, 1);
        this.radius = radius;
        this.spawn = spawn;
        this.created = created;
        this.config = config;
        this.file = file;
    }

    // For internal use only
    private Island(int id, int radius, @NotNull YamlConfiguration config, @NotNull File file) {
        this.id = id;
        spiralLocation = SpiralLocation.of(id, 1, 1);
        this.radius = radius;
        this.config = config;
        this.file = file;
    }

    /**
     * Gets the unique serial ID of the island.
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the relative location in the spiral pattern of islands.
     * @return The spiral location
     */
    public SpiralLocation getSpiralLocation() {
        return spiralLocation;
    }

    /**
     * Gets the radius of the island.
     * @return The radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the island and automatically adjusts the region size.
     * @param radius The new radius
     */
    public void setRadius(int radius) {
        RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = rc.get(BukkitAdapter.adapt(main.getIslandWorld()));
        if (rm == null) {
            return;
        }

        this.radius = radius;
        ProtectedRegion oldRegion = rm.getRegion(getName());
        ProtectedRegion newRegion = spiralLocation.asRegion(getName(), radius);
        if (oldRegion != null) {
            newRegion.copyFrom(oldRegion);
            rm.removeRegion(getName());
        }
        rm.addRegion(newRegion);
        save();
    }

    /**
     * Gets the spawn location that has been set for the island.
     * @return The spawn location
     */
    @NotNull
    public Location getSpawn() {
        if (spawn == null) {
            return getCenterLocation();
        }
        return spawn;
    }

    /**
     * Sets the spawn location of the island.
     * @param spawn The new spawn location
     */
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
        save();
    }

    /**
     * Gets the timestamp of when the island has been created.
     * @return The timestamp as LocalDateTime
     */
    @NotNull
    public LocalDateTime getCreated() {
        if (created == null) {
            created = LocalDateTime.now();
        }
        return created;
    }

    /**
     * Gets the name of this island, usually following the pattern sc_??? where ??? is the id with leading zeros.
     * Example: id = 43, name = sc_043
     * @return The name
     */
    public String getName() {
        return IslandUtils.idToName(id);
    }

    /**
     * Gets the center location of the island.
     * @return The center location
     */
    public Location getCenterLocation() {
        return new Location(main.getIslandWorld(),
                spiralLocation.getX() * IslandSystem.ISLAND_DISTANCE, 64,
                spiralLocation.getZ() * IslandSystem.ISLAND_DISTANCE, 0, 1);
    }

    /**
     * Gets the WorldGuard region of this island. Use this to set the owner, add/remove members etc.
     * @return The region
     */
    @Nullable
    public ProtectedRegion getRegion() {
        RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = rc.get(BukkitAdapter.adapt(main.getIslandWorld()));
        if (rm == null) {
            return null;
        }
        return rm.getRegion(getName());
    }

    @Nullable
    private ProtectedRegion createRegion() {
        RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = rc.get(BukkitAdapter.adapt(main.getIslandWorld()));
        if (rm == null) {
            return null;
        }

        if (rm.hasRegion(getName())) {
            rm.removeRegion(getName());
        }

        ProtectedCuboidRegion region = spiralLocation.asRegion(getName(), radius);
        rm.addRegion(region);
        return region;
    }

    private boolean generateDefaultIsland(@NotNull IslandTemplate template) {
        boolean success = IslandUtils.printSchematic(
                spiralLocation.getX() * IslandSystem.ISLAND_DISTANCE, 64,
                spiralLocation.getZ() * IslandSystem.ISLAND_DISTANCE, template.getFile());
        if (success) {
            Location villagerLocation = getCenterLocation();
            main.getIslandWorld().spawnEntity(villagerLocation.add(0, 2, 0), EntityType.VILLAGER);
            main.getIslandWorld().spawnEntity(villagerLocation.add(1, 2, 0), EntityType.VILLAGER);
        }
        return success;
    }

    /**
     * Saves the island to its file.
     */
    public void save() {
        if (file == null || config == null) {
            return;
        }

        config.set("radius", radius);
        config.set("spawn", spawn);
        config.set("creation_timestamp", created.toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new island with the given id, radius and starter island template.
     * @param id The id of the island, also used for calculating the spiral location
     * @param radius The radius of the island
     * @param template The template for the starter island
     * @return The created Island
     * @throws IOException If the island could not be saved.
     */
    @Nullable
    public static Island create(int id, int radius, IslandTemplate template) throws IOException {
        if (main.getIslandWorld() == null) {
            main.getLogger().severe("Island world is not loaded. Island could not be created.");
            return null;
        }
        File file = IslandUtils.getIslandSaveLocation(id, true);
        if (file == null) {
            return null;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Island island = new Island(id, radius, config, file);
        island.setSpawn(island.getCenterLocation());
        if (!island.generateDefaultIsland(template)) {
            return null;
        }
        if (island.createRegion() == null) {
            return null;
        }
        config.set("radius", radius);
        config.set("spawn", island.getSpawn());
        config.set("creation_timestamp", island.getCreated().toString());
        config.save(file);
        return island;
    }

    /**
     * Imports and saves an island from raw data and saves it.
     * @param id The id
     * @param radius The radius
     * @param spawn The spawn location
     * @param created The creation timestamp
     * @param config The configuration
     * @param file The config file
     */
    public static void importAndSave(int id, int radius, Location spawn, @NotNull LocalDateTime created,
                                     @NotNull YamlConfiguration config, @NotNull File file) {
        Island island = new Island(id, radius, spawn, created, config, file);
        island.save();
    }

    /**
     * Gets the island at the given location. Will return null if none is found.
     * @param location The location to search around
     * @return The island
     */
    @Nullable
    public static Island at(Location location) {
        ProtectedRegion r = IslandUtils.getIslandRegionAt(location);
        if (r == null) {
            return null;
        }
        return Island.load(IslandUtils.nameToId(r.getId()));
    }

    /**
     * Loads the island with the given id from its file. WIll return null if no island with the given id exists.
     * @param id The id
     * @return The island
     */
    @Nullable
    public static Island load(int id) {
        if (main.getIslandWorld() == null) {
            main.getLogger().severe("Island world is not loaded. Island could not be loaded.");
            return null;
        }
        File file = IslandUtils.getIslandSaveLocation(id, false);
        if (file == null) {
            return null;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        LocalDateTime created;
        String timestamp = config.getString("creation_timestamp");
        if (timestamp == null) {
            created = LocalDateTime.now();
        } else {
            created = LocalDateTime.parse(timestamp);
        }

        return new Island(
                id, config.getInt("radius"), config.getObject("spawn", Location.class),
                created, config, file
        );
    }

}
