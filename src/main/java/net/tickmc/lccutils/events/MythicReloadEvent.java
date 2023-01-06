package net.tickmc.lccutils.events;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import net.tickmc.lccutils.components.mythicplaceholders.MythicPlaceholderComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicReloadEvent implements Listener {
    @EventHandler
    public void onMythicReloaded(MythicReloadedEvent e) {
        MythicPlaceholderComponent.registerAll();
    }
}
