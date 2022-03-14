package de.leonheuer.skycave.islandsystem.models;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.enums.IslandTemplate;
import de.leonheuer.skycave.islandsystem.util.IslandUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class Islands {

    private static final IslandSystem main = IslandSystem.getPlugin(IslandSystem.class);

    /**
     * Creates a new island with the given id, radius and starter island template.
     * @param id The id of the island, also used for calculating the spiral location
     * @param radius The radius of the island
     * @param template The template for the starter island
     * @return The created Island
     */
    @NotNull
    public static CreationResponse create(int id, int radius, IslandTemplate template) {
        if (main.getIslandWorld() == null) {
            return new CreationResponse(CreationResponse.ResponseType.ISLAND_WORLD_UNLOADED, null, null);
        }
        File file = IslandUtils.getIslandSaveLocation(id, true);
        if (file == null) {
            return new CreationResponse(CreationResponse.ResponseType.SAVE_LOCATION_UNDEFINED, null, null);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Island island = new Island(id, radius, config, file);
        island.setSpawn(island.getCenterLocation());
        CompletableFuture<Boolean> generationTask = island.generateDefaultIsland(template);
        if (island.createRegion() == null) {
            return new CreationResponse(CreationResponse.ResponseType.WG_REGION_ERROR, null, generationTask);
        }
        config.set("radius", radius);
        config.set("spawn", island.getSpawn());
        config.set("creation_timestamp", island.getCreated().toString());
        config.set("template", template.toString());
        try {
            config.save(file);
            return new CreationResponse(CreationResponse.ResponseType.SUCCESS, island, generationTask);
        } catch (IOException e) {
            e.printStackTrace();
            return new CreationResponse(CreationResponse.ResponseType.FILE_ERROR, null, generationTask);
        }
    }

    /**
     * Imports and saves an island from raw data and saves it.
     * @param id The id
     * @param config The configuration
     * @param file The config file
     * @param bannedPlayers The list of banned uuids
     * @param radius The radius
     * @param spawn The spawn location
     * @param created The creation timestamp
     * @param template The island template
     * @return The imported island
     */
    @Nullable
    public static Island importAndSave(int id, @NotNull YamlConfiguration config, @NotNull File file, @NotNull List<UUID> bannedPlayers,
                                       int radius, Location spawn, LocalDateTime created, IslandTemplate template
    ) {
        if (template == null) {
            template = IslandTemplate.ICE;
        }
        Island island = new Island(id, config, file, bannedPlayers, radius, spawn, created, template);
        if (island.save()) {
            return island;
        }
        return null;
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
        return Islands.load(IslandUtils.nameToId(r.getId()));
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

        IslandTemplate template;
        String templateName = config.getString("template");
        if (templateName == null) {
            template = IslandTemplate.ICE;
        } else {
            try {
                template = IslandTemplate.valueOf(config.getString("template"));
            } catch (IllegalArgumentException e) {
                template = IslandTemplate.ICE;
            }
        }

        List<UUID> bannedPlayers = new ArrayList<>();
        for (String entry : config.getStringList("banned_uuids")) {
            bannedPlayers.add(UUID.fromString(entry));
        }

        return new Island(id, config, file, bannedPlayers, config.getInt("radius"),
                config.getObject("spawn", Location.class), created, template);
    }
    /**
     * Returns a list of all known islands.
     * @return The list of islands
     */
    @NotNull
    public static List<Island> listAll() {
        List<Island> result = new ArrayList<>();
        File dir = new File(main.getDataFolder(), "islands/");
        if (!dir.isDirectory()) {
            return result;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return result;
        }
        for (File f : files) {
            String name = f.getName().replace(".yml", "");
            if (!IslandUtils.isValidName(name)) {
                continue;
            }
            Island island = Islands.load(IslandUtils.nameToId(name));
            if (island != null) {
                result.add(island);
            }
        }
        return result;
    }

    /**
     * Returns a list of all known islands, optionally with filter.
     * @param sort The comparator to sort items according to
     * @return The list of islands
     */
    @NotNull
    public static List<Island> listAll(@NotNull Comparator<Island> sort) {
        List<Island> result = listAll();
        result.sort(sort);
        return result;
    }

}
