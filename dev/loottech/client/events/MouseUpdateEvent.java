package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class MouseUpdateEvent
extends EventArgument {
    private final double cursorDeltaX;
    private final double cursorDeltaY;

    public MouseUpdateEvent(double cursorDeltaX, double cursorDeltaY) {
        this.cursorDeltaX = cursorDeltaX;
        this.cursorDeltaY = cursorDeltaY;
    }

    public double getCursorDeltaX() {
        return this.cursorDeltaX;
    }

    public double getCursorDeltaY() {
        return this.cursorDeltaY;
    }

    @Override
    public void call(EventListener listener) {
        listener.onMouseUpdate(this);
    }
}
