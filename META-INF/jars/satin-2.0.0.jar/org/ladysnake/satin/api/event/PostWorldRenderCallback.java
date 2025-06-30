package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;

@FunctionalInterface
public interface PostWorldRenderCallback {
    public static final Event<PostWorldRenderCallback> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallback.class, listeners -> (camera, tickDelta) -> {
        for (PostWorldRenderCallback handler : listeners) {
            handler.onWorldRendered(camera, tickDelta);
        }
    });

    public void onWorldRendered(class_4184 var1, float var2);
}
