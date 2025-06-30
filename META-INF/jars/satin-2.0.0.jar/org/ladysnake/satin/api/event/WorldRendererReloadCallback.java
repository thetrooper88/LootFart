package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_761;

@FunctionalInterface
public interface WorldRendererReloadCallback {
    public static final Event<WorldRendererReloadCallback> EVENT = EventFactory.createArrayBacked(WorldRendererReloadCallback.class, listeners -> renderer -> {
        for (WorldRendererReloadCallback event : listeners) {
            event.onRendererReload(renderer);
        }
    });

    public void onRendererReload(class_761 var1);
}
