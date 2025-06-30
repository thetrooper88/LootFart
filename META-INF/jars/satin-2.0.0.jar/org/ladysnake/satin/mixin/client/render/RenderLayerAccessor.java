package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_1921;
import net.minecraft.class_293;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1921.class})
public interface RenderLayerAccessor {
    @Accessor
    public boolean isTranslucent();

    @Invoker(value="of")
    public static class_1921.class_4687 satin$of(String name, class_293 vertexFormat, class_293.class_5596 drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, class_1921.class_4688 phases) {
        throw new IllegalStateException("Mixin not transformed");
    }
}
