package net.tickmc.lccutils.components.mythicplaceholders;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class PlayerMythicPlaceholderComponent extends EntityMythicPlaceholderComponent {
    @Override
    public String get(Entity entity, String arg) {
        if (!(entity instanceof Player player)) {
            return "";
        }
        return get(player, arg);
    }

    public abstract String get(Player player, String arg);

}
