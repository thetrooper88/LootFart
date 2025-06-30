package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.RenderPlayerEvent;
import net.minecraft.class_1007;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1007.class})
public class PlayerEntityRendererMixin {
    @Unique
    private float yaw;
    @Unique
    private float prevYaw;
    @Unique
    private float bodyYaw;
    @Unique
    private float prevBodyYaw;
    @Unique
    private float headYaw;
    @Unique
    private float prevHeadYaw;
    @Unique
    private float pitch;
    @Unique
    private float prevPitch;
    @Unique
    private boolean prevSneaking;
    @Unique
    private boolean prevFallFlying;

    @Inject(method={"render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render /VertexConsumerProvider;I)V"}, at={@At(value="HEAD")})
    private void onRenderHead(class_742 abstractClientPlayerEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        RenderPlayerEvent renderPlayerEvent = new RenderPlayerEvent(abstractClientPlayerEntity);
        Managers.EVENT.call(renderPlayerEvent);
        this.yaw = abstractClientPlayerEntity.method_36454();
        this.prevYaw = abstractClientPlayerEntity.field_5982;
        this.bodyYaw = abstractClientPlayerEntity.field_6283;
        this.prevBodyYaw = abstractClientPlayerEntity.field_6220;
        this.headYaw = abstractClientPlayerEntity.field_6241;
        this.prevHeadYaw = abstractClientPlayerEntity.field_6259;
        this.pitch = abstractClientPlayerEntity.method_36455();
        this.prevPitch = abstractClientPlayerEntity.field_6004;
        this.prevSneaking = abstractClientPlayerEntity.method_5715();
        if (renderPlayerEvent.isCanceled()) {
            abstractClientPlayerEntity.method_36456(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.field_5982 = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.method_5636(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.field_6220 = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.method_5847(renderPlayerEvent.getYaw());
            abstractClientPlayerEntity.field_6259 = renderPlayerEvent.getYaw();
            abstractClientPlayerEntity.method_36457(renderPlayerEvent.getPitch());
            abstractClientPlayerEntity.field_6004 = renderPlayerEvent.getPitch();
        }
    }

    @Inject(method={"render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="TAIL")})
    private void onRenderTail(class_742 abstractClientPlayerEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        abstractClientPlayerEntity.method_36456(this.yaw);
        abstractClientPlayerEntity.field_5982 = this.prevYaw;
        abstractClientPlayerEntity.method_5636(this.bodyYaw);
        abstractClientPlayerEntity.field_6220 = this.prevBodyYaw;
        abstractClientPlayerEntity.method_5847(this.headYaw);
        abstractClientPlayerEntity.field_6259 = this.prevHeadYaw;
        abstractClientPlayerEntity.method_36457(this.pitch);
        abstractClientPlayerEntity.field_6004 = this.prevPitch;
    }
}
