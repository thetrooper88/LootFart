package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_4587;

public class ViewBobEvent
extends EventArgument {
    private final class_4587 matrixStack;
    private final float tickDelta;
    private float y;

    public ViewBobEvent(class_4587 matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public class_4587 getMatrixStack() {
        return this.matrixStack;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }

    @Override
    public void call(EventListener listener) {
        listener.onBob(this);
    }
}
