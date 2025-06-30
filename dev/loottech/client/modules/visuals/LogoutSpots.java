package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.api.utilities.render.RenderBuffers;
import dev.loottech.api.utilities.render.RenderManager;
import dev.loottech.client.events.PlayerJoinEvent;
import dev.loottech.client.events.PlayerLeaveEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.values.impl.ValueBoolean;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4184;

@RegisterModule(name="LogoutSpots", description="Renders a box where a player has logged out.", category=Module.Category.VISUALS)
public class LogoutSpots
extends Module {
    private final ValueBoolean notifyLogout = new ValueBoolean("NotifyLogout", "Send a message in chat when a player has logged out.", false);
    private final ValueBoolean notifyJoin = new ValueBoolean("NotifyJoin", "Send a message in chat when a player has logged in.", false);
    private final ValueBoolean showTime = new ValueBoolean("ShowTime", "Display how long ago the player logged out (in minutes)", true);
    private final Map<String, LogoutSpot> logoutSpots = new ConcurrentHashMap();

    @Override
    public void onLogout() {
        this.logoutSpots.clear();
    }

    @Override
    public void onLogin() {
        this.logoutSpots.clear();
    }

    @Override
    public void onDisable() {
        this.logoutSpots.clear();
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (LogoutSpots.mc.field_1687 == null || LogoutSpots.mc.field_1724 == null) {
            return;
        }
        for (Map.Entry entry : this.logoutSpots.entrySet()) {
            LogoutSpot spot = (LogoutSpot)((Object)entry.getValue());
            class_4184 camera = LogoutSpots.mc.field_1773.method_19418();
            class_243 pos = spot.position;
            class_243 vec = new class_243(pos.field_1352, pos.field_1351 + 1.0, pos.field_1350);
            class_238 box = new class_238(pos.field_1352 - 0.3, pos.field_1351, pos.field_1350 - 0.3, pos.field_1352 + 0.3, pos.field_1351 + 1.85, pos.field_1350 + 0.3);
            float scale = Math.max((float)0.015f, (float)((float)(camera.method_19326().method_1022(vec) * (double)0.003f)));
            RenderBuffers.preRender();
            RenderManager.drawBoundingBox(event.getMatrices(), box, ModuleColor.getColor().getRGB());
            RenderManager.drawBox(event.getMatrices(), box, new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), 30).getRGB());
            String displayText = spot.playerName + " Logout";
            if (this.showTime.getValue()) {
                long minutesAgo = (System.currentTimeMillis() - spot.timestamp) / 60000L;
                displayText = displayText + String.format((String)" (%dm ago)", (Object[])new Object[]{minutesAgo});
            }
            RenderManager.renderSign(displayText, vec.field_1352, vec.field_1351 + 1.25, vec.field_1350, 0.0f, 0.0f, scale, Color.WHITE.getRGB());
            RenderBuffers.postRender();
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getName();
        if (this.logoutSpots.containsKey((Object)playerName)) {
            this.logoutSpots.remove((Object)playerName);
            if (this.notifyJoin.getValue()) {
                this.sendString("\u00a7a" + playerName + " has logged back in.");
            }
        }
    }

    @Override
    public void onPlayerLeave(PlayerLeaveEvent event) {
        String playerName = event.getName();
        class_1657 player = (class_1657)LogoutSpots.mc.field_1687.method_18456().stream().filter(p -> p.method_5477().getString().equals((Object)playerName)).findFirst().orElse(null);
        if (player != null) {
            this.logoutSpots.put((Object)playerName, (Object)new LogoutSpot(player.method_19538(), playerName, System.currentTimeMillis()));
            if (this.notifyLogout.getValue()) {
                this.sendString("\u00a7c" + playerName + " has logged out.");
            }
        }
    }

    private void sendString(String message) {
        ChatUtils.sendMessage(message, "LogoutSpots");
    }

    private record LogoutSpot(class_243 position, String playerName, long timestamp) {
    }
}
