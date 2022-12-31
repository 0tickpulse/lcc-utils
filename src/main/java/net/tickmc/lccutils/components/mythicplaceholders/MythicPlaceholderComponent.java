package net.tickmc.lccutils.components.mythicplaceholders;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import net.tickmc.lccutils.components.LccComponent;
import org.jetbrains.annotations.NotNull;

public abstract class MythicPlaceholderComponent extends LccComponent<MythicPlaceholderComponent> {
    @Override
    public @NotNull String getCategory() {
        return "MythicPlaceholder";
    }
    @Override
    public void onDisable() {
    }

    public void register(@NotNull String[] names, Placeholder transformer) {
        MythicBukkit.inst().getPlaceholderManager().register(names, transformer);
    }
}
