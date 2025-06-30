package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1297;

public class RemoveEntityEvent
extends EventArgument {
    private class_1297 entity;

    public RemoveEntityEvent(class_1297 entity) {
        this.entity = entity;
    }

    public class_1297 getEntity() {
        return this.entity;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRemoveEntity(this);
    }
}
