package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_437;

public class OpenScreenEvent
extends EventArgument {
    public class_437 screen;

    public OpenScreenEvent(class_437 screen) {
        this.screen = screen;
    }

    @Override
    public void call(EventListener listener) {
        listener.onScreenOpen(this);
    }
}
