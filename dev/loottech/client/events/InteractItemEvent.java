package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1268;
import net.minecraft.class_1269;

public class InteractItemEvent
extends EventArgument {
    public class_1268 hand;
    public class_1269 toReturn;

    public InteractItemEvent(class_1268 hand) {
        this.hand = hand;
        this.toReturn = null;
    }

    @Override
    public void call(EventListener listener) {
        listener.onInteractItem(this);
    }
}
