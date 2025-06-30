package dev.loottech.api.manager.command;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.command.Command;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.commands.ColorCommand;
import dev.loottech.client.commands.CommandBind;
import dev.loottech.client.commands.CommandConfig;
import dev.loottech.client.commands.CommandDrawn;
import dev.loottech.client.commands.CommandFolder;
import dev.loottech.client.commands.CommandFriend;
import dev.loottech.client.commands.CommandNotify;
import dev.loottech.client.commands.CommandPrefix;
import dev.loottech.client.commands.CommandTag;
import dev.loottech.client.commands.CommandToggle;
import dev.loottech.client.commands.CommandWaypoint;
import dev.loottech.client.commands.Coords;
import dev.loottech.client.events.ChatSendEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager
implements IMinecraft,
EventListener {
    private String prefix = ".";
    private final ArrayList<Command> commands;

    public CommandManager() {
        Managers.EVENT.register(this);
        this.commands = new ArrayList();
        this.register(new CommandBind());
        this.register(new CommandConfig());
        this.register(new CommandDrawn());
        this.register(new CommandFolder());
        this.register(new CommandFriend());
        this.register(new CommandNotify());
        this.register(new CommandPrefix());
        this.register(new CommandTag());
        this.register(new CommandToggle());
        this.register(new Coords());
        this.register(new CommandWaypoint());
        this.register(new ColorCommand());
    }

    public void register(Command command) {
        this.commands.add((Object)command);
    }

    @Override
    public void onChatSend(ChatSendEvent event) {
        String message = event.getMessage();
        if (message.startsWith(this.getPrefix())) {
            event.cancel();
            message = message.substring(this.getPrefix().length());
            if (message.split(" ").length > 0) {
                String name = message.split(" ")[0];
                boolean found = false;
                for (Command command : this.getCommands()) {
                    if (!command.getAliases().contains((Object)name.toLowerCase()) && !command.getName().equalsIgnoreCase(name)) continue;
                    CommandManager.mc.field_1705.method_1743().method_1803(event.getMessage());
                    command.onCommand((String[])Arrays.copyOfRange((Object[])message.split(" "), (int)1, (int)message.split(" ").length));
                    found = true;
                    break;
                }
                if (!found) {
                    ChatUtils.sendMessage("Command could not be found.");
                }
            }
        }
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
