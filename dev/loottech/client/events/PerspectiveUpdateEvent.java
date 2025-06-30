package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_5498;

public class PerspectiveUpdateEvent
extends EventArgument {
    private final class_5498 perspective;

    public PerspectiveUpdateEvent(class_5498 perspective) {
        this.perspective = perspective;
    }

    public class_5498 getPerspective() {
        return this.perspective;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPerspectiveUpdate(this);
    }
}
