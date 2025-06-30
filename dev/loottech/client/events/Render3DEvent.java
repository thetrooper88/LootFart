package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_4587;

public class Render3DEvent
extends EventArgument {
    private final float tick;
    private final class_4587 matrices;

    public Render3DEvent(float tick, class_4587 matrices) {
        this.tick = tick;
        this.matrices = matrices;
    }

    public float getTickDelta() {
        return this.tick;
    }

    public class_4587 getMatrices() {
        return this.matrices;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRender3D(this);
    }
}
