package net.tickmc.lccutils.components.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.core.skills.SkillExecutor;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.ComponentWithFields;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @see ComponentCategory#MYTHIC_MECHANIC
 */
public abstract class MechanicComponent extends ComponentWithFields {

    public MechanicComponent() {
        super();
        setCategory(ComponentCategory.MYTHIC_MECHANIC);
    }

    /**
     * The constructor for the mechanic component.
     */
    @FunctionalInterface
    public interface MechanicClassConstructor {
        ISkillMechanic construct(SkillExecutor manager, File file, String line, MythicLineConfig mlc);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public abstract @NotNull MechanicClassConstructor getMechanicConstructor();
}
