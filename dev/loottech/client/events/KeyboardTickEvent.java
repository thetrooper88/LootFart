package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_744;

public class KeyboardTickEvent
extends EventArgument {
    private final class_744 input;

    public KeyboardTickEvent(class_744 input) {
        this.input = input;
    }

    public class_744 getInput() {
        return this.input;
    }

    @Override
    public void call(EventListener listener) {
        listener.onKeyboardTick(this);
    }
}
