package dev.loottech.api.manager.event;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> LISTENER_REGISTRY = new ArrayList();

    public void call(EventArgument argument) {
        new ArrayList(this.LISTENER_REGISTRY).forEach(argument::call);
    }

    public void register(EventListener listener) {
        this.LISTENER_REGISTRY.add((Object)listener);
    }

    public boolean unregister(EventListener listener) {
        return this.LISTENER_REGISTRY.remove((Object)listener);
    }
}
