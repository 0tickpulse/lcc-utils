package net.tickmc.lccutils.components.mechanics.main;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.core.skills.SkillExecutor;
import net.tickmc.lccutils.components.mechanics.MechanicComponent;
import net.tickmc.lccutils.components.mechanics.UtilFieldsMechanic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CustomModelDataMechanicComponent extends MechanicComponent {

    public CustomModelDataMechanicComponent() {
        super();
        addNames(
            "setitemcustommodeldata", "modifyitemcustommodeldata", "changeitemcustommodeldata",
            "setitemmodel", "modifyitemmodel", "changeitemmodel"
        );
        setDescription("Sets the custom model data of the item.");
        addAuthors("0TickPulse");
        addFields(
            new ComponentField().addNames("custommodeldata", "model", "m").setDescription("The custom model data to set the item to.")
        );
    }

    public static class CustomModelDataMechanic extends UtilFieldsMechanic implements INoTargetSkill {
        private final PlaceholderInt model;
        public CustomModelDataMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
            super(manager, file, line, mlc);
            model = mlc.getPlaceholderInteger("custommodeldata", 0);
        }

        @Override
        public SkillResult cast(SkillMetadata skillMetadata) {
            if (!skillMetadata.getVariables().has("equip-item")) {
                return SkillResult.SUCCESS;
            }
            ItemStack stack = (ItemStack) skillMetadata.getVariables().get("equip-item").get();
            ItemMeta newMeta = stack.getItemMeta();
            newMeta.setCustomModelData(model.get(skillMetadata));
            stack.setItemMeta(newMeta);
            return SkillResult.SUCCESS;
        }
    }

    @NotNull
    @Override
    public MechanicClassConstructor getMechanicConstructor() {
        return CustomModelDataMechanic::new;
    }
}
