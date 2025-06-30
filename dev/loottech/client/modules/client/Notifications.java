package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.events.ChatSendEvent;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.TotemPopEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;

@RegisterModule(name="Notifications", category=Module.Category.CLIENT, tag="Notifications", description="Controls notifications in the client.", drawn=false)
public class Notifications
extends Module {
    public final ValueNumber duration = new ValueNumber("Duration (S)", "Duration(S)", "How long the fucking notification should fucking last.", (Number)Double.valueOf((double)1.0), (Number)Double.valueOf((double)0.1), (Number)Double.valueOf((double)10.0));
    public final ValueEnum notificationPosition = new ValueEnum("Position", "Position", "Fucking Notification position.", (Enum)NotificationPositions.TOP_CENTER);
    public final ValueBoolean players = new ValueBoolean("VisualRange", "VisualRange", "", true);
    public final ValueBoolean mentions = new ValueBoolean("ChatMentions", "ChatMentions", "", true);
    public final ValueBoolean toggle = new ValueBoolean("ModuleToggle", "ModuleToggle", "", false);
    public final ValueBoolean totems = new ValueBoolean("TotemPops", "TotemPops", "", true);
    public final ValueBoolean autoSave = new ValueBoolean("Config AutoSave", "Config AutoSave", "", true);
    private final Map<UUID, String> knownPlayers = new ConcurrentHashMap();
    private final Map<UUID, Integer> totemPops = new ConcurrentHashMap();
    private final Map<UUID, Long> lastPopTime = new ConcurrentHashMap();

    @Override
    public void onEnable() {
        this.knownPlayers.clear();
        this.totemPops.clear();
        this.lastPopTime.clear();
    }

    @Override
    public void onTotemPop(TotemPopEvent event) {
        if (Notifications.mc.field_1687 == null || Notifications.mc.field_1724 == null || !this.totems.getValue()) {
            return;
        }
        this.handleTotemPop(event.getPlayer());
    }

    @Override
    public void onDeath(DeathEvent event) {
        if (Notifications.mc.field_1687 == null || Notifications.mc.field_1724 == null) {
            return;
        }
        this.handlePlayerDeath(event.getEntity().method_5667(), event.getEntity().method_5477().method_54160());
    }

    @Override
    public void onTick() {
        if (!this.players.getValue() || Notifications.mc.field_1687 == null || Notifications.mc.field_1724 == null) {
            return;
        }
        HashMap currentPlayers = new HashMap();
        ArrayList worldPlayers = new ArrayList((Collection)Notifications.mc.field_1687.method_18456());
        for (class_1657 entity : worldPlayers) {
            if (!(entity instanceof class_1657) || entity == Notifications.mc.field_1724) continue;
            UUID uuid2 = entity.method_5667();
            String name2 = entity.method_5477().getString();
            currentPlayers.put((Object)uuid2, (Object)name2);
        }
        HashMap newPlayers = new HashMap();
        currentPlayers.forEach((arg_0, arg_1) -> this.lambda$onTick$0((Map)newPlayers, arg_0, arg_1));
        HashMap leftPlayers = new HashMap();
        this.knownPlayers.forEach((arg_0, arg_1) -> Notifications.lambda$onTick$1((Map)currentPlayers, (Map)leftPlayers, arg_0, arg_1));
        newPlayers.forEach((uuid, name) -> {
            this.sendPlayerAlert((String)name, true);
            this.knownPlayers.put(uuid, name);
        });
        leftPlayers.forEach((uuid, name) -> {
            this.sendPlayerAlert((String)name, false);
            this.knownPlayers.remove(uuid);
        });
    }

    public void handleTotemPop(class_1657 player) {
        UUID uuid = player.method_5667();
        String name = player.method_5477().getString();
        boolean isSelf = player == Notifications.mc.field_1724;
        int pops = (Integer)this.totemPops.getOrDefault((Object)uuid, (Object)0) + 1;
        this.totemPops.put((Object)uuid, (Object)pops);
        this.lastPopTime.put((Object)uuid, (Object)System.currentTimeMillis());
        String message = isSelf ? String.valueOf((Object)class_124.field_1065) + "You have popped " + pops + " totem" + (pops != 1 ? "s" : "") : String.valueOf((Object)class_124.field_1065) + name + " has popped " + pops + " totem" + (pops != 1 ? "s" : "");
        int color = isSelf ? Color.YELLOW.getRGB() : Color.ORANGE.getRGB();
        Managers.NOTIFICATION.send(message, color);
    }

    private void handlePlayerDeath(UUID uuid, String name) {
        if (this.totemPops.containsKey((Object)uuid)) {
            int pops = (Integer)this.totemPops.get((Object)uuid);
            boolean isSelf = uuid.equals((Object)Notifications.mc.field_1724.method_5667());
            String message = isSelf ? String.valueOf((Object)class_124.field_1061) + "You died after popping " + pops + " totem" + (pops != 1 ? "s" : "") : String.valueOf((Object)class_124.field_1061) + name + " has died after popping " + pops + " totem" + (pops != 1 ? "s" : "");
            int color = isSelf ? new Color(255, 100, 100).getRGB() : Color.RED.getRGB();
            Managers.NOTIFICATION.send(message, color);
            this.totemPops.remove((Object)uuid);
            this.lastPopTime.remove((Object)uuid);
        }
    }

    public void handleToggle(Module module) {
        int color = module.isEnabled() ? Color.GREEN.getRGB() : Color.RED.getRGB();
        String message = module.getTag() + " toggled " + (module.isEnabled() ? "on." : "off.");
        Managers.NOTIFICATION.send(message, color);
    }

    @Override
    public void onChatSend(ChatSendEvent event) {
        String myName;
        if (!this.mentions.getValue() || Notifications.mc.field_1724 == null) {
            return;
        }
        String rawMessage = event.getMessage();
        if (rawMessage.contains((CharSequence)(myName = Notifications.mc.field_1724.method_5477().method_54160()))) {
            String message = String.valueOf((Object)class_124.field_1065) + "[Chat] You were mentioned!";
            ChatUtils.sendMessage(message);
        }
    }

    private void sendPlayerAlert(String playerName, boolean entered) {
        double distance;
        int color;
        String message = entered ? "[+] " + playerName + " entered range" : "[-] " + playerName + " left range";
        int n = color = entered ? Color.GREEN.getRGB() : Color.RED.getRGB();
        if (entered && (distance = this.getDistanceToPlayer(playerName)) > 0.0) {
            message = message + " (" + String.format((String)"%.1fm", (Object[])new Object[]{distance}) + ")";
        }
        Managers.NOTIFICATION.send(message, color);
    }

    private double getDistanceToPlayer(String name) {
        for (class_1297 entity : Notifications.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1657) || !entity.method_5477().getString().equals((Object)name)) continue;
            return Notifications.mc.field_1724.method_19538().method_1022(entity.method_19538());
        }
        return -1.0;
    }

    private static /* synthetic */ void lambda$onTick$1(Map currentPlayers, Map leftPlayers, UUID uuid, String name) {
        if (!currentPlayers.containsKey((Object)uuid)) {
            leftPlayers.put((Object)uuid, (Object)name);
        }
    }

    private /* synthetic */ void lambda$onTick$0(Map newPlayers, UUID uuid, String name) {
        if (!this.knownPlayers.containsKey((Object)uuid)) {
            newPlayers.put((Object)uuid, (Object)name);
        }
    }

    public static enum NotificationPositions {
        TOP_CENTER,
        LEFT,
        RIGHT,
        BOTTOM_RIGHT;

    }
}
