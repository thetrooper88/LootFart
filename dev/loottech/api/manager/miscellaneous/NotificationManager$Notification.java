package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.client.Notifications;

private static class NotificationManager.Notification {
    private final String message;
    private final int color;
    private final long creationTime;
    private long fadeStartTime = -1L;
    private final long durationMillis;
    private boolean animating = false;

    public NotificationManager.Notification(String message, int color) {
        this.message = message;
        this.color = color;
        this.creationTime = System.currentTimeMillis();
        this.durationMillis = (long)(Managers.MODULE.getInstance(Notifications.class).duration.getValue().doubleValue() * 1000.0);
    }

    public String getMessage() {
        return this.message;
    }

    public int getColor() {
        return this.color;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return currentTime > this.creationTime + this.durationMillis + 250L;
    }

    public boolean isFading() {
        long currentTime = System.currentTimeMillis();
        if (this.fadeStartTime == -1L && currentTime > this.creationTime + this.durationMillis) {
            this.fadeStartTime = currentTime;
        }
        return this.fadeStartTime != -1L;
    }

    public long getFadeStartTime() {
        return this.fadeStartTime;
    }

    public boolean isAnimating() {
        return this.animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }
}
