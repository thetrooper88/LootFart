package org.ladysnake.satin.mixin.client.gl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_276;
import org.ladysnake.satin.api.experimental.ReadableDepthFramebuffer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_276.class})
public abstract class DepthGlFramebufferMixin
implements ReadableDepthFramebuffer {
    @Shadow
    @Final
    public boolean field_1478;
    @Shadow
    public int field_1482;
    @Shadow
    public int field_1481;
    private int satin$stillDepthTexture = -1;

    @Shadow
    public abstract void method_1235(boolean var1);

    @Inject(method={"initFbo"}, at={@At(value="FIELD", opcode=181, target="Lnet/minecraft/client/gl/Framebuffer;depthAttachment:I", shift=At.Shift.AFTER)})
    private void initFbo(int width, int height, boolean flushErrors, CallbackInfo ci) {
        if (this.field_1478) {
            this.satin$stillDepthTexture = this.satin$setupDepthTexture();
        }
    }

    @Unique
    private int satin$setupDepthTexture() {
        int shadowMap = GL11.glGenTextures();
        RenderSystem.bindTexture((int)shadowMap);
        RenderSystem.texParameter((int)3553, (int)10240, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10241, (int)9729);
        RenderSystem.texParameter((int)3553, (int)10242, (int)33071);
        RenderSystem.texParameter((int)3553, (int)10243, (int)33071);
        GlStateManager._texImage2D((int)3553, (int)0, (int)33190, (int)this.field_1482, (int)this.field_1481, (int)0, (int)6402, (int)5121, null);
        return shadowMap;
    }

    @Inject(method={"delete"}, at={@At(value="FIELD", opcode=180, target="Lnet/minecraft/client/gl/Framebuffer;depthAttachment:I")})
    private void delete(CallbackInfo ci) {
        if (this.satin$stillDepthTexture > -1) {
            TextureUtil.releaseTextureId((int)this.satin$stillDepthTexture);
            this.satin$stillDepthTexture = -1;
        }
    }

    @Override
    public int getStillDepthMap() {
        return this.satin$stillDepthTexture;
    }

    @Override
    public void freezeDepthMap() {
        if (this.field_1478) {
            this.method_1235(false);
            RenderSystem.bindTexture((int)this.satin$stillDepthTexture);
            GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)this.field_1482, (int)this.field_1481);
        }
    }
}
