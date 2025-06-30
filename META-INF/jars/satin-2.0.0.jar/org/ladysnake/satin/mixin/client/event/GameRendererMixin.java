package org.ladysnake.satin.mixin.client.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_5912;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.ladysnake.satin.api.event.PickEntityShaderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_757.class})
public abstract class GameRendererMixin {
    @Shadow
    @Nullable
    class_279 field_4024;

    @Shadow
    protected abstract void method_3168(class_2960 var1);

    @Inject(at={@At(value="INVOKE", target="Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift=At.Shift.AFTER)}, method={"render"})
    private void hookShaderRender(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
        ((ShaderEffectRenderCallback)ShaderEffectRenderCallback.EVENT.invoker()).renderShaderEffects(tickCounter.method_60637(tick));
    }

    @Inject(method={"onCameraEntitySet"}, at={@At(value="RETURN")}, require=0)
    private void useCustomEntityShader(@Nullable class_1297 entity, CallbackInfo info) {
        if (this.field_4024 == null) {
            ((PickEntityShaderCallback)PickEntityShaderCallback.EVENT.invoker()).pickEntityShader(entity, (Consumer<class_2960>)((Consumer)loc -> this.method_3168((class_2960)loc)), (Supplier<class_279>)((Supplier)() -> this.field_4024));
        }
    }

    @Inject(method={"loadPrograms"}, at={@At(value="RETURN")})
    private void loadSatinPrograms(class_5912 factory, CallbackInfo ci) {
        ReloadableShaderEffectManager.INSTANCE.reload(factory);
    }
}
