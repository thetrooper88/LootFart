package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_332;

public class Render2DEvent
extends EventArgument {
    private final float tick;
    private final class_332 context;

    public Render2DEvent(float tick, class_332 context) {
        this.tick = tick;
        this.context = context;
    }

    public float getTick() {
        return this.tick;
    }

    public class_332 getContext() {
        return this.context;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRender2D(this);
    }
}
