package net.tickmc.lccutils.components.bridges.main;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.bridges.BridgeComponent;
import net.tickmc.lccutils.components.miscellaneous.worldguard.WorldGuardCustomFlagsComponent;
import net.tickmc.lccutils.components.mythicplaceholders.worldguard.EntityWorldGuardFlagPlaceholderComponent;
import net.tickmc.lccutils.components.mythicplaceholders.worldguard.LocationWorldGuardFlagPlaceholderComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WorldGuardBridge extends BridgeComponent {

    public WorldGuardBridge() {
        super();
        addNames("WorldGuard");
        setDescription("Adds several things that hooks into WorldGuard.");
        addAuthors("0TickPulse");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean canEnable() {
        return LccUtils.hasPlugin("WorldGuard");
    }

    @Override
    public void onLoad() {
        ComponentManager.registerComponents(
            new LocationWorldGuardFlagPlaceholderComponent(),
            new EntityWorldGuardFlagPlaceholderComponent(),
            new WorldGuardCustomFlagsComponent()
        );
    }

    public static Set<ProtectedRegion> getRegions(Location location) {
        ApplicableRegionSet regions = getApplicableRegions(location);
        return regions != null ? regions.getRegions() : null;
    }

    public static ApplicableRegionSet getApplicableRegions(Location location) {
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (manager != null) {
            return manager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        }
        return null;
    }

    public static @Nullable Flag<?> getFlagFromString(String flag) {
        return WorldGuard.getInstance().getFlagRegistry().get(flag);
    }

    public static Object getFlagValue(Location location, @Nullable Flag<?> flag) {
        ApplicableRegionSet regions = getApplicableRegions(location);
        if (regions == null || regions.size() == 0 || flag == null) {
            return null;
        }
        for (ProtectedRegion region : regions) {
            Object value = region.getFlag(flag);
            if (value != null) {
                return value.toString();
            }
        }
        return flag.getDefault();
    }

    /**
     * @implNote Will return a "null" string instead of null.
     */
    public static String getFlagValueString(Location location, Flag<?> flag) {
        Object value = getFlagValue(location, flag);
        return value != null ? value.toString() : "null";
    }
}
