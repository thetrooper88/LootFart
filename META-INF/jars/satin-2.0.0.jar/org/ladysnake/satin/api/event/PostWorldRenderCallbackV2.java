package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.ladysnake.satin.api.event.PostWorldRenderCallback;

@FunctionalInterface
public interface PostWorldRenderCallbackV2 {
    public static final Event<PostWorldRenderCallbackV2> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallbackV2.class, listeners -> (posingStack, camera, tickDelta) -> {
        ((PostWorldRenderCallback)PostWorldRenderCallback.EVENT.invoker()).onWorldRendered(camera, tickDelta);
        for (PostWorldRenderCallbackV2 handler : listeners) {
            handler.onWorldRendered(posingStack, camera, tickDelta);
        }
    });

    public void onWorldRendered(class_4587 var1, class_4184 var2, float var3);
}
