package net.tickmc.lccutils.components.mythicplaceholders.worldguard;

import net.tickmc.lccutils.components.bridges.main.WorldGuardBridge;
import net.tickmc.lccutils.components.mythicplaceholders.LocationMythicPlaceholderComponent;
import org.bukkit.Location;

public class LocationWorldGuardFlagPlaceholderComponent extends LocationMythicPlaceholderComponent {

    public LocationWorldGuardFlagPlaceholderComponent() {
        super();
        addNames("worldguard.flag.<flag>");
        setDescription("Returns the value of the specified WorldGuard flag at the target location.");
        addAuthors("0TickPulse");
    }

    @Override
    public String get(Location location, String arg) {
        return WorldGuardBridge.getFlagValueString(location, WorldGuardBridge.getFlagFromString(arg));
    }
}
