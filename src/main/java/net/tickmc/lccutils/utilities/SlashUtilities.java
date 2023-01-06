package net.tickmc.lccutils.utilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

import static java.lang.Math.*;

/**
 * A class that provides methods for slashes - arcs of a circle.
 *
 * @author 0TickPulse
 */
public class SlashUtilities {
    public static Set<Entity> getEntitiesInPoints(Location casterLocation, Set<Location> locations, double radius, double verticalRadius, double distance) {
        Set<Entity> entities = new HashSet<>();
        for (Location location : locations) {
            Set<Location> pointsBetween = distance != 0 ? LocationUtilities.getPointsBetween(location, casterLocation, distance) : new HashSet<>(Collections.singletonList(location));
            for (Location point : pointsBetween) {
                entities.addAll(point.getWorld().getNearbyEntities(point, radius, verticalRadius, radius));
            }
        }
        return entities;
    }

    public static List<Location> getSlashLocations(Location origin, double radius, double rotation, int points, double arc) throws IllegalArgumentException {

        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (arc < 0) {
            throw new IllegalArgumentException("Arc cannot be negative");
        }
        if (arc > 360) {
            throw new IllegalArgumentException("Arc cannot be greater than 360");
        }
        if (rotation < 0) {
            rotation += 180;
        }

        List<Location> locations = new ArrayList<>();

        double i = 90 + (arc / -2);

        while (i <= 90 + (arc / 2)) {
            double relativeHorizontalOffset = radius * sin(toRadians((arc / 2) - i));
            double horizontalOffset = relativeHorizontalOffset * cos(toRadians(rotation));
            double forwardOffset = radius * cos(toRadians(((arc / 2) - i)));
            double verticalOffset = sin(toRadians(rotation)) * relativeHorizontalOffset;
            locations.add(LocationUtilities.relativeOffset(origin, forwardOffset, horizontalOffset, verticalOffset));
            i += arc / points;
        }

        return locations;
    }
}
