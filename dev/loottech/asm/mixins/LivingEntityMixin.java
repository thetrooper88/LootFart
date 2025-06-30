package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.JumpRotationEvent;
import dev.loottech.client.events.SwingSpeedEvent;
import dev.loottech.client.modules.movement.ElytraFlight;
import dev.loottech.client.modules.movement.NoJumpDelay;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_4095;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1309.class})
public abstract class LivingEntityMixin
extends class_1297
implements IMinecraft {
    @Shadow
    private int field_6228;
    NoJumpDelay noJumpDelay = Managers.MODULE.getInstance(NoJumpDelay.class);
    ElytraFlight efly = Managers.MODULE.getInstance(ElytraFlight.class);

    public LivingEntityMixin(class_1299<?> type, class_1937 world) {
        super(type, world);
    }

    @Shadow
    public abstract class_4095<?> method_18868();

    @Inject(at={@At(value="HEAD")}, method={"Lnet/minecraft/entity/LivingEntity;tickMovement()V"})
    private void tickMovement(CallbackInfo ci) {
        if (LivingEntityMixin.mc.field_1724 != null && LivingEntityMixin.mc.field_1724.method_18868().equals(this.method_18868()) && this.efly != null && this.efly.enabled() || this.noJumpDelay.isEnabled()) {
            this.field_6228 = 0;
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"}, cancellable=true)
    private void isFallFlying(CallbackInfoReturnable<Boolean> cir) {
        if (LivingEntityMixin.mc.field_1724 != null && LivingEntityMixin.mc.field_1724.method_18868().equals(this.method_18868()) && this.efly != null && this.efly.enabled()) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"getHandSwingDuration"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookGetHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        SwingSpeedEvent swingSpeedEvent = new SwingSpeedEvent();
        Managers.EVENT.call(swingSpeedEvent);
        if (swingSpeedEvent.isCanceled()) {
            cir.setReturnValue((Object)swingSpeedEvent.getSwingSpeed());
        }
    }

    @Inject(method={"isClimbing"}, at={@At(value="HEAD")}, cancellable=true)
    public void isClimbing(CallbackInfoReturnable<Boolean> cir) {
        if (Managers.MODULE.isModuleEnabled("NoClimb")) {
            cir.setReturnValue((Object)false);
        }
    }

    @ModifyExpressionValue(method={"jump"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getYaw()F")})
    private float hookJump$getYaw(float original) {
        if (this != LivingEntityMixin.mc.field_1724) {
            return original;
        }
        JumpRotationEvent jumpRotationEvent = new JumpRotationEvent(original);
        Managers.EVENT.call(jumpRotationEvent);
        if (jumpRotationEvent.isCanceled()) {
            return jumpRotationEvent.getYaw();
        }
        return original;
    }
}
