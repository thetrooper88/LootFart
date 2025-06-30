package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.client.values.Value;

public class ClientEvent
extends EventArgument {
    private final Value value;

    public ClientEvent(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return this.value;
    }

    @Override
    public void call(EventListener listener) {
        listener.onClient(this);
    }
}
