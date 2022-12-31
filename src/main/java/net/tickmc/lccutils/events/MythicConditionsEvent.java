package net.tickmc.lccutils.events;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.components.conditions.ConditionComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicConditionsEvent implements Listener {
    @EventHandler
    public void onMythicConditionsLoad(MythicConditionLoadEvent e) {
        for (LccComponent<?> component : ComponentManager.getComponentsByCategory("Condition")) {
            if (GeneralUtilities.containsIgnoreCase(component.names, e.getConditionName()) && component instanceof ConditionComponent condition) {
                e.register(condition.getConditionConstructor().construct(e.getConfig()));
            }
        }
    }
}
