package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class KeyEvent
extends EventArgument {
    private final int keyCode;
    private final int scanCode;

    public KeyEvent(int keyCode, int scanCode) {
        this.keyCode = keyCode;
        this.scanCode = scanCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public int getScanCode() {
        return this.scanCode;
    }

    @Override
    public void call(EventListener listener) {
        listener.onKey(this);
    }
}
