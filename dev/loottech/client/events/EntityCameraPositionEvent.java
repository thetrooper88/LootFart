package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1297;
import net.minecraft.class_243;

public class EntityCameraPositionEvent
extends EventArgument {
    private class_243 position;
    private final float tickDelta;
    private final class_1297 entity;

    public EntityCameraPositionEvent(class_243 position, class_1297 entity, float tickDelta) {
        this.position = position;
        this.tickDelta = tickDelta;
        this.entity = entity;
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

    public class_1297 getEntity() {
        return this.entity;
    }

    @Override
    public void call(EventListener listener) {
        listener.onEntityCameraPosition(this);
    }
}
