package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.EntityCameraPositionEvent;
import dev.loottech.client.events.EntityRotationVectorEvent;
import dev.loottech.client.events.PushEvent;
import dev.loottech.client.events.SlowMovementEvent;
import dev.loottech.client.events.UpdateVelocityEvent;
import dev.loottech.client.modules.movement.ElytraFlight;
import dev.loottech.client.modules.visuals.Shaders;
import java.util.UUID;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1297.class})
public abstract class EntityMixin {
    @Shadow
    protected class_243 field_17046;
    @Shadow
    public float field_6017;
    @Shadow
    protected UUID field_6021;
    ElytraFlight efly = Managers.MODULE.getInstance(ElytraFlight.class);
    @Shadow
    private int field_5986;

    @Shadow
    private static class_243 method_18795(class_243 movementInput, float speed, float yaw) {
        return null;
    }

    @Shadow
    public abstract void method_5697(class_1297 var1);

    @Inject(method={"isGlowing"}, at={@At(value="HEAD")}, cancellable=true)
    public void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
        Shaders shaders = Managers.MODULE.getInstance(Shaders.class);
        if (shaders.isEnabled()) {
            cir.setReturnValue((Object)shaders.shouldRender((class_1297)this));
        }
    }

    @Inject(method={"updateVelocity"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookUpdateVelocity(float speed, class_243 movementInput, CallbackInfo ci) {
        if (this == Util.mc.field_1724) {
            UpdateVelocityEvent updateVelocityEvent = new UpdateVelocityEvent(movementInput, speed, Util.mc.field_1724.method_36454(), EntityMixin.method_18795(movementInput, speed, Util.mc.field_1724.method_36454()));
            Managers.EVENT.call(updateVelocityEvent);
            if (updateVelocityEvent.isCanceled()) {
                ci.cancel();
                Util.mc.field_1724.method_18799(Util.mc.field_1724.method_18798().method_1019(updateVelocityEvent.getVelocity()));
            }
        }
    }

    @Inject(at={@At(value="RETURN")}, method={"adjustMovementForCollisions"}, cancellable=true)
    private void adjustMovementForCollisions(class_243 movement, CallbackInfoReturnable<class_243> cir) {
        class_243 returnValue;
        if (Util.mc.field_1724 != null && this.field_6021 == Util.mc.field_1724.method_5667() && this.efly != null && this.efly.enabled() && Managers.MODULE.getInstance(ElytraFlight.class).fakeHeadBlock.getValue() && Math.abs((double)((returnValue = (class_243)cir.getReturnValue()).method_10214() - 0.42)) < 0.1) {
            cir.setReturnValue((Object)new class_243(returnValue.method_10216(), 0.2, returnValue.method_10215()));
        }
    }

    @Inject(method={"getRotationVec"}, at={@At(value="RETURN")}, cancellable=true)
    public void hookGetCameraPosVec(float tickDelta, CallbackInfoReturnable<class_243> info) {
        EntityRotationVectorEvent event = new EntityRotationVectorEvent(tickDelta, (class_1297)this, (class_243)info.getReturnValue());
        Managers.EVENT.call(event);
        info.setReturnValue((Object)event.getPosition());
    }

    @Inject(method={"getCameraPosVec"}, at={@At(value="RETURN")}, cancellable=true)
    public void hookCameraPositionVec(float tickDelta, CallbackInfoReturnable<class_243> cir) {
        EntityCameraPositionEvent cameraPositionEvent = new EntityCameraPositionEvent((class_243)cir.getReturnValue(), (class_1297)this, tickDelta);
        Managers.EVENT.call(cameraPositionEvent);
        cir.setReturnValue((Object)cameraPositionEvent.getPosition());
    }

    @Inject(method={"slowMovement"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookSlowMovement(class_2680 state, class_243 multiplier, CallbackInfo ci) {
        if (this != Util.mc.field_1724) {
            return;
        }
        SlowMovementEvent slowMovementEvent = new SlowMovementEvent(state);
        Managers.EVENT.call(slowMovementEvent);
        if (slowMovementEvent.isCanceled()) {
            ci.cancel();
            this.field_6017 = 0.0f;
            this.field_17046 = class_243.field_1353;
        }
    }

    @Inject(method={"pushAwayFrom"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookPushAwayFrom(class_1297 e, CallbackInfo ci) {
        PushEvent event = new PushEvent();
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
