package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class ChatSendEvent
extends EventArgument {
    private final String message;

    public ChatSendEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void call(EventListener listener) {
        listener.onChatSend(this);
    }
}
