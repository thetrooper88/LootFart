package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.shader.ShaderManager;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.PerspectiveEvent;
import dev.loottech.client.events.Render3DEvent;
import dev.loottech.client.modules.visuals.NoRender;
import dev.loottech.client.modules.visuals.Shaders;
import net.minecraft.class_279;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import net.minecraft.class_7833;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_761.class})
public class WorldRendererMixin {
    @Inject(method={"render"}, at={@At(value="RETURN")}, cancellable=true)
    private void render(class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local class_4587 stack) {
        stack.method_22903();
        stack.method_22907(class_7833.field_40714.rotationDegrees(IMinecraft.mc.field_1773.method_19418().method_19329()));
        stack.method_22907(class_7833.field_40716.rotationDegrees(IMinecraft.mc.field_1773.method_19418().method_19330() + 180.0f));
        class_310.method_1551().method_16011().method_15396("oyvey-render-3d");
        RenderSystem.clear((int)256, (boolean)class_310.field_1703);
        Render3DEvent event = new Render3DEvent(tickCounter.method_60637(true), stack);
        Managers.EVENT.call(event);
        stack.method_22909();
        class_310.method_1551().method_16011().method_15407();
        IMinecraft.mc.method_22940().method_23000().method_22993();
    }

    @Inject(method={"renderWeather"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderWeather(class_765 manager, float f, double d, double e, double g, CallbackInfo ci) {
        if (Managers.MODULE.getInstance(NoRender.class).weather.getValue()) {
            ci.cancel();
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal=0))
    private void replaceShaderHook(class_279 instance, float tickDelta) {
        if (IMinecraft.mc.field_1687 == null || IMinecraft.mc.field_1724 == null) {
            return;
        }
        ShaderManager.Shader shaders = Managers.MODULE.getInstance(Shaders.class).getMode();
        if (Managers.MODULE.getInstance(Shaders.class).isEnabled() && IMinecraft.mc.field_1687 != null) {
            if (Managers.SHADER.fullNullCheck()) {
                return;
            }
            Managers.SHADER.setupShader(shaders, Managers.SHADER.getShaderOutline(ShaderManager.Shader.Default));
        } else {
            instance.method_1258(tickDelta);
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;isThirdPerson()Z"))
    public boolean hookRender(class_4184 instance) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent(instance);
        Managers.EVENT.call(perspectiveEvent);
        if (perspectiveEvent.isCanceled()) {
            return true;
        }
        return instance.method_19333();
    }
}
