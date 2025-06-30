package dev.loottech.client.events;

public class Event {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
