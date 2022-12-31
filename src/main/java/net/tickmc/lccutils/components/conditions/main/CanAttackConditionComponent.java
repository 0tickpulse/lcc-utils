package net.tickmc.lccutils.components.conditions.main;

import com.google.common.base.Function;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import net.tickmc.lccutils.components.conditions.ConditionComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CanAttackConditionComponent extends ConditionComponent {

    public CanAttackConditionComponent() {
        addNames("canattack", "canattackentity", "candamage", "candamageentity");
        setDescription("Checks if an entity can attack another entity by simulating a damage event.");
        setAuthor("0TickPulse");
        addFields(
                new ComponentField().addNames("cause", "damagecause", "c").setDescription("The cause of the damage event.").setDefaultValue("CUSTOM")
        );
    }

    public static class CanAttackCondition extends SkillCondition implements IEntityComparisonCondition {

        public final EntityDamageEvent.DamageCause cause;

        public CanAttackCondition(MythicLineConfig mlc) {
            super(mlc.getLine());
            String causeString = mlc.getString(new String[]{"cause", "damagecause", "c"}, "CUSTOM");
            EntityDamageEvent.DamageCause damageCause;
            try {
                damageCause = EntityDamageEvent.DamageCause.valueOf(causeString);
            } catch (IllegalArgumentException e) {
                damageCause = EntityDamageEvent.DamageCause.CUSTOM;
            }
            this.cause = damageCause;
        }

        public static boolean isRunning;

        @Override
        public boolean check(AbstractEntity caster, AbstractEntity target) {
            if (isRunning) {
                return false;
            }
            Entity attacker = caster.getBukkitEntity();
            Entity targetEntity = target.getBukkitEntity();
            isRunning = true;
            Map<EntityDamageEvent.DamageModifier, Double> damageMap = new HashMap<>();
            damageMap.put(EntityDamageEvent.DamageModifier.BASE, 0.0);
            Map<EntityDamageEvent.DamageModifier, Function<Double, Double>> modifierFunctions = new HashMap<>();
            modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, input -> input);
            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
                    attacker,
                    targetEntity,
                    cause,
                    damageMap,
                    modifierFunctions,
                    false);
            try {
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } catch (IllegalStateException e) {
                return false;
            } finally {
                isRunning = false;
            }
        }
    }

    @Override
    public @NotNull ConditionClassConstructor getConditionConstructor() {
        return CanAttackCondition::new;
    }
}
