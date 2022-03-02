package de.leonheuer.skycave.islandsystem.models;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.leonheuer.skycave.islandsystem.IslandSystem;
import org.jetbrains.annotations.NotNull;

public class SpiralLocation {

    private final int left = 0;
    private final int right = 1;
    private final int up = 2;
    private final int down = 3;
    private final int x;
    private final int z;

    public SpiralLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static SpiralLocation of(int index, int startX, int startZ) {
        SpiralLocation result = new SpiralLocation(startX, startZ);
        if (index <= 0) {
            return result;
        }
        for (int i = 0; i < index; i++) {
            result = result.next();
        }
        return result;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public BlockVector3 getStartVector(int radius) {
        return BlockVector3.at(x * IslandSystem.ISLAND_DISTANCE - radius, -64, x * IslandSystem.ISLAND_DISTANCE - radius);
    }

    public BlockVector3 getEndVector(int radius) {
        return BlockVector3.at(x * IslandSystem.ISLAND_DISTANCE + radius, 320, x * IslandSystem.ISLAND_DISTANCE + radius);
    }

    public ProtectedCuboidRegion asRegion(String name, int radius) {
        return new ProtectedCuboidRegion(name, getStartVector(radius), getEndVector(radius));
    }

    @NotNull
    public SpiralLocation next() {
        int x = Math.abs(this.x);
        int z = Math.abs(this.z);
        
        if (x > z) {
            // we're on a right or left line somewhere between the corners
            if (this.x > 0) {
                // right line
                return move(up);
            } else {
                // left line
                return move(down);
            }
        }
        
        if (x < z) {
            // we're on a top or bottom line somewhere between the corners
            if (this.z > 0) {
                // top line
                return move(left);
            } else {
                // bottom line
                return move(right);
            }
        }

        // if x == z then we're in a corner
        if (this.x >= 0) {
            // a corner with positive x is the top-right or bottom-right corner of a square
            // either way we have to move up, to start the right line or to start a new square
            return move(up);
        }
        if (this.z >= 0) {
            // a corner with negative x and positive z is the top-left corner of a square
            // that means we have to move down to start the left line
            return move(down);
        }
        // a corner with negative x and negative z is the bottom-left corner of a square
        // that means we have to move right to start the bottom line
        return move(right);
        // we don't ever need to move left from a corner, otherwise we would just loop over the same square
    }

    @NotNull
    private SpiralLocation move(int direction) {
        return switch (direction) {
            case left -> new SpiralLocation(this.x - 1, this.z);
            case right -> new SpiralLocation(this.x + 1, this.z);
            case up -> new SpiralLocation(this.x, this.z + 1);
            case down -> new SpiralLocation(this.x, this.z - 1);
            default -> throw new IllegalArgumentException(direction + " is an illegal direction index.");
        };
    }

}
