package net.tickmc.lccutils.components.commands.main;

import dev.jorel.commandapi.CommandAPICommand;
import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.components.commands.CommandComponent;
import net.tickmc.lccutils.documentation.DocumentationManager;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class LCCUtilsCommandComponent extends CommandComponent {

    public LCCUtilsCommandComponent() {
        super();
        addNames("lccutils", "lccu", "lccutilities", "lccutil", "lccmm", "lccmythicmobs", "lccmythic", "lcc");
        setDescription("The main command for LccUtils.");
        addAuthors("0TickPulse");
        addExamples("/lccutils component command slash");
    }

    @Override
    public @NotNull CommandAPICommand getCommand() {
        CommandAPICommand cmd = new CommandAPICommand(getName())
            .withAliases(getAliases())
            .withHelp(getDescription(), getDescription())
            .withPermission("lccutils.command.lccutils");
        CommandAPICommand componentsCommand = new CommandAPICommand("component");
        for (Map.Entry<ComponentCategory, List<LccComponent<?>>> entry : ComponentManager.getComponents().entrySet()) {
            CommandAPICommand subcommand = new CommandAPICommand(entry.getKey().getSimpleReadableName())
                .withPermission("lccutils.command.lccutils." + entry.getKey().getSimpleReadableName());
            for (LccComponent<?> component : entry.getValue()) {
                subcommand.withSubcommand(new CommandAPICommand(component.getName().replace(" ", "_"))
                    .withPermission("lccutils.command.lccutils." + entry.getKey().getSimpleReadableName() + "." + component.getName())
                    .executes((sender, args) -> {
                        sender.sendMessage(component.generateMinecraftEntry());
                    })
                );
            }
            componentsCommand.withSubcommand(subcommand);
        }
        cmd.withSubcommand(componentsCommand);

        /*
         * SUBCOMMANDS
         */

        // generate docs
        cmd.withSubcommand(new CommandAPICommand("generateDocs").executes((sender, args) -> {
            sender.sendMessage("Generating docs...");
            try {
                DocumentationManager.writeDocumentation();
            } catch (Exception e) {
                sender.sendMessage(Debug.formatException(e));
            }
        }));

        cmd.withSubcommand(new CommandAPICommand("debug").executes((sender, args) -> {
            LccUtils.debug = !LccUtils.debug;
            sender.sendMessage("Debug mode is now " + LccUtils.debug + "!");
        }));

        return cmd;
    }
}
