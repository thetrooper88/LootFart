package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1799;

public class FinishUsingEvent
extends EventArgument {
    private final class_1799 stack;

    public FinishUsingEvent(class_1799 stack) {
        this.stack = stack;
    }

    public class_1799 getStack() {
        return this.stack;
    }

    @Override
    public void call(EventListener listener) {
        listener.onFinishUsing(this);
    }
}
