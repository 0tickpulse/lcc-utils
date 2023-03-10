package net.tickmc.lccutils.components.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import net.tickmc.lccutils.components.ComponentWithFields;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class UtilFieldsMechanic extends SkillMechanic {
    public final PlaceholderDouble chance;
    public static List<ComponentWithFields.ComponentField> FIELDS = new ArrayList<>(Arrays.asList(
        new ComponentWithFields.ComponentField().addNames("chance").setDescription("The chance that this skill will run, with 1 being a guaranteed execution.").setDefaultValue("1")
    ));
    public UtilFieldsMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
        this.chance = mlc.getPlaceholderDouble("chance", 1);
    }
    protected boolean shouldRun(SkillMetadata skillMetadata) {
        double random = Math.random();
        return chance.get(skillMetadata) >= random;
    }

    @Override
    public boolean executeSkills(SkillMetadata data) {
        if (!shouldRun(data)) {
            return false;
        }
        return super.executeSkills(data);
    }
}
