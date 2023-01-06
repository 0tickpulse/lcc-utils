package net.tickmc.lccutils.components.conditions.mcmmo;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import net.tickmc.lccutils.components.bridges.main.McMMOBridge;
import net.tickmc.lccutils.components.conditions.ConditionComponent;
import net.tickmc.lccutils.components.mythicplaceholders.mcmmo.McMMOPartyPlaceholderComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class McMMOSamePartyConditionComponent extends ConditionComponent {

    public McMMOSamePartyConditionComponent() {
        super();
        addNames("sameparty", "samemcmmoparty", "partymatches", "mcmmopartymatches");
        setDescription("Checks if the player is in the same McMMO party as the target.");
        addAuthors("0TickPulse");
        addSeeAlso(McMMOPartyPlaceholderComponent.class);
    }

    public static class McMMOSamePartyCondition extends SkillCondition implements IEntityComparisonCondition {
        public McMMOSamePartyCondition(MythicLineConfig mlc) {
            super(mlc.getLine());
        }

        @Override
        public boolean check(AbstractEntity abstractEntity, AbstractEntity abstractEntity1) {
            if (!abstractEntity.isPlayer() || !abstractEntity1.isPlayer()) {
                return false;
            }
            return McMMOBridge.sameParty((Player) abstractEntity.getBukkitEntity(), (Player) abstractEntity1.getBukkitEntity());
        }
    }

    @NotNull
    @Override
    public ConditionClassConstructor getConditionConstructor() {
        return McMMOSamePartyCondition::new;
    }
}
