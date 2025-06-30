package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;

@RegisterCommand(name="config", description="Let's you save or load your configuration without restarting the game.", syntax="config <save|load> <name>", aliases={"configuration", "cfg"})
public class CommandConfig
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            String cmd = args[0].toLowerCase();
            String name = args[1];
            if ("save".equals((Object)cmd)) {
                Managers.CONFIG.saveModuleConfig(name);
                ChatUtils.sendMessage("Saved config: \u00a7e" + name + "\u00a7r");
            } else if ("load".equals((Object)cmd)) {
                Managers.CONFIG.loadModuleConfig(name);
                ChatUtils.sendMessage("Loaded config \u00a7e" + name + "\u00a7r");
            } else {
                this.sendSyntax();
            }
        } else {
            this.sendSyntax();
        }
    }
}
