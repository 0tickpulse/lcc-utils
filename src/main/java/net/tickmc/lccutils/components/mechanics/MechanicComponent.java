package net.tickmc.lccutils.components.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.core.skills.SkillExecutor;
import net.tickmc.lccutils.components.ComponentWithFields;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @see <a href="https://git.lumine.io/mythiccraft/MythicMobs/-/wikis/Skills/Mechanics">Mythic wiki - Mechanics</a>
 */
public abstract class MechanicComponent extends ComponentWithFields {

    /**
     * The constructor for the mechanic component.
     */
    @FunctionalInterface
    public interface MechanicClassConstructor {
        ISkillMechanic construct(SkillExecutor manager, File file, String line, MythicLineConfig mlc);
    }

    @Override
    public @NotNull String getCategory() {
        return "Mechanic";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public abstract @NotNull MechanicClassConstructor getMechanicConstructor();
}
