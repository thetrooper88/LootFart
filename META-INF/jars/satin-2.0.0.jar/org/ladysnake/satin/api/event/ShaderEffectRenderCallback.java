package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ShaderEffectRenderCallback {
    public static final Event<ShaderEffectRenderCallback> EVENT = EventFactory.createArrayBacked(ShaderEffectRenderCallback.class, listeners -> tickDelta -> {
        for (ShaderEffectRenderCallback handler : listeners) {
            handler.renderShaderEffects(tickDelta);
        }
    });

    public void renderShaderEffects(float var1);
}
