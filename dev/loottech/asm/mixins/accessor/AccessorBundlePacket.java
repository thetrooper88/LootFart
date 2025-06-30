package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2596;
import net.minecraft.class_8038;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_8038.class})
public interface AccessorBundlePacket {
    @Accessor(value="packets")
    @Mutable
    public void setIterable(Iterable<class_2596<?>> var1);
}
