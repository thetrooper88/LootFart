package org.ladysnake.satin.api.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1297;
import net.minecraft.class_279;
import net.minecraft.class_2960;

public interface PickEntityShaderCallback {
    public static final Event<PickEntityShaderCallback> EVENT = EventFactory.createArrayBacked(PickEntityShaderCallback.class, listeners -> (entity, loadShaderFunc, appliedShaderGetter) -> {
        for (PickEntityShaderCallback handler : listeners) {
            handler.pickEntityShader(entity, (Consumer<class_2960>)loadShaderFunc, (Supplier<class_279>)appliedShaderGetter);
            if (appliedShaderGetter.get() != null) break;
        }
    });

    public void pickEntityShader(@Nullable class_1297 var1, Consumer<class_2960> var2, Supplier<class_279> var3);
}
