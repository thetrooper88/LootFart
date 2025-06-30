package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_124;

@RegisterCommand(name="toggle", description="Let's you toggle a module by name.", syntax="toggle <module>", aliases={"t"})
public class CommandToggle
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            boolean found = false;
            for (Module module : Managers.MODULE.getModules()) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                module.toggle(false);
                ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + String.valueOf((Object)class_124.field_1067) + module.getTag() + String.valueOf((Object)ModuleCommands.getFirstColor()) + " has been toggled " + (module.isEnabled() ? String.valueOf((Object)class_124.field_1060) + "on" : String.valueOf((Object)class_124.field_1061) + "off") + String.valueOf((Object)ModuleCommands.getFirstColor()) + "!", "Toggle");
                found = true;
                break;
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Toggle");
            }
        } else {
            this.sendSyntax();
        }
    }
}
