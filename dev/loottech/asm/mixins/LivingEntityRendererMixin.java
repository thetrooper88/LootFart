package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.RenderEntityEvent;
import dev.loottech.client.events.RenderThroughWallsEvent;
import dev.loottech.client.modules.visuals.Chams;
import dev.loottech.client.modules.visuals.Freecam;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1921;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_922.class})
public abstract class LivingEntityRendererMixin<T extends class_1309, M extends class_583<T>> {
    @Shadow
    protected M field_4737;
    @Unique
    private Chams chams = Managers.MODULE.getInstance(Chams.class);
    @Shadow
    @Final
    protected List<class_3887<T, M>> field_4738;

    @Shadow
    protected abstract class_1921 method_24302(T var1, boolean var2, boolean var3, boolean var4);

    @ModifyExpressionValue(method={"hasLabel(Lnet/minecraft/entity/LivingEntity;)Z"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;")})
    private class_1297 hasLabelGetCameraEntityProxy(class_1297 cameraEntity) {
        return Managers.MODULE.isModuleEnabled(Freecam.class) ? null : cameraEntity;
    }

    @Inject(method={"render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookRender$1(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        RenderThroughWallsEvent renderThroughWallsEvent = new RenderThroughWallsEvent((class_1309)livingEntity);
        Managers.EVENT.call(renderThroughWallsEvent);
        if (renderThroughWallsEvent.isCanceled()) {
            RenderSystem.disableDepthTest();
        }
    }

    @Inject(method={"render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="TAIL")})
    private void hookRender$2(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        RenderThroughWallsEvent renderThroughWallsEvent = new RenderThroughWallsEvent((class_1309)livingEntity);
        Managers.EVENT.call(renderThroughWallsEvent);
        if (renderThroughWallsEvent.isCanceled()) {
            RenderSystem.enableDepthTest();
        }
    }

    @Inject(method={"render*"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookRender(class_1309 livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        RenderEntityEvent<T> renderEntityEvent = new RenderEntityEvent<T>((class_922)this, livingEntity, f, g, matrixStack, vertexConsumerProvider, i, (class_583)this.field_4737, this.method_24302(livingEntity, this.chams.isEnabled() && this.chams.wallsConfig.getValue(), true, true), this.field_4738);
        Managers.EVENT.call(renderEntityEvent);
        if (renderEntityEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
