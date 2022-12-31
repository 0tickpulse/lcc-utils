package net.tickmc.lccutils.components.bridges;

import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.utilities.Debug;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a bridge between the plugin and another plugin.
 */
public abstract class BridgeComponent extends LccComponent<BridgeComponent> {
    /**
     * Should return whether this bridge can be enabled. Typically, this should check if the target plugin is enabled.
     */
    public abstract boolean canEnable();

    /**
     * Called when the bridge is enabled.
     */
    public abstract void onLoad();

    @Override
    public void onEnable() {
        if (canEnable()) {
            Debug.log("Enabling bridge " + getName() + "...");
            onLoad();
        }
    }

    @Override
    public @NotNull String getCategory() {
        return "Bridge";
    }
}
