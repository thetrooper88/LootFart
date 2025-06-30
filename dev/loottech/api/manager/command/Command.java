package dev.loottech.api.manager.command;

import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.IMinecraft;
import java.util.Arrays;
import java.util.List;

public abstract class Command
implements IMinecraft {
    private final String name;
    private final String description;
    private final String syntax;
    private final List<String> aliases;

    public Command() {
        RegisterCommand annotation = (RegisterCommand)this.getClass().getAnnotation(RegisterCommand.class);
        this.name = annotation.name();
        this.description = annotation.description();
        this.syntax = annotation.syntax();
        this.aliases = Arrays.asList((Object[])annotation.aliases());
    }

    public abstract void onCommand(String[] var1);

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public void sendSyntax() {
        ChatUtils.sendMessage(this.getSyntax());
    }
}
