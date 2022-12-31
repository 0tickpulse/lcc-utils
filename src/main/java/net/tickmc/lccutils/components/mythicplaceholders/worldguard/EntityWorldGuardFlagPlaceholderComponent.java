package net.tickmc.lccutils.components.mythicplaceholders.worldguard;

import net.tickmc.lccutils.components.bridges.main.WorldGuardBridge;
import net.tickmc.lccutils.components.mythicplaceholders.EntityMythicPlaceholderComponent;
import org.bukkit.entity.Entity;

public class EntityWorldGuardFlagPlaceholderComponent extends EntityMythicPlaceholderComponent {

    public EntityWorldGuardFlagPlaceholderComponent() {
        addNames("worldguard.flag.<flag>");
        setDescription("Returns the value of the specified WorldGuard flag at the target entity's location.");
        setAuthor("0TickPulse");
    }

    @Override
    public String get(Entity entity, String arg) {
        return WorldGuardBridge.getFlagValueString(entity.getLocation(), WorldGuardBridge.getFlag(arg));
    }
}
