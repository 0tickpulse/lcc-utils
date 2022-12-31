package net.tickmc.lccutils.utilities;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Set;

/**
 * A set of utilities for working with locations.
 * @author 0TickPulse
 */
public class LocationUtilities {
    public record RelativeOffsets(double forward, double right, double up) {
    }

    public static Set<Location> getPointsBetween(Location location1, Location location2, double distance) {
        Set<Location> locations = new java.util.HashSet<>();
        Vector vector = location2.toVector().subtract(location1.toVector());
        vector.normalize();
        vector.multiply(distance);
        Location current = location1.clone();
        while (current.toVector().distance(location2.toVector()) > distance) {
            locations.add(current.clone());
            current.add(vector);
        }
        return locations;
    }

    public static Location forward(Location location, double distance) {
        Vector vec = location.getDirection().normalize().multiply(distance);
        return location.clone().add(vec);
    }

    public static Location right(Location location, double distance) {
        Location newLocation = location.clone();
        newLocation.setPitch(0);
        Vector vec = newLocation.getDirection().rotateAroundY(Math.PI * 0.5).normalize().multiply(distance * -1);
        return location.clone().add(vec);
    }

    public static Location up(Location location, double distance) {
        Location newLocation = location.clone();
        newLocation.setPitch(location.getPitch() - 90);
        Vector vec = newLocation.getDirection().normalize().multiply(distance);
        return location.clone().add(vec);
    }

    public static Location relativeOffset(Location location, double forward, double right, double up) {
        return up(forward(right(location, right), forward), up);
    }

    public static Location relativeOffset(Location location, RelativeOffsets offsets) {
        return relativeOffset(location, offsets.forward, offsets.right, offsets.up);
    }
}