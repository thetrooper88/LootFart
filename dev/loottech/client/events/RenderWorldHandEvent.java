package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_4587;

public class RenderWorldHandEvent
extends EventArgument {
    private final class_4587 matrices;
    private final float tickDelta;

    public RenderWorldHandEvent(class_4587 matrices, float tickDelta) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
    }

    public class_4587 getMatrices() {
        return this.matrices;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderWorldHand(this);
    }
}
