package dev.loottech.asm.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.asm.ducks.IClientPlayerEntity;
import dev.loottech.client.events.MovementPacketsEvent;
import dev.loottech.client.events.PlayerTickEvent;
import dev.loottech.client.events.PostPlayerUpdateEvent;
import dev.loottech.client.events.PrePlayerUpdateEvent;
import dev.loottech.client.events.PushEvent;
import dev.loottech.client.events.SprintCancelEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.movement.NoSlow;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_3532;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_746.class})
public abstract class ClientPlayerEntityMixin
extends class_742
implements IMinecraft,
IClientPlayerEntity {
    @Shadow
    private double field_3926;
    @Shadow
    private double field_3940;
    @Shadow
    private double field_3924;
    @Shadow
    private float field_3941;
    @Shadow
    private float field_3925;
    @Shadow
    private boolean field_3920;
    @Shadow
    private boolean field_3936;
    @Shadow
    @Final
    public class_634 field_3944;
    @Shadow
    private int field_3923;
    @Shadow
    private boolean field_3927;
    @Shadow
    public class_744 field_3913;

    @Shadow
    protected abstract void method_46742();

    @Shadow
    public abstract boolean method_5715();

    @Shadow
    protected abstract boolean method_3134();

    public ClientPlayerEntityMixin(class_638 world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectSendMovementPacketsPre(CallbackInfo ci) {
        TickEvent event = new TickEvent();
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift=At.Shift.BEFORE, ordinal=0)})
    private void hookTickPre(CallbackInfo ci) {
        PlayerTickEvent playerTickEvent = new PlayerTickEvent();
        Managers.EVENT.call(playerTickEvent);
    }

    @Redirect(method={"tickMovement"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V", ordinal=3))
    private void hookSetSprinting(class_746 instance, boolean b) {
        SprintCancelEvent sprintEvent = new SprintCancelEvent();
        Managers.EVENT.call(sprintEvent);
        if (sprintEvent.isCanceled()) {
            instance.method_5728(true);
        } else {
            instance.method_5728(b);
        }
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookSendMovementPackets(CallbackInfo ci) {
        PrePlayerUpdateEvent playerUpdateEvent = new PrePlayerUpdateEvent();
        Managers.EVENT.call(playerUpdateEvent);
        MovementPacketsEvent movementPacketsEvent = new MovementPacketsEvent(ClientPlayerEntityMixin.mc.field_1724.method_23317(), ClientPlayerEntityMixin.mc.field_1724.method_23318(), ClientPlayerEntityMixin.mc.field_1724.method_23321(), ClientPlayerEntityMixin.mc.field_1724.method_36454(), ClientPlayerEntityMixin.mc.field_1724.method_36455(), ClientPlayerEntityMixin.mc.field_1724.method_24828());
        Managers.EVENT.call(movementPacketsEvent);
        double x = movementPacketsEvent.getX();
        double y = movementPacketsEvent.getY();
        double z = movementPacketsEvent.getZ();
        float yaw = movementPacketsEvent.getRotationYaw();
        float pitch = movementPacketsEvent.getRotationPitch();
        boolean ground = movementPacketsEvent.isOnGround();
        if (movementPacketsEvent.isCanceled()) {
            ci.cancel();
            this.method_46742();
            boolean bl = this.method_5715();
            if (bl != this.field_3936) {
                class_2848.class_2849 mode = bl ? class_2848.class_2849.field_12979 : class_2848.class_2849.field_12984;
                this.field_3944.method_52787((class_2596)new class_2848((class_1297)this, mode));
                this.field_3936 = bl;
            }
            if (this.method_3134()) {
                boolean bl3;
                double d = x - this.field_3926;
                double e = y - this.field_3940;
                double f = z - this.field_3924;
                double g = yaw - this.field_3941;
                double h = pitch - this.field_3925;
                ++this.field_3923;
                boolean bl2 = class_3532.method_41190((double)d, (double)e, (double)f) > class_3532.method_33723((double)2.0E-4) || this.field_3923 >= 20;
                boolean bl4 = bl3 = g != 0.0 || h != 0.0;
                if (this.method_5765()) {
                    class_243 vec3d = this.method_18798();
                    this.field_3944.method_52787((class_2596)new class_2828.class_2830(vec3d.field_1352, -999.0, vec3d.field_1350, this.method_36454(), this.method_36455(), ground));
                    bl2 = false;
                } else if (bl2 && bl3) {
                    this.field_3944.method_52787((class_2596)new class_2828.class_2830(x, y, z, yaw, pitch, ground));
                } else if (bl2) {
                    this.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, ground));
                } else if (bl3) {
                    this.field_3944.method_52787((class_2596)new class_2828.class_2831(yaw, pitch, ground));
                } else if (this.field_3920 != this.method_24828()) {
                    this.field_3944.method_52787((class_2596)new class_2828.class_5911(ground));
                }
                if (bl2) {
                    this.field_3926 = x;
                    this.field_3940 = y;
                    this.field_3924 = z;
                    this.field_3923 = 0;
                }
                if (bl3) {
                    this.field_3941 = yaw;
                    this.field_3925 = pitch;
                }
                this.field_3920 = ground;
                this.field_3927 = (Boolean)ClientPlayerEntityMixin.mc.field_1690.method_42423().method_41753();
            }
        }
        PostPlayerUpdateEvent postPlayerUpdateEvent = new PostPlayerUpdateEvent();
        Managers.EVENT.call(postPlayerUpdateEvent);
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushOutOfBlocks(double x, double d, CallbackInfo ci) {
        PushEvent event = new PushEvent();
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z")})
    private boolean tickMovement$isUsingItem(boolean original) {
        if (Managers.MODULE.getInstance(NoSlow.class).isEnabled() && (Managers.MODULE.getInstance(NoSlow.class).items.getValue().equals((Object)NoSlow.ItemModes.Normal) || Managers.MODULE.getInstance(NoSlow.class).items.getValue().equals((Object)NoSlow.ItemModes.Grim))) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(method={"canStartSprinting"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z")})
    private boolean canStartSprinting$isUsingItem(boolean original) {
        if (Managers.MODULE.isModuleEnabled("NoSlow")) {
            return false;
        }
        return original;
    }

    @Override
    @Unique
    public float loottech$getLastSpoofedYaw() {
        return this.field_3941;
    }

    @Override
    @Unique
    public float loottech$getLastSpoofedPitch() {
        return this.field_3925;
    }
}
