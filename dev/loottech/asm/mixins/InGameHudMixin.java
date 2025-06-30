package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_329.class})
public abstract class InGameHudMixin {
    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    public void onRender(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        Render2DEvent event = new Render2DEvent(tickCounter.method_60637(true), context);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderStatusEffectOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderStatusEffectOverlay(class_332 context, class_9779 tickCounter, CallbackInfo info) {
        if (Managers.ELEMENT.isElementEnabled("ModuleList") || Managers.MODULE.getInstance(NoRender.class).statusOverlay.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderCrosshair(CallbackInfo ci) {
        if (!Managers.MODULE.isModuleEnabled("Crosshair")) {
            return;
        }
        ci.cancel();
    }
}
