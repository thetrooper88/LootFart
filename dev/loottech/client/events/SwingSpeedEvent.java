package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class SwingSpeedEvent
extends EventArgument {
    int swingSpeed;
    boolean selfOnly;

    public void setSwingSpeed(int swingSpeed) {
        this.swingSpeed = swingSpeed;
    }

    public int getSwingSpeed() {
        return this.swingSpeed;
    }

    public void setSelfOnly(boolean selfOnly) {
        this.selfOnly = selfOnly;
    }

    public boolean getSelfOnly() {
        return this.selfOnly;
    }

    @Override
    public void call(EventListener listener) {
    }
}
