package org.ladysnake.satin.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.ladysnake.satin.api.event.PostWorldRenderCallbackV2;

@FunctionalInterface
public interface PostWorldRenderCallbackV3 {
    public static final Event<PostWorldRenderCallbackV3> EVENT = EventFactory.createArrayBacked(PostWorldRenderCallbackV3.class, listeners -> (matrices, projectionMat, modelViewMath, camera, tickDelta) -> {
        ((PostWorldRenderCallbackV2)PostWorldRenderCallbackV2.EVENT.invoker()).onWorldRendered(matrices, camera, tickDelta);
        for (PostWorldRenderCallbackV3 handler : listeners) {
            handler.onWorldRendered(matrices, projectionMat, modelViewMath, camera, tickDelta);
        }
    });

    public void onWorldRendered(class_4587 var1, Matrix4f var2, Matrix4f var3, class_4184 var4, float var5);
}
