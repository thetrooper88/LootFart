package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.SetbackData;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import java.util.Arrays;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_6373;

public final class AntiCheatManager
implements Util,
EventListener {
    private SetbackData lastSetback;
    private final int[] transactions = new int[4];
    private int index;
    private boolean isGrim;

    public AntiCheatManager() {
        Arrays.fill((int[])this.transactions, (int)-1);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_6373) {
            int uid;
            class_6373 packet = (class_6373)class_25962;
            if (this.index > 3) {
                return;
            }
            this.transactions[this.index] = uid = packet.method_36950();
        } else {
            class_25962 = event.getPacket();
            if (class_25962 instanceof class_2708) {
                class_2708 packet = (class_2708)class_25962;
                this.lastSetback = new SetbackData(new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738()), System.currentTimeMillis(), packet.method_11737());
            }
        }
    }

    @Override
    public void onLogout(LogoutEvent event) {
        Arrays.fill((int[])this.transactions, (int)-1);
        this.index = 0;
        this.isGrim = false;
    }

    public boolean isGrim() {
        return this.isGrim;
    }

    public boolean hasPassed(long timeMS) {
        return this.lastSetback != null && this.lastSetback.timeSince() >= timeMS;
    }
}
