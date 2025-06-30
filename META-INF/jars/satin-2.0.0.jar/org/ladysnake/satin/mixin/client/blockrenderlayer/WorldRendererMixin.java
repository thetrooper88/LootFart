package org.ladysnake.satin.mixin.client.blockrenderlayer;

import net.minecraft.class_1921;
import net.minecraft.class_4184;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={class_761.class})
public abstract class WorldRendererMixin {
    @Shadow
    protected abstract void method_3251(class_1921 var1, double var2, double var4, double var6, Matrix4f var8, Matrix4f var9);

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal=2, shift=At.Shift.AFTER)}, slice={@Slice(from=@At(value="INVOKE_STRING", target="Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args={"ldc=terrain"}), to=@At(value="INVOKE", target="Lnet/minecraft/client/render/DimensionEffects;isDarkened()Z"))}, locals=LocalCapture.CAPTURE_FAILSOFT)
    private void renderCustom(class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        for (class_1921 layer : BlockRenderLayerRegistry.INSTANCE.getLayers()) {
            this.method_3251(layer, camera.method_19326().field_1352, camera.method_19326().field_1351, camera.method_19326().field_1350, matrix4f, matrix4f2);
        }
    }
}
