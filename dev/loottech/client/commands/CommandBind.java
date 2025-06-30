package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_3675;
import org.lwjgl.glfw.GLFW;

@RegisterCommand(name="bind", description="Let's you bind a module with commands.", syntax="bind <name> <key> | clear", aliases={"key", "keybind", "b"})
public class CommandBind
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            String name = args[0];
            String key = args[1];
            boolean found = false;
            for (Module module : Managers.MODULE.getModules()) {
                if (!module.getName().equalsIgnoreCase(name)) continue;
                int keyCode = class_3675.method_15981((String)("key.keyboard." + key.toLowerCase())).method_1444();
                module.setBind(keyCode);
                ChatUtils.sendMessage("Bound " + String.valueOf((Object)ModuleCommands.getSecondColor()) + module.getTag() + String.valueOf((Object)ModuleCommands.getFirstColor()) + " to " + String.valueOf((Object)ModuleCommands.getSecondColor()) + GLFW.glfwGetKeyName((int)module.getBind(), (int)1).toUpperCase() + String.valueOf((Object)ModuleCommands.getFirstColor()) + ".", "Bind");
                found = true;
                break;
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Bind");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (Module module : Managers.MODULE.getModules()) {
                    module.setBind(0);
                }
                ChatUtils.sendMessage("Successfully cleared all binds.", "Bind");
            } else {
                this.sendSyntax();
            }
        } else {
            this.sendSyntax();
        }
    }
}
