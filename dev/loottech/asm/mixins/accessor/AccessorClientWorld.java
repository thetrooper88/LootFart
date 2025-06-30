package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import net.minecraft.class_7202;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_638.class})
public interface AccessorClientWorld {
    @Invoker(value="playSound")
    public void hookPlaySound(double var1, double var3, double var5, class_3414 var7, class_3419 var8, float var9, float var10, boolean var11, long var12);

    @Invoker(value="getPendingUpdateManager")
    public class_7202 hookGetPendingUpdateManager();

    @Invoker(value="playSound")
    public void hook$playSound(double var1, double var3, double var5, class_3414 var7, class_3419 var8, float var9, float var10, boolean var11, long var12);
}
