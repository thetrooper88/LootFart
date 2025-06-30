package org.ladysnake.satin.mixin.client.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4604;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.ladysnake.satin.api.event.EntitiesPostRenderCallback;
import org.ladysnake.satin.api.event.EntitiesPreRenderCallback;
import org.ladysnake.satin.api.event.PostWorldRenderCallbackV3;
import org.ladysnake.satin.api.experimental.ReadableDepthFramebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_761.class})
public abstract class WorldRendererMixin {
    @Unique
    private class_4604 frustum;

    @ModifyVariable(method={"render"}, at=@At(value="CONSTANT", args={"stringValue=entities"}, ordinal=0, shift=At.Shift.BEFORE))
    private class_4604 captureFrustum(class_4604 frustum) {
        this.frustum = frustum;
        return frustum;
    }

    @Inject(method={"render"}, at={@At(value="CONSTANT", args={"stringValue=entities"}, ordinal=0)})
    private void firePreRenderEntities(class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        ((EntitiesPreRenderCallback)EntitiesPreRenderCallback.EVENT.invoker()).beforeEntitiesRender(camera, this.frustum, tickCounter.method_60637(false));
    }

    @Inject(method={"render"}, at={@At(value="CONSTANT", args={"stringValue=blockentities"}, ordinal=0)})
    private void firePostRenderEntities(class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        ((EntitiesPostRenderCallback)EntitiesPostRenderCallback.EVENT.invoker()).onEntitiesRendered(camera, this.frustum, tickCounter.method_60637(false));
    }

    @Inject(method={"render"}, slice={@Slice(from=@At(value="FIELD:LAST", opcode=180, target="Lnet/minecraft/client/render/WorldRenderer;transparencyPostProcessor:Lnet/minecraft/client/gl/PostEffectProcessor;"))}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V"), @At(value="INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V", ordinal=1, shift=At.Shift.AFTER)})
    private void hookPostWorldRender(class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local class_4587 matrices) {
        ((ReadableDepthFramebuffer)class_310.method_1551().method_1522()).freezeDepthMap();
        ((PostWorldRenderCallbackV3)PostWorldRenderCallbackV3.EVENT.invoker()).onWorldRendered(matrices, matrix4f, matrix4f2, camera, tickCounter.method_60637(true));
    }
}
