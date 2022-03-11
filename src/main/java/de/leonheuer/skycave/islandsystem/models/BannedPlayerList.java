package de.leonheuer.skycave.islandsystem.models;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BannedPlayerList {

    private final Island island;
    private final List<UUID> bannedPlayers;

    protected BannedPlayerList(Island island, @NotNull List<UUID> bannedPlayers) {
        this.island = island;
        this.bannedPlayers = bannedPlayers;
    }

    /**
     * Adds a UUID to the list of banned players and saves the island.
     * @param uuid The UUID of the player to ban
     */
    public void add(@NotNull UUID uuid) {
        if (!bannedPlayers.contains(uuid)) {
            bannedPlayers.add(uuid);
        }
        island.save();
    }
    /**
     * Adds all the UUIDs to the list of banned players and saves the island.
     * @param uuidList The list of UUIDs of the players to ban
     */
    public void addAll(@NotNull List<UUID> uuidList) {
        for (UUID u : uuidList) {
            if (!bannedPlayers.contains(u)) {
                bannedPlayers.add(u);
            }
        }
        island.save();
    }

    /**
     * Removes a UUID from the list of banned players and saves the island.
     * @param uuid The UUID of the player to unban
     */
    public void remove(@NotNull UUID uuid) {
        bannedPlayers.remove(uuid);
        island.save();
    }

    /**
     * Removes a UUID from the list of banned players and saves the island.
     * @param uuidList The list of UUIDs of the players to unban
     */
    public void removeAll(@NotNull List<UUID> uuidList) {
        bannedPlayers.removeAll(uuidList);
        island.save();
    }

    /**
     * Gets whether a UUID is listed as banned from the island.
     * @param uuid The UUID
     * @return Whether it is banned
     */
    public boolean contains(@NotNull UUID uuid) {
        return bannedPlayers.contains(uuid);
    }

    /**
     * Gets a list of all UUIDs that are currently banned on the island.
     * @return An unmodifiable list of UUIDs
     */
    @NotNull
    public List<UUID> getUniqueIds() {
        return Collections.unmodifiableList(bannedPlayers);
    }
}
