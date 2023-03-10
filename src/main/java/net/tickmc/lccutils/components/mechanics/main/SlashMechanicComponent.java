package net.tickmc.lccutils.components.mechanics.main;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.*;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.skills.SkillExecutor;
import net.tickmc.lccutils.components.mechanics.MechanicComponent;
import net.tickmc.lccutils.components.mechanics.TransformableLocationsMechanic;
import net.tickmc.lccutils.utilities.SlashUtilities;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SlashMechanicComponent extends MechanicComponent {
    public SlashMechanicComponent() {
        super();
        addNames("slash", "runslash");
        setDescription("""
            Performs a slash. This slash is executed by calculating points along a circle.
            This circle is calculated by fields like `points` and `radius`.
            When used effectively, this mechanic can simulate a weapon's slash.
            This mechanic also provides entity targeting and allows you to specify actions performed on targeted entities through the `onhitskill` field.
            However, if you want more lenient targeting, you can use the `@EntitiesInCone` targeter provided by Mythic.""");
        addAuthors("0TickPulse");
        for (ComponentField field : TransformableLocationsMechanic.FIELDS) {
            addFields(field);
        }
        addFields(new ComponentField().addNames("onpointskill", "onpoint", "op").setDescription("The skill to perform for every point in the slash."),
            new ComponentField().addNames("onhitskill", "onhit", "oh").setDescription("The skill to perform for every entity hit by the slash."),
            new ComponentField().addNames("radius", "r").setDescription("The radius of the slash.").setDefaultValue("2"),
            new ComponentField().addNames("points", "p").setDescription("The number of points in the slash.").setDefaultValue("5"),
            new ComponentField().addNames("arc", "a").setDescription("The arc of the slash.").setDefaultValue("180"),
            new ComponentField().addNames("interval", "i").setDescription("The interval between each iteration in the slash.").setDefaultValue("0"),
            new ComponentField().addNames("iterationCount", "count", "ic", "c").setDescription("The number of points each iteration will have.").setDefaultValue("1"),
            new ComponentField().addNames("lineDistance", "ld").setDescription("When slashing, sometimes the target entities are in between the caster and the points of the slash, causing the entity not to be hit. In order to circumvent this, the slash mechanic also takes points in between each slash point and checks for entities there. This is the distance between each line point. Set to 0 to disable.").setDefaultValue("0"),
            new ComponentField().addNames("hitRadius", "hr").setDescription("Each point in the slash checks for entities within a certain radius to determine if the entity was hit. This is the horizontal radius of each point.").setDefaultValue("0.2"),
            new ComponentField().addNames("verticalHitRadius", "vhr", "vr").setDescription("Each point in the slash checks for entities within a certain radius to determine if the entity was hit. This is the vertical radius of each point.").setDefaultValue("(The hitRadius field)"));
        addExamples("""
            SlashTest:
              Skills:
              - slash{onpointskill=SlashTestTick;points=80;r=5;rot=<random.1to180>} @forward{f=0;uel=true}
                                    
            SlashTestTick:
              Skills:
              - e:p{p=flame} @Origin""");
    }

    public static class SlashMechanic extends TransformableLocationsMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {

        private final PlaceholderString onPointSkillName;
        private final PlaceholderString onHitSkillName;
        private final PlaceholderDouble radius;
        private final PlaceholderInt points;
        private final PlaceholderDouble arc;
        private final PlaceholderInt iterationInterval;
        private final PlaceholderInt iterationCount;
        private final PlaceholderDouble lineDistance;
        private final PlaceholderDouble hitRadius;
        private final PlaceholderDouble verticalHitRadius;
        private Optional<Skill> onPointSkill;
        private Optional<Skill> onHitSkill;

        public SlashMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
            super(manager, file, line, mlc);

            onPointSkillName = mlc.getPlaceholderString(new String[]{"onpointskill", "onpoint", "op"}, "");
            onHitSkillName = mlc.getPlaceholderString(new String[]{"onhitskill", "onhit", "oh"}, "");

            radius = mlc.getPlaceholderDouble(new String[]{"radius", "r"}, "2");
            points = mlc.getPlaceholderInteger(new String[]{"points", "p"}, "5");

            arc = mlc.getPlaceholderDouble(new String[]{"arc", "a"}, "180");

            iterationInterval = mlc.getPlaceholderInteger(new String[]{"interval", "i"}, "1");
            iterationCount = mlc.getPlaceholderInteger(new String[]{"iterationCount", "count", "ic", "c"}, "1");

            lineDistance = mlc.getPlaceholderDouble(new String[]{"lineDistance", "ld"}, "0.2");
            hitRadius = mlc.getPlaceholderDouble(new String[]{"hitRadius", "hr"}, "0.2");
            verticalHitRadius = mlc.getPlaceholderDouble(new String[]{"verticalHitRadius", "vhr", "vr"}, hitRadius);

            getManager().queueSecondPass(() -> {
                onPointSkill = getManager().getSkill(onPointSkillName.get());
                if (onPointSkill.isEmpty() && !onPointSkillName.get().equals("")) {
                    MythicLogger.error("Could not find onPointSkill " + onPointSkillName.get());
                }
                onHitSkill = getManager().getSkill(onHitSkillName.get());
                if (onHitSkill.isEmpty() && !onHitSkillName.get().equals("")) {
                    MythicLogger.error("Could not find onHitSkill " + onHitSkillName.get());
                }
            });
        }

        @Override
        public List<Location> getPoints(SkillMetadata skillMetadata, Location target) throws IllegalArgumentException {
            return SlashUtilities.getSlashLocations(
                target,
                radius.get(skillMetadata),
                rotation.get(skillMetadata),
                points.get(skillMetadata),
                arc.get(skillMetadata)
            );
        }

        @Override
        public SkillResult cast(SkillMetadata skillMetadata, List<Location> locations) {
            int i = 0;
            for (Location point : locations) {
                int interval = iterationInterval.get(skillMetadata);
                int count = iterationCount.get(skillMetadata);
                // get delay based on iteration count and interval
                int currentIteration = i / count + 1;
                int delay = interval * currentIteration;
                SkillMetadata pointData = skillMetadata.deepClone();
                pointData.setOrigin(BukkitAdapter.adapt(point));
                onPointSkill.ifPresent(skill -> Schedulers.sync().runLater(() -> skill.execute(pointData), delay));
                i++;
            }
            Set<Entity> entities = SlashUtilities.getEntitiesInPoints(BukkitAdapter.adapt(skillMetadata.getCaster().getLocation()), new HashSet<>(locations), hitRadius.get(skillMetadata), verticalHitRadius.get(skillMetadata), lineDistance.get(skillMetadata));
            for (Entity entity : entities) {
                if (entity.equals(skillMetadata.getCaster().getEntity().getBukkitEntity())) {
                    continue;
                }
                SkillMetadata hitData = skillMetadata.deepClone();
                hitData.setOrigin(BukkitAdapter.adapt(entity.getLocation()));
                hitData.setEntityTarget(BukkitAdapter.adapt(entity));
                onHitSkill.ifPresent(skill -> skill.execute(hitData));
            }
            return SkillResult.SUCCESS;
        }
    }

    @Override
    public @NotNull MechanicClassConstructor getMechanicConstructor() {
        return SlashMechanic::new;
    }
}
