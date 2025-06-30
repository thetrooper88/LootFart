package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_636.class})
public interface AccessorClientPlayerInteractionManager {
    @Invoker(value="syncSelectedSlot")
    public void hookSyncSelectedSlot();

    @Invoker(value="interactBlockInternal")
    public class_1269 hookInteractBlockInternal(class_746 var1, class_1268 var2, class_3965 var3);

    @Accessor(value="currentBreakingProgress")
    public float hookGetCurrentBreakingProgress();

    @Accessor(value="currentBreakingProgress")
    public void hookSetCurrentBreakingProgress(float var1);
}
