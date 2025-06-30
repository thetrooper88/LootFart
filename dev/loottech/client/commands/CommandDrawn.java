package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_124;

@RegisterCommand(name="drawn", description="Let's you disable or enable module drawing on the module list.", syntax="drawn <module> <value>", aliases={"shown", "show", "draw"})
public class CommandDrawn
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            boolean found = false;
            if (args[0].equalsIgnoreCase("all")) {
                for (Module m : Managers.MODULE.getModules()) {
                    m.setDrawn(Boolean.parseBoolean((String)args[1]));
                }
                ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + "All modules" + String.valueOf((Object)ModuleCommands.getFirstColor()) + " are now " + (Boolean.parseBoolean((String)args[1]) ? String.valueOf((Object)class_124.field_1060) + "shown" : String.valueOf((Object)class_124.field_1061) + "hidden") + String.valueOf((Object)ModuleCommands.getFirstColor()) + ".");
            } else {
                for (Module module : Managers.MODULE.getModules()) {
                    if (!module.getName().equalsIgnoreCase(args[0])) continue;
                    found = true;
                    module.setDrawn(Boolean.parseBoolean((String)args[1]));
                    ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + module.getName() + String.valueOf((Object)ModuleCommands.getFirstColor()) + " is now " + (module.isDrawn() ? String.valueOf((Object)class_124.field_1060) + "shown" : String.valueOf((Object)class_124.field_1061) + "hidden") + String.valueOf((Object)ModuleCommands.getFirstColor()) + ".", "Drawn");
                }
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Drawn");
            }
        } else {
            this.sendSyntax();
        }
    }
}
