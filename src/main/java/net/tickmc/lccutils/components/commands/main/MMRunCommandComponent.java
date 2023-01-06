package net.tickmc.lccutils.components.commands.main;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillTriggers;
import net.tickmc.lccutils.components.commands.CommandComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class MMRunCommandComponent extends CommandComponent {

    public MMRunCommandComponent() {
        super();
        addNames(
            "mmrun", "runmm",
            "mythicrun", "runmythic",
            "mythicmobsrun", "runmythicmobs",
            "mythicmobrun", "runmythicmob",
            "mmtest", "testmm",
            "mythictest", "testmythic",
            "mythicmobstest", "testmythicmobs",
            "mythicmobtest", "testmythicmob"
        );
        setDescription("Runs a MythicMobs skill line.");
        addAuthors("0TickPulse");
    }

    @NotNull
    @Override
    public CommandAPICommand getCommand() {
        return new CommandAPICommand(getName())
            .withAliases(getAliases())
            .withArguments(new GreedyStringArgument("line").includeSuggestions(ArgumentSuggestions.strings(
                MythicBukkit.inst().getSkillManager().getMechanics().keySet().stream().map(String::toLowerCase).toArray(String[]::new)
            )))
            .executes((sender, args) -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to run this command.");
                    return;
                }
                String line = (String) args[0];
                MythicBukkit.inst().getSkillManager().getSkill("[" + line + "]").ifPresent(
                    skill -> skill.execute(
                        SkillTriggers.API,
                        new GenericCaster(BukkitAdapter.adapt((Entity) player)),
                        BukkitAdapter.adapt(player),
                        BukkitAdapter.adapt(player.getLocation()),
                        new HashSet<>(List.of(new AbstractEntity[]{BukkitAdapter.adapt(player)})),
                        new HashSet<>(),
                        1
                    )
                );
            });
    }
}
