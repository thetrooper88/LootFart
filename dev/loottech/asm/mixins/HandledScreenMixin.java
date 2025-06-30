package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.MouseDragEvent;
import dev.loottech.client.events.RenderTooltipEvent;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_465.class})
public abstract class HandledScreenMixin {
    @Shadow
    @Nullable
    protected class_1735 field_2787;
    @Shadow
    private boolean field_2783;

    @Shadow
    @Nullable
    protected abstract class_1735 method_2386(double var1, double var3);

    @Shadow
    protected abstract void method_2383(class_1735 var1, int var2, int var3, class_1713 var4);

    @Inject(method={"drawMouseoverTooltip"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookDrawMouseoverTooltip(class_332 context, int x, int y, CallbackInfo ci) {
        if (this.field_2787 == null) {
            return;
        }
        RenderTooltipEvent renderTooltipEvent = new RenderTooltipEvent(context, this.field_2787.method_7677(), x, y);
        Managers.EVENT.call(renderTooltipEvent);
        if (renderTooltipEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"mouseDragged"}, at={@At(value="TAIL")})
    private void hookMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        MouseDragEvent mouseDraggedEvent = new MouseDragEvent();
        Managers.EVENT.call(mouseDraggedEvent);
        if (button != 0 || this.field_2783 || !mouseDraggedEvent.isCanceled()) {
            return;
        }
        class_1735 slot = this.method_2386(mouseX, mouseY);
        if (slot != null && slot.method_7681() && class_437.method_25442()) {
            this.method_2383(slot, slot.field_7874, button, class_1713.field_7794);
        }
    }
}
