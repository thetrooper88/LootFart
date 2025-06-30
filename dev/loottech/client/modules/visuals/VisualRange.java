package dev.loottech.client.modules.visuals;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.ChatUtils;
import dev.loottech.client.values.impl.ValueBoolean;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_3417;

@RegisterModule(name="VisualRange", tag="VisualRange", description="", category=Module.Category.VISUALS)
public class VisualRange
extends Module {
    private final ValueBoolean displayPosition = new ValueBoolean("DisplayPosition", "DisplayPosition", "", true);
    private final ValueBoolean notify = new ValueBoolean("Notify", "Notify", "", true);
    private final ValueBoolean sound = new ValueBoolean("Sound", "Sound", "", true);
    private final Set<class_1657> trackedPlayers = new HashSet();

    @Override
    public void onEnable() {
        super.onEnable();
        this.trackedPlayers.clear();
        if (VisualRange.mc.field_1724 == null || VisualRange.mc.field_1687 == null) {
            return;
        }
        for (class_1657 player : VisualRange.mc.field_1687.method_18456()) {
            if (player.equals((Object)VisualRange.mc.field_1724)) continue;
            this.trackedPlayers.add((Object)player);
        }
    }

    @Override
    public void onTick() {
        if (VisualRange.mc.field_1724 == null || VisualRange.mc.field_1687 == null) {
            return;
        }
        HashSet currentPlayers = new HashSet((Collection)VisualRange.mc.field_1687.method_18456());
        for (class_1657 player : currentPlayers) {
            if (player.equals((Object)VisualRange.mc.field_1724) || this.trackedPlayers.contains((Object)player)) continue;
            this.handlePlayerEvent(player, true);
            this.trackedPlayers.add((Object)player);
        }
        this.trackedPlayers.removeIf(arg_0 -> this.lambda$onTick$0((Set)currentPlayers, arg_0));
    }

    private void handlePlayerEvent(class_1657 player, boolean entered) {
        String state = entered ? "entered" : "left";
        StringBuilder message = new StringBuilder("[VisualRange] ").append((Object)class_124.field_1075).append(player.method_5476().getString()).append((Object)class_124.field_1080).append(" ").append(state).append(" visual range");
        if (this.displayPosition.getValue()) {
            int x = (int)player.method_23317();
            int y = (int)player.method_23318();
            int z = (int)player.method_23321();
            message.append(" at [").append(x).append(", ").append(y).append(", ").append(z).append("].");
        }
        if (this.notify.getValue()) {
            ChatUtils.sendMessage(message.toString());
        }
        if (entered && this.sound.getValue()) {
            VisualRange.mc.field_1724.method_37908().method_8486(VisualRange.mc.field_1724.method_23317(), VisualRange.mc.field_1724.method_23318(), VisualRange.mc.field_1724.method_23321(), class_3417.field_14709, VisualRange.mc.field_1724.method_5634(), 1.0f, 3.0f, false);
        }
    }

    private /* synthetic */ boolean lambda$onTick$0(Set currentPlayers, class_1657 player) {
        if (!currentPlayers.contains((Object)player)) {
            this.handlePlayerEvent(player, false);
            return true;
        }
        return false;
    }
}
