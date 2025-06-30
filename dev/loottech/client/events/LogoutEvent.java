package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class LogoutEvent
extends EventArgument {
    @Override
    public void call(EventListener listener) {
        listener.onLogout(this);
    }
}
