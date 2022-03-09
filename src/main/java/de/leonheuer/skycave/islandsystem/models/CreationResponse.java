package de.leonheuer.skycave.islandsystem.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CreationResponse(@NotNull ResponseType type, @Nullable Island island) {

    public enum ResponseType {
        ISLAND_WORLD_UNLOADED,
        SAVE_LOCATION_UNDEFINED,
        GENERATION_ERROR,
        WG_REGION_ERROR,
        FILE_ERROR,
        SUCCESS,
        UNKNOWN
    }

    @NotNull
    public ResponseType getType() {
        return type;
    }

    @Nullable
    public Island getIsland() {
        return island;
    }
}
