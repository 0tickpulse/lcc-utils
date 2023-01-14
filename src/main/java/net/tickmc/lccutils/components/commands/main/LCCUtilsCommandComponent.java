package net.tickmc.lccutils.components.commands.main;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import net.tickmc.lccutils.components.commands.CommandComponent;
import net.tickmc.lccutils.documentation.DocumentationManager;
import net.tickmc.lccutils.managers.ComponentManager;
import net.tickmc.lccutils.managers.updates.UpdateManager;
import net.tickmc.lccutils.utilities.ComponentUtilities;
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
            .withPermission("lccutils.command.lccutils")
            .executes((sender, args) -> {
                sender.sendMessage(ComponentUtilities.joinComponentsAndCompress(
                    ComponentUtilities.hline,
                    Component.newline(),
                    ComponentUtilities.formatTitle("LccUtils v" + LccUtils.getVersion()),
                    Component.newline(),
                    ComponentUtilities.formatBody(LccUtils.getPlugin().getDescription().getDescription()),
                    Component.newline(),
                    ComponentUtilities.formatBody("/lccutils debug"),
                    ComponentUtilities.formatBody("/lccutils generateDocs"),
                    ComponentUtilities.formatBody("/lccutils component [<category>] [<component>]"),
                    ComponentUtilities.hline));
            });

        /*
         * SUBCOMMANDS
         */

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

        cmd.withSubcommand(new CommandAPICommand("update").executes((sender, args) -> {
            sender.sendMessage("Checking for updates...");
            sender.sendMessage("You are running LccUtils v" + LccUtils.getVersion());
            sender.sendMessage("The latest version is v" + UpdateManager.getLatestVersion());
            if (UpdateManager.isLatestVersion()) {
                sender.sendMessage("You are running the latest version!");
            } else {
                sender.sendMessage("You are not running the latest version!");
            }
        }));

        return cmd;
    }
}
