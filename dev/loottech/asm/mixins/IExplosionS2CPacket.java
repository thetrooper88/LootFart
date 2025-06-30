package dev.loottech.asm.mixins;

import net.minecraft.class_2664;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2664.class})
public interface IExplosionS2CPacket {
    @Mutable
    @Accessor(value="playerVelocityX")
    public void setX(float var1);

    @Mutable
    @Accessor(value="playerVelocityY")
    public void setY(float var1);

    @Mutable
    @Accessor(value="playerVelocityZ")
    public void setZ(float var1);
}
