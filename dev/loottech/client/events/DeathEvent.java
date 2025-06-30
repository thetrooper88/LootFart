package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1309;

public class DeathEvent
extends EventArgument {
    private final class_1309 entity;

    public DeathEvent(class_1309 entity) {
        this.entity = entity;
    }

    public class_1309 getEntity() {
        return this.entity;
    }

    @Override
    public void call(EventListener listener) {
        listener.onDeath(this);
    }
}
