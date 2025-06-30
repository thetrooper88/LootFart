package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_4668;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_4668.class})
public interface AccessorRenderPhase {
    @Mutable
    @Accessor(value="beginAction")
    public void hookSetBeginAction(Runnable var1);

    @Mutable
    @Accessor(value="endAction")
    public void hookSetEndAction(Runnable var1);
}
