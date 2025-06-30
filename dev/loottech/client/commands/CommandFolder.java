package dev.loottech.client.commands;

import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import java.io.File;
import net.minecraft.class_156;

@RegisterCommand(name="folder", description="Opens the Configuration folder.", syntax="folder", aliases={"openfolder", "cfgfldr", "cfgfolder", "configfolder"})
public class CommandFolder
extends Command {
    @Override
    public void onCommand(String[] args) {
        class_156.method_668().method_672(new File("LootTech/"));
    }
}
