package net.tickmc.lccutils.components.conditions;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.core.skills.SkillCondition;
import net.tickmc.lccutils.components.ComponentWithFields;
import org.jetbrains.annotations.NotNull;

public abstract class ConditionComponent extends ComponentWithFields {

    /**
     * The constructor for the condition component.
     */
    @FunctionalInterface
    public interface ConditionClassConstructor {
        SkillCondition construct(MythicLineConfig mlc);
    }
    @Override
    public @NotNull String getCategory() {
        return "Condition";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public abstract @NotNull ConditionClassConstructor getConditionConstructor();
}
