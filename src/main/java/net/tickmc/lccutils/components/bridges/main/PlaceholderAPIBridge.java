package net.tickmc.lccutils.components.bridges.main;

import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.bridges.BridgeComponent;
import net.tickmc.lccutils.components.papi.main.MythicPAPIPlaceholderComponent;
import net.tickmc.lccutils.managers.ComponentManager;

public class PlaceholderAPIBridge extends BridgeComponent {
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
