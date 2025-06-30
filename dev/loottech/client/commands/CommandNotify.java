package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_124;

@RegisterCommand(name="notify", description="Let's you disable or enable module toggle messages.", syntax="notify <module> <value>", aliases={"chatnotify", "togglemsg", "togglemsgs", "togglemessages"})
public class CommandNotify
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            boolean found = false;
            for (Module module : Managers.MODULE.getModules()) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                found = true;
                module.setChatNotify(Boolean.parseBoolean((String)args[1]));
                ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + module.getName() + String.valueOf((Object)ModuleCommands.getFirstColor()) + " now has Toggle Messages " + (module.isChatNotify() ? String.valueOf((Object)class_124.field_1060) + "enabled" : String.valueOf((Object)class_124.field_1061) + "disabled") + String.valueOf((Object)ModuleCommands.getFirstColor()) + ".", "Notify");
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Notify");
            }
        } else {
            this.sendSyntax();
        }
    }
}
