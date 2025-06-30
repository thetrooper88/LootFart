package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_2664;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2664.class})
public interface AccessorExplosionS2CPacket {
    @Accessor(value="playerVelocityX")
    @Mutable
    public void setPlayerVelocityX(float var1);

    @Accessor(value="playerVelocityY")
    @Mutable
    public void setPlayerVelocityY(float var1);

    @Accessor(value="playerVelocityZ")
    @Mutable
    public void setPlayerVelocityZ(float var1);
}
