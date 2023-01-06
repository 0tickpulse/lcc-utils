package net.tickmc.lccutils.components.mythicplaceholders.mcmmo;

import net.tickmc.lccutils.components.bridges.main.McMMOBridge;
import net.tickmc.lccutils.components.conditions.mcmmo.McMMOSamePartyConditionComponent;
import net.tickmc.lccutils.components.mythicplaceholders.PlayerMythicPlaceholderComponent;
import org.bukkit.entity.Player;

public class McMMOPartyPlaceholderComponent extends PlayerMythicPlaceholderComponent {
    public McMMOPartyPlaceholderComponent() {
        super();
        addNames("mcmmo.party");
        setDescription("Returns the McMMO party of the player.");
        addAuthors("0TickPulse");
        addSeeAlso(McMMOSamePartyConditionComponent.class);
    }

    @Override
    public String get(Player player, String arg) {
        return McMMOBridge.getPartyName(player);
    }
}
