package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4604;

@FunctionalInterface
public interface EntitiesPostRenderCallback {
    public static final Event<EntitiesPostRenderCallback> EVENT = EventFactory.createArrayBacked(EntitiesPostRenderCallback.class, listeners -> (camera, frustum, tickDelta) -> {
        for (EntitiesPostRenderCallback handler : listeners) {
            handler.onEntitiesRendered(camera, frustum, tickDelta);
        }
    });

    public void onEntitiesRendered(class_4184 var1, class_4604 var2, float var3);
}
