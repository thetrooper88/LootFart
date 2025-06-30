package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1297;
import net.minecraft.class_243;

public class EntityRotationVectorEvent
extends EventArgument {
    private final class_1297 entity;
    private final float tickDelta;
    private class_243 position;

    public EntityRotationVectorEvent(float tickDelta, class_1297 entity, class_243 position) {
        this.entity = entity;
        this.tickDelta = tickDelta;
        this.position = position;
    }

    public class_1297 getEntity() {
        return this.entity;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }

    public class_243 getPosition() {
        return this.position;
    }

    public void setPosition(class_243 position) {
        this.position = position;
    }

    @Override
    public void call(EventListener listener) {
        listener.onEntityRotation(this);
    }
}
