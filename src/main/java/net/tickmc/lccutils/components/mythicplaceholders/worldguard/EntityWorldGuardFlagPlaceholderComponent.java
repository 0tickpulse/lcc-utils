package net.tickmc.lccutils.components.mythicplaceholders.worldguard;

import net.tickmc.lccutils.components.bridges.main.WorldGuardBridge;
import net.tickmc.lccutils.components.mythicplaceholders.EntityMythicPlaceholderComponent;
import org.bukkit.entity.Entity;

public class EntityWorldGuardFlagPlaceholderComponent extends EntityMythicPlaceholderComponent {

    public EntityWorldGuardFlagPlaceholderComponent() {
        super();
        addNames("worldguard.flag");
        setDescription("Returns the value of the specified WorldGuard flag at the target entity's location.");
        addAuthors("0TickPulse");
    }

    @Override
    public String get(Entity entity, String arg) {
        return WorldGuardBridge.getFlagValueString(entity.getLocation(), WorldGuardBridge.getFlagFromString(arg));
    }
}
