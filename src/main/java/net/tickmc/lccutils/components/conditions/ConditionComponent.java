package net.tickmc.lccutils.components.conditions;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.core.skills.SkillCondition;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.ComponentWithFields;
import org.jetbrains.annotations.NotNull;

public abstract class ConditionComponent extends ComponentWithFields {

    public ConditionComponent() {
        super();
        setCategory(ComponentCategory.MYTHIC_CONDITION);
    }

    /**
     * The constructor for the condition component.
     */
    @FunctionalInterface
    public interface ConditionClassConstructor {
        SkillCondition construct(MythicLineConfig mlc);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public abstract @NotNull ConditionClassConstructor getConditionConstructor();
}
