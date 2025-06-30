package dev.loottech.api.manager.miscellaneous;

import com.google.common.eventbus.Subscribe;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.client.Notifications;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager
implements Util,
EventListener {
    private final List<Notification> notifications = new CopyOnWriteArrayList();
    private long lastNotificationTime = 0L;

    public void send(String message, int color) {
        Notification notification = new Notification(message, color);
        notification.setAnimating(true);
        this.notifications.add(0, (Object)notification);
        if (this.notifications.size() > 5) {
            this.notifications.remove(this.notifications.size() - 1);
        }
        this.lastNotificationTime = System.currentTimeMillis();
    }

    /*
     * Exception decompiling
     */
    @Override
    @Subscribe
    public void onRender2D(Render2DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static class Notification {
        private final String message;
        private final int color;
        private final long creationTime;
        private long fadeStartTime = -1L;
        private final long durationMillis;
        private boolean animating = false;

        public Notification(String message, int color) {
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
}
