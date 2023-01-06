package net.tickmc.lccutils.events;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.core.skills.SkillExecutor;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.components.mechanics.MechanicComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class MythicMechanicsEvent implements Listener {
    @EventHandler
    public void onMechanicsReloaded(MythicMechanicLoadEvent e) {
        SkillExecutor executor = e.getContainer().getManager();
        File file = e.getContainer().getFile();
        String line = e.getConfig().getLine();
        MythicLineConfig mlc = e.getConfig();
        for (LccComponent<?> component : ComponentManager.getComponentsByCategory(ComponentCategory.MYTHIC_MECHANIC)) {
            if (GeneralUtilities.containsIgnoreCase(component.getNames(), e.getMechanicName()) && component instanceof MechanicComponent mechanic) {
                e.register(mechanic.getMechanicConstructor().construct(executor, file, line, mlc));
            }
        }
    }
}
