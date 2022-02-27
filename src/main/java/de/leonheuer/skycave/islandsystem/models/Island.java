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
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class Island {

    private final int id;
    private final SpiralLocation spiralLocation;
    private int radius;
    private Location spawn;
    private LocalDateTime created;
    private YamlConfiguration config;
    private File file;

    public Island(int id, int radius, Location spawn, LocalDateTime created,
                  YamlConfiguration config, File file) {
        this.id = id;
        this.spiralLocation = SpiralLocation.of(id);
        this.radius = radius;
        this.spawn = spawn;
        this.created = created;
        this.config = config;
        this.file = file;
    }

    private Island(int id, int radius) {
        this.id = id;
        spiralLocation = SpiralLocation.of(id);
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public SpiralLocation getSpiralLocation() {
        return spiralLocation;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Nullable
    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public LocalDateTime getCreated() {
        if (created == null) {
            created = LocalDateTime.now();
        }
        return created;
    }

    public String getName() {
        return IslandUtils.idToName(id);
    }

    public Location getCenterLocation() {
        return new Location(Utils.getInselWorld(),
                spiralLocation.getX(radius), 64, spiralLocation.getZ(radius), 0, 1);
    }

    @Nullable
    public ProtectedRegion getRegion() {
        RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = rc.get(BukkitAdapter.adapt(Utils.getInselWorld()));
        if (rm == null) {
            return null;
        }
        return rm.getRegion(getName());
    }

    @Nullable
    private ProtectedRegion createRegion() {
        RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = rc.get(BukkitAdapter.adapt(Utils.getInselWorld()));
        if (rm == null) {
            return null;
        }

        ProtectedCuboidRegion region = spiralLocation.asRegion(getName(), radius);
        rm.addRegion(region);
        return region;
    }

    private boolean generateDefaultIsland(@NotNull IslandTemplate template) {
        boolean success = Utils.printSchematic(
                spiralLocation.getX(radius), 64, spiralLocation.getZ(radius), template.getFile());
        if (success) {
            Location villagerLocation = getCenterLocation();
            Utils.getInselWorld().spawnEntity(villagerLocation.add(0, 2, 0), EntityType.VILLAGER);
            Utils.getInselWorld().spawnEntity(villagerLocation.add(1, 2, 0), EntityType.VILLAGER);
        }
        return success;
    }

    public boolean save() {
        if (file == null || config == null) {
            return false;
        }

        config.set("radius", radius);
        config.set("spawn", spawn);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    public static Island create(int id, int radius, IslandTemplate template) throws IOException {
        File dir = new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "island/");
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        DecimalFormat format = new DecimalFormat("000");
        String name = "sc_" + format.format(id);
        File file = new File(dir, name + ".yml");
        if (!file.isFile()) {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Island island = new Island(id, radius);
        island.setSpawn(island.getCenterLocation());
        if (!island.generateDefaultIsland(template)) {
            return null;
        }
        if (island.createRegion() == null) {
            return null;
        }
        config.set("id", id);
        config.set("radius", radius);
        config.set("spawn", island.getSpawn());
        config.set("creation_timestamp", island.getCreated().toString());
        config.save(file);
        Utils.addLastID();
        return island;
    }

    @Nullable
    public static Island load(int id) {
        File dir = new File(JavaPlugin.getPlugin(IslandSystem.class).getDataFolder(), "island/");
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
            return null;
        }

        DecimalFormat format = new DecimalFormat("000");
        String name = "sc_" + format.format(id);
        File file = new File(dir, name + ".yml");
        if (!file.isFile()) {
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
