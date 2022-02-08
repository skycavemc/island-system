package de.leonheuer.skycave.islandsystem.models;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import de.leonheuer.skycave.islandsystem.util.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Insel {

    private final IslandSystem main = JavaPlugin.getPlugin(IslandSystem.class);
    private final RegionManager rm = main.getRegionContainer().get(BukkitAdapter.adapt(main.getIslandWorld()));
    private String ID;

    public Insel(String id) {
        setID(id);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isOwner(UUID uuid) {
        ProtectedRegion r = rm.getRegion(getID());
        return r.getOwners().contains(uuid);
    }

    public UUID getOwner() {
        ProtectedRegion r = rm.getRegion(getID());
        for (UUID u : r.getOwners().getUniqueIds()) {
            return u;
        }
        return null;
    }

    public void setOwner(UUID uuid) {
        ProtectedRegion r = rm.getRegion(getID());
        FileConfiguration insel = Utils.getInsel(getID());
        insel.set("old-owner", getOwner());
        r.getOwners().removeAll();
        r.getOwners().addPlayer(uuid);
        insel.set("owner", uuid.toString());
        Utils.saveInsel(getID(), insel);
    }

    public void addMember(UUID uuid) {
        ProtectedRegion r = rm.getRegion(getID());
        r.getMembers().addPlayer(uuid);
        FileConfiguration f = Utils.getInsel(getID());
        List<String> d = f.getStringList("member");
        d.add(uuid.toString());
        f.set("member", d);
        Utils.saveInsel(getID(), f);
    }

    public ArrayList<UUID> getMemeber() {
        ProtectedRegion r = rm.getRegion(getID());
        return new ArrayList<>(r.getMembers().getUniqueIds());
    }

    public boolean isMember(UUID uuid) {
        ProtectedRegion r = rm.getRegion(getID());
        return r.getMembers().contains(uuid);
    }

    public void removeMember(UUID uuid) {
        ProtectedRegion r = rm.getRegion(getID());
        r.getMembers().removePlayer(uuid);
        FileConfiguration f = Utils.getInsel(getID());
        List<String> d = f.getStringList("member");
        d.remove(uuid.toString());
        f.set("member", d);
        Utils.saveInsel(getID(), f);
    }

    public void setTP(Location loc) {
        FileConfiguration insel = Utils.getInsel(getID());
        insel.set("tppunkt.x", loc.getX());
        insel.set("tppunkt.y", loc.getY());
        insel.set("tppunkt.z", loc.getZ());
        insel.set("tppunkt.yaw", loc.getYaw());
        insel.set("tppunkt.pitch", loc.getPitch());
        Utils.saveInsel(getID(), insel);
    }

}
