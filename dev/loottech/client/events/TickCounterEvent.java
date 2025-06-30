package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class TickCounterEvent
extends EventArgument {
    private float ticks;

    public float getTicks() {
        return this.ticks;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }

    @Override
    public void call(EventListener listener) {
        listener.onTickCounter(this);
    }
}
