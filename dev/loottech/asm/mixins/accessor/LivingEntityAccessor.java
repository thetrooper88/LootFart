package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_1309;
import net.minecraft.class_3611;
import net.minecraft.class_6862;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1309.class})
public interface LivingEntityAccessor {
    @Invoker(value="swimUpward")
    public void swimUpwards(class_6862<class_3611> var1);

    @Accessor(value="jumping")
    public boolean isJumping();

    @Accessor(value="jumpingCooldown")
    public int getJumpCooldown();

    @Accessor(value="jumpingCooldown")
    public void setJumpCooldown(int var1);
}
