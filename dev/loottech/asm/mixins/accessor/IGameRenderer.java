package dev.loottech.asm.mixins.accessor;

import net.minecraft.class_4184;
import net.minecraft.class_757;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_757.class})
public interface IGameRenderer {
    @Invoker(value="renderHand")
    public void hookRenderHand(class_4184 var1, float var2, Matrix4f var3);
}
