package dev.loottech.api.utilities;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.client.events.PacketReceiveEvent;
import java.util.Arrays;
import net.minecraft.class_2761;
import net.minecraft.class_3532;

public class TPSUtils
implements EventListener {
    private static final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1L;

    public TPSUtils() {
        Arrays.fill((float[])tickRates, (float)0.0f);
        Managers.EVENT.register(this);
    }

    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (!(tickRate > 0.0f)) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return class_3532.method_15363((float)(sumTickRates / numTicks), (float)0.0f, (float)20.0f);
    }

    public static float getTpsFactor() {
        float TPS = TPSUtils.getTickRate();
        return 20.0f / TPS;
    }

    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            TPSUtils.tickRates[this.nextIndex % TPSUtils.tickRates.length] = class_3532.method_15363((float)(20.0f / timeElapsed), (float)0.0f, (float)20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof class_2761) {
            this.onTimeUpdate();
        }
    }
}
