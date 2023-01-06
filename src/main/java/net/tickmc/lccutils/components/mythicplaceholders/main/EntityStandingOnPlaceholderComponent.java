package net.tickmc.lccutils.components.mythicplaceholders.main;

import net.tickmc.lccutils.components.mythicplaceholders.EntityMythicPlaceholderComponent;
import org.bukkit.entity.Entity;

public class EntityStandingOnPlaceholderComponent extends EntityMythicPlaceholderComponent {

    public EntityStandingOnPlaceholderComponent() {
        super();
        addNames("standing_on");
        setDescription("Returns the title of the block the entity is standing on.");
        addAuthors("0TickPulse");
    }

    @Override
    public String get(Entity entity, String arg) {
        return entity.getLocation().add(0, -1, 0).getBlock().getType().name();
    }
}
