package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.ColorUtils;
import dev.loottech.client.events.AddEntityEvent;
import dev.loottech.client.events.PlaySoundEvent;
import dev.loottech.client.events.RemoveEntityEvent;
import dev.loottech.client.modules.visuals.SkyBox;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_638.class})
public abstract class ClientWorldMixin {
    @Shadow
    @Nullable
    public abstract class_1297 method_8469(int var1);

    @Inject(method={"getSkyColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> info) {
        SkyBox skyBox = Managers.MODULE.getInstance(SkyBox.class);
        if (skyBox.isEnabled() && skyBox.sky.getValue()) {
            info.setReturnValue((Object)ColorUtils.getVec3d(skyBox.skyColor.getValue()));
        }
    }

    @Inject(method={"addEntity"}, at={@At(value="HEAD")})
    private void hookAddEntity(class_1297 entity, CallbackInfo ci) {
        AddEntityEvent addEntityEvent = new AddEntityEvent(entity);
        Managers.EVENT.call(addEntityEvent);
    }

    @Inject(method={"removeEntity"}, at={@At(value="HEAD")})
    private void hookRemoveEntity(int entityId, class_1297.class_5529 removalReason, CallbackInfo ci) {
        class_1297 entity = this.method_8469(entityId);
        RemoveEntityEvent removeEntityEvent = new RemoveEntityEvent(entity);
        Managers.EVENT.call(removeEntityEvent);
    }

    @Inject(method={"playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookPlaySound(double x, double y, double z, class_3414 event, class_3419 category, float volume, float pitch, boolean useDistance, long seed, CallbackInfo ci) {
        PlaySoundEvent playSoundEvent = new PlaySoundEvent(new class_243(x, y, z), event, category);
        Managers.EVENT.call(playSoundEvent);
        if (playSoundEvent.isCanceled()) {
            ci.cancel();
        }
    }
}
