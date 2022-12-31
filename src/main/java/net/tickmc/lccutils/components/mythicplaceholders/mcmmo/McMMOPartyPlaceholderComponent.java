package net.tickmc.lccutils.components.mythicplaceholders.mcmmo;

import net.tickmc.lccutils.components.bridges.main.McMMOBridge;
import net.tickmc.lccutils.components.mythicplaceholders.PlayerMythicPlaceholderComponent;
import org.bukkit.entity.Player;

public class McMMOPartyPlaceholderComponent extends PlayerMythicPlaceholderComponent {
    public McMMOPartyPlaceholderComponent() {
        addNames("mcmmo.party");
        setDescription("Returns the McMMO party of the player.");
        setAuthor("0TickPulse");
    }
    @Override
    public String get(Player player, String arg) {
        return McMMOBridge.getPartyName(player);
    }
}
