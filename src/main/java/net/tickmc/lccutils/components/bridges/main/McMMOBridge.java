package net.tickmc.lccutils.components.bridges.main;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.components.bridges.BridgeComponent;
import net.tickmc.lccutils.components.conditions.mcmmo.McMMOSamePartyConditionComponent;
import net.tickmc.lccutils.components.mythicplaceholders.mcmmo.McMMOPartyPlaceholderComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class McMMOBridge extends BridgeComponent {

    public McMMOBridge() {
        addNames("McMMO");
        setDescription("LCCUtils has compatibility with McMMO. This bridge enables certain things like the " + LccComponent.getMarkdownLink(new McMMOPartyPlaceholderComponent()) + ".");
        setAuthor("0TickPulse");
    }

    @Override
    public void onLoad() {
        ComponentManager.registerComponent(new McMMOSamePartyConditionComponent());
        ComponentManager.registerComponent(new McMMOPartyPlaceholderComponent());
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean canEnable() {
        return LccUtils.hasPlugin("mcMMO");
    }

    public static @Nullable Party getParty(Player player) {
        McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(player);
        if (mcMMOPlayer == null) {
            return null;
        }
        return mcMMOPlayer.getParty();
    }

    public static @NotNull String getPartyName(Player player) {
        Party party = getParty(player);
        if (party == null) {
            return "";
        }
        return party.getName();
    }

    public static boolean isInParty(Player player) {
        return getParty(player) != null;
    }

    public static boolean sameParty(Player player1, Player player2) {
        Party party1 = getParty(player1);
        Party party2 = getParty(player2);
        if (party1 == null || party2 == null) {
            return false;
        }
        return party1.equals(party2);
    }
}
