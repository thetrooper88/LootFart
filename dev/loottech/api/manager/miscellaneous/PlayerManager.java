package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.modules.client.FastLatency;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_2868;

public class PlayerManager
implements IMinecraft,
EventListener {
    private boolean switching;
    private boolean sneaking;
    private int slot;
    private int sentPackets;
    private int receivedPackets;

    public PlayerManager() {
        Managers.EVENT.register(this);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2848) {
            class_2848 a = (class_2848)class_25962;
            if (a.method_12365() == class_2848.class_2849.field_12979) {
                this.sneaking = true;
            } else if (a.method_12365() == class_2848.class_2849.field_12984) {
                this.sneaking = false;
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2868) {
            class_2868 b = (class_2868)class_25962;
            this.slot = b.method_12442();
        }
    }

    public int getPing() {
        if (mc.method_1562().method_2871(PlayerManager.mc.field_1724.method_5667()) == null) {
            return -1;
        }
        int ping = -1;
        int fastLatency = (int)Managers.MODULE.getInstance(FastLatency.class).getLatency();
        int server = mc.method_1562().method_2871(PlayerManager.mc.field_1724.method_5667()).method_2959();
        if (Managers.MODULE.isModuleEnabled("FastLatency") && fastLatency != -1) {
            ping = fastLatency;
        } else if (server != -1) {
            ping = server;
        }
        return ping;
    }

    public boolean isSwitching() {
        return this.switching;
    }

    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getSentPackets() {
        return this.sentPackets;
    }

    public int getReceivedPackets() {
        return this.receivedPackets;
    }
}
