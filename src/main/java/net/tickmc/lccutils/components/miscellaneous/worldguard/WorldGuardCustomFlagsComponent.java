package net.tickmc.lccutils.components.miscellaneous.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.tickmc.lccutils.components.miscellaneous.MiscellaneousComponent;
import net.tickmc.lccutils.managers.configuration.ConfigurationManager;
import net.tickmc.lccutils.utilities.Debug;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class WorldGuardCustomFlagsComponent extends MiscellaneousComponent {

    public WorldGuardCustomFlagsComponent() {
        super();
        addNames("Worldguard custom flags");
        setDescription("Allows the registration of custom user-defined WorldGuard flags. These are dummy flags that do not actually do anything, but can be used in WorldGuard flag checks.");
        addAuthors("0TickPulse");
    }

    @Override
    public void onEnable() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        FileConfiguration config = ConfigurationManager.getConfiguration("worldguard_custom_flags.yml", """
            flags:
              # <flag_name>: <default_value>
              myflag: true
            """);
        if (config == null) {
            return;
        }
        try {
            Objects.requireNonNull(config.getConfigurationSection("flags")).getKeys(false).forEach(flagName -> {
                if (registry.get(flagName) == null) {
                    registry.register(new StateFlag(flagName, config.getBoolean("flags." + flagName)));
                }
            });
        } catch (NullPointerException e) {
            Debug.error("Missing flags section in worldguard_custom_flags.yml!");
        }
    }
}
