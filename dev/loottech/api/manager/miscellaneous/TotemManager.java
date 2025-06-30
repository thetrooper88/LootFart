package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_2663;

public class TotemManager
implements Util,
EventListener {
    private final ConcurrentMap<UUID, TotemData> totems = new ConcurrentHashMap();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_1297 entity;
        class_2663 packet;
        if (TotemManager.mc.field_1687 == null) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2663 && (packet = (class_2663)class_25962).method_11470() == 35 && (entity = packet.method_11469((class_1937)TotemManager.mc.field_1687)) != null && entity.method_5805()) {
            if (this.totems.containsKey((Object)entity.method_5667())) {
                this.totems.replace((Object)entity.method_5667(), (Object)new TotemData(System.currentTimeMillis(), ((TotemData)this.totems.get((Object)entity.method_5667())).getPops() + 1));
            } else {
                this.totems.put((Object)entity.method_5667(), (Object)new TotemData(System.currentTimeMillis(), 1));
            }
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
        this.totems.remove((Object)event.getEntity().method_5667());
    }

    @Override
    public void onLogout(LogoutEvent event) {
        this.totems.clear();
    }

    public int getTotems(class_1297 entity) {
        return ((TotemData)this.totems.getOrDefault((Object)entity.method_5667(), (Object)new TotemData(0L, 0))).getPops();
    }

    public long getLastPopTime(class_1297 entity) {
        return ((TotemData)this.totems.getOrDefault((Object)entity.method_5667(), (Object)new TotemData(-1L, 0))).getLastPopTime();
    }

    public static class TotemData {
        private final long lastPopTime;
        private final int pops;

        public TotemData(long lastPopTime, int pops) {
            this.lastPopTime = lastPopTime;
            this.pops = pops;
        }

        public int getPops() {
            return this.pops;
        }

        public long getLastPopTime() {
            return this.lastPopTime;
        }
    }
}
