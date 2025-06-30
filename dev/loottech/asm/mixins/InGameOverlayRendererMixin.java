package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_4603.class})
public class InGameOverlayRendererMixin {
    @Inject(method={"renderInWallOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void renderInWallOverlayHook(class_1058 sprite, class_4587 matrices, CallbackInfo ci) {
        if (Managers.MODULE.getInstance(NoRender.class).blockOverlay.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderFireOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void renderFireOverlayHook(class_310 minecraftClient, class_4587 matrixStack, CallbackInfo ci) {
        if (Managers.MODULE.getInstance(NoRender.class).fireOverlay.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderUnderwaterOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void renderUnderwaterOverlayHook(class_310 minecraftClient, class_4587 matrixStack, CallbackInfo ci) {
        if (Managers.MODULE.getInstance(NoRender.class).liquidOverlay.getValue()) {
            ci.cancel();
        }
    }
}
