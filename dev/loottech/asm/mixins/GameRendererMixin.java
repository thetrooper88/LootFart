package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.LightmapInitEvent;
import dev.loottech.client.events.LightmapTickEvent;
import dev.loottech.client.events.LightmapUpdateEvent;
import dev.loottech.client.events.RenderWorldHandEvent;
import dev.loottech.client.events.ViewBobEvent;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_4587;
import net.minecraft.class_4599;
import net.minecraft.class_5912;
import net.minecraft.class_757;
import net.minecraft.class_759;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.ladysnake.satin.impl.ReloadableShaderEffectManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_757.class})
public class GameRendererMixin {
    @Inject(method={"showFloatingItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void showFloatingItemHook(class_1799 floatingItem, CallbackInfo info) {
        if (floatingItem.method_7909() == class_1802.field_8288 && Managers.MODULE.getInstance(NoRender.class).totemOverlay.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V", shift=At.Shift.AFTER)})
    public void postRender3dHook(class_9779 tickCounter, CallbackInfo ci) {
        if (Util.mc.field_1724 == null || Util.mc.field_1687 == null) {
            return;
        }
        Managers.SHADER.renderShaders();
    }

    @Inject(method={"loadPrograms"}, at={@At(value="RETURN")})
    private void loadSatinPrograms(class_5912 factory, CallbackInfo ci) {
        ReloadableShaderEffectManager.INSTANCE.reload(factory);
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void hookInit(class_310 client, class_759 heldItemRenderer, class_3300 resourceManager, class_4599 buffers, CallbackInfo ci) {
        LightmapInitEvent lightmapInitEvent = new LightmapInitEvent();
        Managers.EVENT.call(lightmapInitEvent);
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/LightmapTextureManager;tick()V")})
    private void hookTick(CallbackInfo ci) {
        LightmapTickEvent lightmapTickEvent = new LightmapTickEvent();
        Managers.EVENT.call(lightmapTickEvent);
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/LightmapTextureManager;update(F)V")})
    private void hookRenderWorld$3(class_9779 tickCounter, CallbackInfo ci) {
        LightmapUpdateEvent lightmapUpdateEvent = new LightmapUpdateEvent(tickCounter.method_60637(true));
        Managers.EVENT.call(lightmapUpdateEvent);
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V", shift=At.Shift.AFTER)})
    public void hookRenderWorld$2(class_9779 tickCounter, CallbackInfo ci, @Local(ordinal=1) Matrix4f matrix4f2, @Local(ordinal=1) float tickDelta, @Local class_4587 matrixStack) {
        RenderWorldHandEvent reloadShaderEvent = new RenderWorldHandEvent(matrixStack, tickDelta);
        Managers.EVENT.call(reloadShaderEvent);
    }

    @Inject(method={"bobView"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookBobView(class_4587 matrices, float tickDelta, CallbackInfo ci) {
        ViewBobEvent bobViewEvent = new ViewBobEvent(matrices, tickDelta);
        Managers.EVENT.call(bobViewEvent);
        if (bobViewEvent.isCanceled()) {
            ci.cancel();
            matrices.method_46416(0.0f, bobViewEvent.getY(), 0.0f);
        }
    }
}
