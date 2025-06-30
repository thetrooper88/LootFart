package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2708;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2708.class})
public interface PlayerPositionLookS2CPacketAccessor {
    @Accessor(value="yaw")
    @Mutable
    public void setYaw(float var1);

    @Accessor(value="pitch")
    @Mutable
    public void setPitch(float var1);
}
