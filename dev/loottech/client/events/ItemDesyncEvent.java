package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1799;

public class ItemDesyncEvent
extends EventArgument {
    private class_1799 stack;

    public void setStack(class_1799 stack) {
        this.stack = stack;
    }

    public class_1799 getServerItem() {
        return this.stack;
    }

    @Override
    public void call(EventListener listener) {
        listener.onItemDesync(this);
    }
}
