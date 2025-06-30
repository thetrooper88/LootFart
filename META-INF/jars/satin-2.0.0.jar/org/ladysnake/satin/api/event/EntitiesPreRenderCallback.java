package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4604;

@FunctionalInterface
public interface EntitiesPreRenderCallback {
    public static final Event<EntitiesPreRenderCallback> EVENT = EventFactory.createArrayBacked(EntitiesPreRenderCallback.class, listeners -> (camera, frustum, tickDelta) -> {
        for (EntitiesPreRenderCallback handler : listeners) {
            handler.beforeEntitiesRender(camera, frustum, tickDelta);
        }
    });

    public void beforeEntitiesRender(class_4184 var1, class_4604 var2, float var3);
}
