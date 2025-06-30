package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1297;

public class AddEntityEvent
extends EventArgument {
    private final class_1297 entity;

    public AddEntityEvent(class_1297 entity) {
        this.entity = entity;
    }

    public class_1297 getEntity() {
        return this.entity;
    }

    @Override
    public void call(EventListener listener) {
        listener.onAddEntity(this);
    }
}
