package net.tickmc.lccutils.components.bridges.main;

import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.bridges.BridgeComponent;
import net.tickmc.lccutils.components.papi.main.MythicPAPIPlaceholderComponent;
import net.tickmc.lccutils.managers.ComponentManager;

public class PlaceholderAPIBridge extends BridgeComponent {

    public PlaceholderAPIBridge() {
        super();
        addNames("PlaceholderAPI");
        setDescription("This plugin adds a 'mythic' placeholder to PlaceholderAPI.");
        addAuthors("0TickPulse");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean canEnable() {
        return LccUtils.hasPlugin("PlaceholderAPI");
    }

    @Override
    public void onLoad() {
        ComponentManager.registerComponent(new MythicPAPIPlaceholderComponent());
    }
}
