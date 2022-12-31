package net.tickmc.lccutils.components.commands.main;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import net.tickmc.lccutils.components.commands.CommandComponent;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.documentation.DocumentationManager;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.utilities.Debug;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class LCCUtilsCommandComponent extends CommandComponent {

    public LCCUtilsCommandComponent() {
        addNames("lccutils", "lccu", "lccutilities", "lccutil", "lccmm", "lccmythicmobs");
        setDescription("The main command for LccUtils.");
        setAuthor("0TickPulse");
        addExamples("lccutils command slash");
    }

    @Override
    public @NotNull CommandAPICommand getCommand() {
        CommandAPICommand cmd = new CommandAPICommand(getName())
                .withAliases(getAliases())
                .withHelp(description, description)
                .withPermission("lccutils.command.lccutils");
        for (Map.Entry<String, List<LccComponent<?>>> entry : ComponentManager.getComponents().entrySet()) {
            cmd.withSubcommand(new CommandAPICommand(entry.getKey().toLowerCase())
                    .withPermission("lccutils.command.lccutils." + entry.getKey().toLowerCase())
                    .withArguments(new MultiLiteralArgument(entry.getValue().stream().map(LccComponent::getName).toArray(String[]::new)))
                    .executes((sender, args) -> {
                        LccComponent<?> component = ComponentManager.getComponentsByName(entry.getKey(), (String) args[0]).get(0);
                        if (component == null) {
                            sender.sendMessage("§cComponent not found.");
                            return;
                        }
                        sender.sendMessage(component.generateMinecraftEntry());
                    }));
        }
        cmd.withSubcommand(new CommandAPICommand("generateDocs").executes((sender, args) -> {
            sender.sendMessage("Generating docs...");
            try {
                DocumentationManager.writeDocumentation();
            } catch (Exception e) {
                sender.sendMessage(Debug.formatException(e));
            }
        }));
        return cmd;
    }
}
