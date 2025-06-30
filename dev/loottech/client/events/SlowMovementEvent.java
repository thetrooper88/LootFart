package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_2680;

public class SlowMovementEvent
extends EventArgument {
    final class_2680 state;

    public class_2680 getState() {
        return this.state;
    }

    public SlowMovementEvent(class_2680 state) {
        this.state = state;
    }

    @Override
    public void call(EventListener listener) {
    }
}
