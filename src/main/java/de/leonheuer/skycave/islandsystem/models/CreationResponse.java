package de.leonheuer.skycave.islandsystem.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public record CreationResponse(@NotNull ResponseType type, @Nullable Island island,
                               @Nullable CompletableFuture<Boolean> generationTask) {

    public enum ResponseType {
        ISLAND_WORLD_UNLOADED,
        SAVE_LOCATION_UNDEFINED,
        WG_REGION_ERROR,
        FILE_ERROR,
        SUCCESS
    }


}
