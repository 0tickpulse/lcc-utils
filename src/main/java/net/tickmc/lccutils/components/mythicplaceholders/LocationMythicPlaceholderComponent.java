package net.tickmc.lccutils.components.mythicplaceholders;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import org.bukkit.Location;

import java.util.Arrays;

public abstract class LocationMythicPlaceholderComponent extends MythicPlaceholderComponent {

    private String[] originalNames = new String[0];

    @Override
    public LocationMythicPlaceholderComponent setNames(String... names) {
        super.setNames(Arrays.stream(names).map(name -> "target." + name).toArray(String[]::new));
        originalNames = names;
        return this;
    }

    @Override
    public LocationMythicPlaceholderComponent addNames(String... names) {
        super.addNames(Arrays.stream(names).map(name -> "target." + name).toArray(String[]::new));
        originalNames = GeneralUtilities.include(originalNames, names);
        return this;
    }

    @Override
    public void onEnable() {
        register(Arrays.stream(originalNames).map("target."::concat).toArray(String[]::new), Placeholder.location((location, arg) -> get(BukkitAdapter.adapt(location), arg)));
    }

    public abstract String get(Location location, String arg);
}
