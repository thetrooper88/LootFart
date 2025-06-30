package dev.loottech.asm.mixins.accessor;

import java.util.List;
import net.minecraft.class_279;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_279.class})
public interface AccessiblePassesShaderEffect {
    @Accessor
    public List<class_283> getPasses();
}
