package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class PlayerLeaveEvent
extends EventArgument {
    public String name;

    public PlayerLeaveEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPlayerLeave(this);
    }
}
