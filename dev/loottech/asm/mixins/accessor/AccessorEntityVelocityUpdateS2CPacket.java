package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2743.class})
public interface AccessorEntityVelocityUpdateS2CPacket {
    @Accessor(value="velocityX")
    @Mutable
    public void setVelocityX(int var1);

    @Accessor(value="velocityY")
    @Mutable
    public void setVelocityY(int var1);

    @Accessor(value="velocityZ")
    @Mutable
    public void setVelocityZ(int var1);
}
