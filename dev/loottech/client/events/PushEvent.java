package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class PushEvent
extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onPush(this);
    }
}
