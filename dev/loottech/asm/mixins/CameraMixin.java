package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.CameraPositionEvent;
import dev.loottech.client.events.CameraRotationEvent;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_4184.class})
public abstract class CameraMixin {
    @Shadow
    private float field_47549;

    @Shadow
    protected abstract void method_19327(double var1, double var3, double var5);

    @Shadow
    protected abstract void method_19325(float var1, float var2);

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void hookUpdatePosition(class_4184 instance, double x, double y, double z) {
        CameraPositionEvent cameraPositionEvent = new CameraPositionEvent(x, y, z, this.field_47549);
        Managers.EVENT.call(cameraPositionEvent);
        this.method_19327(cameraPositionEvent.getX(), cameraPositionEvent.getY(), cameraPositionEvent.getZ());
    }

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void hookUpdateRotation(class_4184 instance, float yaw, float pitch) {
        CameraRotationEvent cameraRotationEvent = new CameraRotationEvent(yaw, pitch, this.field_47549);
        Managers.EVENT.call(cameraRotationEvent);
        this.method_19325(cameraRotationEvent.getYaw(), cameraRotationEvent.getPitch());
    }
}
