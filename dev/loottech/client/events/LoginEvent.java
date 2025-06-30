package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_639;
import net.minecraft.class_642;

public class LoginEvent
extends EventArgument {
    private final class_639 address;
    private final class_642 info;

    public LoginEvent(class_639 address, class_642 info) {
        this.address = address;
        this.info = info;
    }

    public class_639 getAddress() {
        return this.address;
    }

    public class_642 getInfo() {
        return this.info;
    }

    @Override
    public void call(EventListener listener) {
        listener.onLogin(this);
    }
}
