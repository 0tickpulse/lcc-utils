package net.tickmc.lccutils.components.mythicplaceholders;

import io.lumine.mythic.core.skills.placeholders.Placeholder;
import net.tickmc.lccutils.utilities.GeneralUtilities;
import org.bukkit.entity.Entity;

import java.util.Arrays;

public abstract class EntityMythicPlaceholderComponent extends MythicPlaceholderComponent {

    private String[] originalNames = new String[0];

    @Override
    public EntityMythicPlaceholderComponent setNames(String... names) {
        super.setNames(Arrays.stream(names).map(name -> "[caster/target/trigger/parent]." + name).toArray(String[]::new));
        originalNames = names;
        return this;
    }

    @Override
    public EntityMythicPlaceholderComponent addNames(String... names) {
        super.addNames(Arrays.stream(names).map(name -> "[caster/target/trigger/parent]." + name).toArray(String[]::new));
        originalNames = GeneralUtilities.include(originalNames, names);
        return this;
    }

    @Override
    public void onEnable() {
        register(Arrays.stream(originalNames).map("caster."::concat).toArray(String[]::new), Placeholder.meta((meta, arg) -> get(meta.getCaster().getEntity().getBukkitEntity(), arg)));
        register(Arrays.stream(originalNames).map("target."::concat).toArray(String[]::new), Placeholder.entity((entity, arg) -> get(entity.getBukkitEntity(), arg)));
        register(Arrays.stream(originalNames).map("trigger."::concat).toArray(String[]::new), Placeholder.meta((meta, arg) -> get(meta.getTrigger().getBukkitEntity(), arg)));
        register(Arrays.stream(originalNames).map("parent."::concat).toArray(String[]::new), Placeholder.parent((entity, arg) -> get(entity.getBukkitEntity(), arg)));
    }

    public abstract String get(Entity entity, String arg);
}
