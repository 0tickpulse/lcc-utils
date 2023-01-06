package net.tickmc.lccutils.components.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import org.jetbrains.annotations.NotNull;

public abstract class CommandComponent extends LccComponent<CommandComponent> {
    public CommandComponent() {
        super();
        setCategory(ComponentCategory.MINECRAFT_COMMAND);
    }

    public abstract @NotNull CommandAPICommand getCommand();

    @Override
    public void onEnable() {
        getCommand().register();
    }

    @Override
    public void onDisable() {
    }
}
