package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class LightmapUpdateEvent
extends EventArgument {
    private final float tickDelta;

    @Override
    public void call(EventListener listener) {
        listener.onLightmapUpdate(this);
    }

    public LightmapUpdateEvent(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }
}
