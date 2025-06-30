package org.ladysnake.satin.mixin.client.event;

import net.minecraft.class_1041;
import net.minecraft.class_310;
import org.ladysnake.satin.api.event.ResolutionChangeCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_310.class})
public abstract class MinecraftClientMixin {
    @Final
    @Shadow
    private class_1041 field_1704;

    @Inject(method={"onResolutionChanged"}, at={@At(value="RETURN")})
    private void hookResolutionChanged(CallbackInfo info) {
        int width = this.field_1704.method_4489();
        int height = this.field_1704.method_4506();
        ((ResolutionChangeCallback)ResolutionChangeCallback.EVENT.invoker()).onResolutionChanged(width, height);
    }
}
