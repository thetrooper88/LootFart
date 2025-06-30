package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;

@RegisterCommand(name="tag", description="Let's you customize any module's tag.", syntax="tag <module> <value>", aliases={"customname", "modtag", "moduletag", "mark"})
public class CommandTag
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            boolean found = false;
            for (Module module : Managers.MODULE.getModules()) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                found = true;
                module.setTag(args[1].replace((CharSequence)"_", (CharSequence)" "));
                ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + module.getName() + String.valueOf((Object)ModuleCommands.getFirstColor()) + " is now marked as " + String.valueOf((Object)ModuleCommands.getSecondColor()) + module.getTag() + String.valueOf((Object)ModuleCommands.getFirstColor()) + ".", "Tag");
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Tag");
            }
        } else {
            this.sendSyntax();
        }
    }
}
