package dev.loottech.client.commands;

import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.text.DecimalFormat;

@RegisterCommand(name="coords", description="Copy coordinates to clipboard.", syntax="coords", aliases={"cords", "pos"})
public class Coords
extends Command {
    @Override
    public void onCommand(String[] args) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection transferable = new StringSelection(this.getCoords());
        clipboard.setContents((Transferable)transferable, null);
        ChatUtils.sendMessage("Copied player coordinates to clipboard!");
    }

    public String getCoords() {
        DecimalFormat decimal = new DecimalFormat("0.0");
        double x = Coords.mc.field_1724.method_23317();
        double y = Coords.mc.field_1724.method_23318();
        double z = Coords.mc.field_1724.method_23321();
        return decimal.format(x) + ", " + decimal.format(y) + ", " + decimal.format(z);
    }
}
