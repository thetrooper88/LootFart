package org.ladysnake.satin.mixin.client.gl;

import net.minecraft.class_276;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.satin.impl.CustomFormatFramebuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_276.class})
public abstract class CustomFormatFramebufferMixin {
    @Unique
    private int satin$format = 32856;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void satin$setFormat(boolean useDepth, CallbackInfo ci) {
        @Nullable CustomFormatFramebuffers.TextureFormat format = CustomFormatFramebuffers.getCustomFormat();
        if (format != null) {
            this.satin$format = format.value;
            CustomFormatFramebuffers.clearCustomFormat();
        }
    }

    @ModifyArg(method={"initFbo"}, slice=@Slice(from=@At(value="INVOKE", target="Lnet/minecraft/client/gl/Framebuffer;setTexFilter(IZ)V"), to=@At(value="INVOKE", target="Lcom/mojang/blaze3d/platform/GlStateManager;_glBindFramebuffer(II)V")), at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"), index=2)
    private int satin$modifyInternalFormat(int internalFormat) {
        return this.satin$format;
    }
}
