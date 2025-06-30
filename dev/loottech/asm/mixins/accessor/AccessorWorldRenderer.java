package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_4604;
import net.minecraft.class_761;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_761.class})
public interface AccessorWorldRenderer {
    @Accessor(value="frustum")
    @NotNull
    public class_4604 getFrustum();
}
