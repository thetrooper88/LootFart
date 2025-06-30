package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;

@RegisterCommand(name="Prefix", description="Let's you change your command prefix.", syntax="prefix <input>", aliases={"commandprefix", "cmdprefix", "commandp", "cmdp"})
public class CommandPrefix
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            if (args[0].length() > 2) {
                ChatUtils.sendMessage("The prefix must not be longer than 2 characters.", "Prefix");
            } else {
                Managers.COMMAND.setPrefix(args[0]);
                ChatUtils.sendMessage("Prefix set to \"" + String.valueOf((Object)ModuleCommands.getSecondColor()) + Managers.COMMAND.getPrefix() + String.valueOf((Object)ModuleCommands.getFirstColor()) + "\"!", "Prefix");
            }
        } else {
            this.sendSyntax();
        }
    }
}
