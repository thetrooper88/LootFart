package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_4184;

public class PerspectiveEvent
extends EventArgument {
    public class_4184 camera;

    public PerspectiveEvent(class_4184 camera) {
        this.camera = camera;
    }

    public class_4184 getCamera() {
        return this.camera;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPerspective(this);
    }
}
