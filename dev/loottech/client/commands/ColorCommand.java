package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.client.ModuleCommands;
import java.awt.Color;

@RegisterCommand(name="color", description="Let's you change your command prefix.", syntax="color <#hexvalue>", aliases={"colors"})
public class ColorCommand
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            String hex = args[0];
            if (!hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8}|[A-Fa-f0-9]{3})$")) {
                ChatUtils.sendMessage("Invalid hex format! Use #RRGGBB or #RRGGBBAA.", "Color");
                return;
            }
            try {
                Color color = Color.decode((String)(hex.length() <= 4 ? this.expandShortHex(hex) : hex));
                Managers.MODULE.getInstance(ModuleColor.class).color.setValue(color);
                ChatUtils.sendMessage("Command prefix color set to " + String.valueOf((Object)ModuleCommands.getSecondColor()) + hex + String.valueOf((Object)ModuleCommands.getFirstColor()) + "!", "Color");
            }
            catch (Exception e) {
                ChatUtils.sendMessage("Failed to set color: " + e.getMessage(), "Color");
            }
        } else {
            this.sendSyntax();
        }
    }

    private String expandShortHex(String shortHex) {
        if (shortHex.length() == 4) {
            return "#" + shortHex.charAt(1) + shortHex.charAt(1) + shortHex.charAt(2) + shortHex.charAt(2) + shortHex.charAt(3) + shortHex.charAt(3);
        }
        if (shortHex.length() == 5) {
            return "#" + shortHex.charAt(1) + shortHex.charAt(1) + shortHex.charAt(2) + shortHex.charAt(2) + shortHex.charAt(3) + shortHex.charAt(3) + shortHex.charAt(4) + shortHex.charAt(4);
        }
        return shortHex;
    }
}
