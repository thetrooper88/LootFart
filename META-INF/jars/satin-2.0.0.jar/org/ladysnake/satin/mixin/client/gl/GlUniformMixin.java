package org.ladysnake.satin.mixin.client.gl;

import net.minecraft.class_284;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_284.class})
public abstract class GlUniformMixin {
    @Inject(method={"upload"}, at={@At(value="JUMP", opcode=154, ordinal=0, shift=At.Shift.AFTER)}, cancellable=true)
    private void fixUploadEarlyReturn(CallbackInfo ci) {
        ci.cancel();
    }
}
