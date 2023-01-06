package net.tickmc.lccutils.components.mythicplaceholders.main;

import net.tickmc.lccutils.components.mythicplaceholders.EntityMythicPlaceholderComponent;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class EntityAttributePlaceholderComponent extends EntityMythicPlaceholderComponent {

    public EntityAttributePlaceholderComponent() {
        super();
        addNames("attribute");
        setDescription("Returns the value of the specified attribute of the entity. Attributes: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html");
        addAuthors("0TickPulse");
    }

    @Override
    public String get(Entity entity, String arg) {
        try {
            Attribute attribute = Attribute.valueOf(arg.toUpperCase());
            return String.valueOf(Objects.requireNonNull(((Attributable) entity).getAttribute(attribute)).getValue());
        } catch (IllegalArgumentException | NullPointerException e) {
            return "0";
        }
    }
}
