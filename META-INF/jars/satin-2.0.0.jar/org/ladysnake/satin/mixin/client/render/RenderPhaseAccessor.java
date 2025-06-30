package org.ladysnake.satin.mixin.client.render;

import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_4668.class})
public interface RenderPhaseAccessor {
    @Accessor
    public String getName();
}
