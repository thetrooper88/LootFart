package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.AmbientColorEvent;
import dev.loottech.client.modules.visuals.Fullbright;
import java.awt.Color;
import net.minecraft.class_1011;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_765.class})
public abstract class LightmapTextureManagerMixin {
    @Shadow
    @Final
    private class_1011 field_4133;

    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    private void update(Args args) {
        if (Managers.MODULE.isModuleEnabled(Fullbright.class)) {
            args.set(2, (Object)-1);
        }
    }

    @Inject(method={"update"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/texture/NativeImageBackedTexture;upload()V", shift=At.Shift.BEFORE)})
    private void hookUpdate(float delta, CallbackInfo ci) {
        AmbientColorEvent ambientColorEvent = new AmbientColorEvent();
        Managers.EVENT.call(ambientColorEvent);
        Color c = ambientColorEvent.getColor();
        if (ambientColorEvent.isCanceled()) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    this.field_4133.method_4305(i, j, 0xFF000000 | b << 16 | g << 8 | r);
                }
            }
        }
    }
}
