package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1309;

public class RenderThroughWallsEvent
extends EventArgument {
    private class_1309 entity;

    public RenderThroughWallsEvent(class_1309 entity) {
        this.entity = entity;
    }

    public class_1309 getEntity() {
        return this.entity;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderThroughWalls(this);
    }
}
