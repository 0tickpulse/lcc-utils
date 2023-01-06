package net.tickmc.lccutils.managers;

import net.tickmc.lccutils.components.bridges.main.McMMOBridge;
import net.tickmc.lccutils.components.bridges.main.PlaceholderAPIBridge;
import net.tickmc.lccutils.components.bridges.main.WorldGuardBridge;
import net.tickmc.lccutils.components.commands.main.LCCUtilsCommandComponent;
import net.tickmc.lccutils.components.commands.main.MMRunCommandComponent;
import net.tickmc.lccutils.components.conditions.main.CanAttackConditionComponent;
import net.tickmc.lccutils.components.mechanics.main.SlashMechanicComponent;
import net.tickmc.lccutils.components.mythicplaceholders.main.EntityAttributePlaceholderComponent;
import net.tickmc.lccutils.components.mythicplaceholders.main.EntityStandingOnPlaceholderComponent;

public class ComponentRegisterer {
    public static void register() {
        // mechanics
        ComponentManager.registerComponents(new SlashMechanicComponent(),
            // conditions
            new CanAttackConditionComponent(),
            // placeholders
            new EntityStandingOnPlaceholderComponent(),
            new EntityAttributePlaceholderComponent(),
            // bridges
            new McMMOBridge(),
            new WorldGuardBridge(),
            new PlaceholderAPIBridge(),
            // commands
            new MMRunCommandComponent(),
            // ! Important: This command must be registered last, as it looks for all other components.
            new LCCUtilsCommandComponent()
        );
    }
}
