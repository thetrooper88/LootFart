package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_310.class})
public interface MinecraftClientAccessor {
    @Accessor(value="itemUseCooldown")
    public void hookSetItemUseCooldown(int var1);

    @Accessor(value="itemUseCooldown")
    public int hookGetItemUseCooldown();

    @Accessor(value="attackCooldown")
    public void hookSetAttackCooldown(int var1);

    @Invoker(value="doItemUse")
    public void hookDoItemUse();

    @Accessor(value="session")
    @Final
    @Mutable
    public void setSession(class_320 var1);

    @Accessor(value="world")
    public void hookSetWorld(class_638 var1);

    @Accessor(value="disconnecting")
    public void hookSetDisconnecting(boolean var1);
}
