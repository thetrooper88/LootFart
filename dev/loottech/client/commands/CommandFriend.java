package dev.loottech.client.commands;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.command.RegisterCommand;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.modules.client.ModuleCommands;
import net.minecraft.class_124;

@RegisterCommand(name="Friend", description="Let's you add friends.", syntax="friend <add/del> <name>", aliases={"friend", "f"})
public class CommandFriend
extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ChatUtils.sendMessage("You have " + (Managers.FRIEND.getFriends().size() + 1) + " friends");
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (Managers.FRIEND.isFriend(args[1])) {
                    ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + args[1] + String.valueOf((Object)ModuleCommands.getFirstColor()) + " is already a friend!");
                    return;
                }
                if (!Managers.FRIEND.isFriend(args[1])) {
                    Managers.FRIEND.addFriend(args[1]);
                    ChatUtils.sendMessage(String.valueOf((Object)class_124.field_1060) + "Added " + String.valueOf((Object)ModuleCommands.getSecondColor()) + args[1] + String.valueOf((Object)ModuleCommands.getFirstColor()) + " to friends list");
                }
            }
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
                if (!Managers.FRIEND.isFriend(args[1])) {
                    ChatUtils.sendMessage(String.valueOf((Object)ModuleCommands.getSecondColor()) + args[1] + String.valueOf((Object)ModuleCommands.getFirstColor()) + " is not a friend!");
                    return;
                }
                if (Managers.FRIEND.isFriend(args[1])) {
                    Managers.FRIEND.removeFriend(args[1]);
                    ChatUtils.sendMessage(String.valueOf((Object)class_124.field_1061) + "Removed " + String.valueOf((Object)ModuleCommands.getSecondColor()) + args[1] + String.valueOf((Object)ModuleCommands.getFirstColor()) + " from friends list");
                }
            }
        } else {
            this.sendSyntax();
        }
    }
}
