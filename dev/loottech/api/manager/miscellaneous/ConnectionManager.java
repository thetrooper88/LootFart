package dev.loottech.api.manager.miscellaneous;

import com.google.common.collect.Lists;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.api.utilities.interfaces.EvictingQueue;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PlayerJoinEvent;
import dev.loottech.client.events.PlayerLeaveEvent;
import dev.loottech.client.events.TickCounterEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.UUID;
import net.minecraft.class_2703;
import net.minecraft.class_2761;
import net.minecraft.class_640;
import net.minecraft.class_7828;

public class ConnectionManager
implements Util,
EventListener {
    private final Deque<Float> ticks = new EvictingQueue(20);
    private long time;
    private float clientTick = 1.0f;

    @Override
    public void onLogout(LogoutEvent event) {
        this.ticks.clear();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Iterator iterator;
        class_2703 packet;
        Iterator iterator2;
        if (ConnectionManager.mc.field_1724 == null || ConnectionManager.mc.field_1687 == null) {
            return;
        }
        if (event.getPacket() instanceof class_2761) {
            float last = 20000.0f / (float)(System.currentTimeMillis() - this.time);
            this.ticks.addFirst((Object)Float.valueOf((float)last));
            this.time = System.currentTimeMillis();
        }
        if ((iterator2 = event.getPacket()) instanceof class_2703 && (packet = (class_2703)iterator2).method_46327().contains((Object)class_2703.class_5893.field_29136)) {
            for (class_2703.class_2705 entry : packet.method_46329()) {
                Managers.EVENT.call(new PlayerJoinEvent(entry.comp_1107().getName()));
            }
        }
        if ((iterator = event.getPacket()) instanceof class_7828) {
            List profileIds;
            class_7828 class_78282 = (class_7828)iterator;
            try {
                List list;
                profileIds = list = class_78282.comp_1105();
            }
            catch (Throwable throwable) {
                throw new MatchException(throwable.toString(), throwable);
            }
            if (mc.method_1562() == null) {
                return;
            }
            for (UUID uuid : profileIds) {
                class_640 toRemove = mc.method_1562().method_2871(uuid);
                if (toRemove == null) continue;
                Managers.EVENT.call(new PlayerLeaveEvent(toRemove.method_2966().getName()));
            }
        }
    }

    public void setClientTick(float ticks) {
        this.clientTick = ticks;
    }

    @Override
    public void onTickCounter(TickCounterEvent event) {
        if (this.clientTick != 1.0f) {
            event.cancel();
            event.setTicks(this.clientTick);
        }
    }

    public Queue<Float> getTicks() {
        return this.ticks;
    }

    public float getTpsAverage() {
        float avg = 0.0f;
        try {
            ArrayList ticksCopy = Lists.newArrayList(this.ticks);
            if (!ticksCopy.isEmpty()) {
                Iterator iterator = ticksCopy.iterator();
                while (iterator.hasNext()) {
                    float t = ((Float)iterator.next()).floatValue();
                    avg += t;
                }
                avg /= Math.max((float)ticksCopy.size(), (float)1.0f);
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return Math.min((float)100.0f, (float)avg);
    }

    public float getTpsCurrent() {
        try {
            if (!this.ticks.isEmpty()) {
                return Math.min((float)100.0f, (float)((Float)this.ticks.getFirst()).floatValue());
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
        return 20.0f;
    }

    public float getTpsMin() {
        float min = 20.0f;
        try {
            Iterator iterator = this.ticks.iterator();
            while (iterator.hasNext()) {
                float t = ((Float)iterator.next()).floatValue();
                if (!(t < min)) continue;
                min = t;
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return min;
    }

    public boolean isTicksFilled() {
        return this.ticks.size() >= 20;
    }
}
