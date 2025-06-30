package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class PlayerJoinEvent
extends EventArgument {
    public String name;

    public PlayerJoinEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPlayerJoin(this);
    }
}
