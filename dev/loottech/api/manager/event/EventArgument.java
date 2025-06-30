package dev.loottech.api.manager.event;

import dev.loottech.api.manager.event.EventListener;

public abstract class EventArgument {
    private boolean cancel = false;

    protected EventArgument() {
    }

    public final void cancel() {
        this.cancel = true;
    }

    public final boolean isCanceled() {
        return this.cancel;
    }

    public abstract void call(EventListener var1);
}
