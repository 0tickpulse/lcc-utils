package net.tickmc.lccutils.components.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.tickmc.lccutils.components.LccComponent;
import org.jetbrains.annotations.NotNull;

public abstract class CommandComponent extends LccComponent<CommandComponent> {
    @Override
    public @NotNull String getCategory() {
        return "Command";
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
