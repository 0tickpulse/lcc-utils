package net.tickmc.lccutils.components.bridges;

import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.utilities.Debug;

/**
 * Represents a bridge between the plugin and another plugin.
 */
public abstract class BridgeComponent extends LccComponent<BridgeComponent> {
    public BridgeComponent() {
        super();
        setCategory(ComponentCategory.BRIDGE);
    }

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
}
