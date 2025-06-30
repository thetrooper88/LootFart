package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.manager.waypoint.Waypoint;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.PlayerUtils;
import net.minecraft.class_2338;

@RegisterCommand(name="waypoint", description="Manage waypoints for navigation.", syntax="waypoint <add|remove> <name> [x y z]", aliases={"wp", "waypoints"})
public class CommandWaypoint
extends Command {
    /*
     * Exception decompiling
     */
    @Override
    public void onCommand(String[] args) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void handleAddWaypoint(String[] args, String name) {
        if (args.length == 2) {
            try {
                int x = CommandWaypoint.mc.field_1724.method_31477();
                int y = CommandWaypoint.mc.field_1724.method_31478();
                int z = CommandWaypoint.mc.field_1724.method_31479();
                String serverIp = mc.method_1558() != null ? CommandWaypoint.mc.method_1558().field_3761 : "singleplayer";
                int dimension = PlayerUtils.getDimension();
                class_2338 pos = new class_2338(x, y, z);
                Waypoint waypoint = new Waypoint(name, pos, dimension, serverIp);
                Managers.WAYPOINT.addWaypoint(waypoint);
                ChatUtils.sendMessage("\u00a7aWaypoint " + name + " added.", "Waypoint");
                return;
            }
            catch (NumberFormatException e) {
                ChatUtils.sendMessage("\u00a7cInvalid parameters.", "Waypoint");
            }
        }
        if (args.length < 5) {
            ChatUtils.sendMessage("\u00a7cPlease provide coordinates: waypoint add <name> <x> <y> <z>", "Waypoint");
            return;
        }
        try {
            int x = Integer.parseInt((String)args[2]);
            int y = Integer.parseInt((String)args[3]);
            int z = Integer.parseInt((String)args[4]);
            String serverIp = mc.method_1558() != null ? CommandWaypoint.mc.method_1558().field_3761 : "singleplayer";
            int dimension = PlayerUtils.getDimension();
            class_2338 pos = new class_2338(x, y, z);
            Waypoint waypoint = new Waypoint(name, pos, dimension, serverIp);
            Managers.WAYPOINT.addWaypoint(waypoint);
            ChatUtils.sendMessage("\u00a7aWaypoint " + name + " added.", "Waypoint");
        }
        catch (NumberFormatException e) {
            ChatUtils.sendMessage("\u00a7cInvalid coordinates. Please use numbers.", "Waypoint");
        }
    }

    private void handleRemoveWaypoint(String name) {
        char chr = '\"';
        if (Managers.WAYPOINT.getWaypoints().stream().anyMatch(w -> w.getName().equalsIgnoreCase(name))) {
            Managers.WAYPOINT.removeWaypoint(name);
            ChatUtils.sendMessage("\u00a7aRemoved all waypoints with the name " + chr + name + chr, "Waypoint");
        } else {
            ChatUtils.sendMessage("\u00a7cWaypoint '" + name + "' not found", "Waypoint");
        }
    }

    @Override
    public void sendSyntax() {
        ChatUtils.sendMessage("\u00a76Waypoint Commands:", "Waypoint");
        ChatUtils.sendMessage("\u00a7e- waypoint add <name> <x> <y> <z> \u00a77- Add a new waypoint", "Waypoint");
        ChatUtils.sendMessage("\u00a7e- waypoint remove <name> \u00a77- Remove a waypoint", "Waypoint");
        ChatUtils.sendMessage("\u00a7e- waypoint list \u00a77- List all waypoints (not implemented)", "Waypoint");
    }
}
