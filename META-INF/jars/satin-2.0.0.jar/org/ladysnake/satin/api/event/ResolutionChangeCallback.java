package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface ResolutionChangeCallback {
    public static final Event<ResolutionChangeCallback> EVENT = EventFactory.createArrayBacked(ResolutionChangeCallback.class, listeners -> (newWidth, newHeight) -> {
        for (ResolutionChangeCallback event : listeners) {
            event.onResolutionChanged(newWidth, newHeight);
        }
    });

    public void onResolutionChanged(int var1, int var2);
}
