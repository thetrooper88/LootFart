package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class PrePlayerJumpEvent
extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onPrePlayerJump(this);
    }
}
