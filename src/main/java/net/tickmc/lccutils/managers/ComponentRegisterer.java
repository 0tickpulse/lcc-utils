package net.tickmc.lccutils.managers;

import net.tickmc.lccutils.components.bridges.main.McMMOBridge;
import net.tickmc.lccutils.components.commands.main.LCCUtilsCommandComponent;
import net.tickmc.lccutils.components.conditions.main.CanAttackConditionComponent;
import net.tickmc.lccutils.components.mechanics.main.SlashMechanicComponent;
import net.tickmc.lccutils.components.mythicplaceholders.main.StandingOnPlaceholderComponent;

public class ComponentRegisterer {
    public static void register() {
        // mechanics
        ComponentManager.registerComponent(new SlashMechanicComponent());
        // conditions
        ComponentManager.registerComponent(new CanAttackConditionComponent());
        // bridges
        ComponentManager.registerComponent(new McMMOBridge());
        // placeholders
        ComponentManager.registerComponent(new StandingOnPlaceholderComponent());
        // commands
        // ! Important: This command must be registered last, as it looks for all other components.
        ComponentManager.registerComponent(new LCCUtilsCommandComponent());
    }
}
