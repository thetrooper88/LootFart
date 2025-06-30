package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2828.class})
public interface PlayerMoveC2SPacketAccessor {
    @Accessor(value="onGround")
    @Mutable
    public void setOnGround(boolean var1);
}
