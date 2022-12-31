package net.tickmc.lccutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import net.tickmc.lccutils.events.MythicConditionsEvent;
import net.tickmc.lccutils.events.MythicMechanicsEvent;
import net.tickmc.lccutils.managers.ComponentRegisterer;
import net.tickmc.lccutils.utilities.Debug;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LccUtils extends JavaPlugin {

    public boolean debug = true;

    public static LccUtils getPlugin() {
        return getPlugin(LccUtils.class);
    }
    public static boolean hasPlugin(String name) {
        return getPlugin().getServer().getPluginManager().getPlugin(name) != null;
    }
    public static PluginManager getPluginManager() {
        return getPlugin().getServer().getPluginManager();
    }

    public static String getPluginName() {
        return getPlugin().getName();
    }

    public static String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    public void registerEvents() {
        Debug.devLog("Registering events...");
        registerEvents(new MythicMechanicsEvent(), new MythicConditionsEvent());
    }

    public void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onEnable() {
        CommandAPI.onLoad(new CommandAPIConfig());
        CommandAPI.onEnable(this);
        ComponentRegisterer.register();
        registerEvents();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
