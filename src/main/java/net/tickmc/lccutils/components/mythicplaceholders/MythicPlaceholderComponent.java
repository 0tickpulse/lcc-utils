package net.tickmc.lccutils.components.mythicplaceholders;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.Debug;
import org.jetbrains.annotations.NotNull;

public abstract class MythicPlaceholderComponent extends LccComponent<MythicPlaceholderComponent> {
    public MythicPlaceholderComponent() {
        super();
        setCategory(ComponentCategory.MYTHIC_PLACEHOLDER);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public abstract void load();

    public void register(@NotNull String[] names, Placeholder transformer) {
        Debug.devLog("Registering MythicPlaceholderComponent " + getClass().getSimpleName() + " with names " + String.join(", ", names));
        MythicBukkit.inst().getPlaceholderManager().register(names, transformer);
    }

    public static void registerAll() {
        for (LccComponent<?> component : ComponentManager.getComponentsByCategory(ComponentCategory.MYTHIC_PLACEHOLDER)) {
            if (!(component instanceof MythicPlaceholderComponent mythicPlaceholderComponent)) {
                continue;
            }
            mythicPlaceholderComponent.load();
        }
    }
}
