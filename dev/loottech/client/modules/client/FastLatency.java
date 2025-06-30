package dev.loottech.client.modules.client;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.CacheTimer;
import dev.loottech.client.events.LogoutEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.TickEvent;
import net.minecraft.class_2596;
import net.minecraft.class_2639;
import net.minecraft.class_2805;

@RegisterModule(name="FastLatency", category=Module.Category.CLIENT, tag="FastLatency")
public class FastLatency
extends Module {
    private final CacheTimer lastRequest = new CacheTimer();
    private final CacheTimer requestTimer = new CacheTimer();
    private long requestTime;
    private long latency;

    @Override
    public String getHudInfo() {
        return String.format((String)"%dms", (Object[])new Object[]{this.latency});
    }

    @Override
    public void onLogout(LogoutEvent event) {
        this.latency = 0L;
        this.requestTime = 0L;
    }

    @Override
    public void onTick(TickEvent event) {
        if (this.lastRequest.passed((Number)Integer.valueOf((int)5000)) && this.requestTimer.passed((Number)Integer.valueOf((int)500))) {
            Managers.NETWORK.sendPacket((class_2596<?>)new class_2805(1000, "w "));
            this.requestTimer.reset();
            this.lastRequest.reset();
            this.requestTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        class_2639 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2639 && (packet = (class_2639)class_25962).comp_2262() == 1000) {
            this.latency = System.currentTimeMillis() - this.requestTime;
            this.lastRequest.setElapsedTime((Number)Long.valueOf((long)-255L));
        }
    }

    public long getLatency() {
        return this.latency;
    }
}
