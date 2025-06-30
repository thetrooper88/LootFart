package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.KeyEvent;
import dev.loottech.client.events.MouseUpdateEvent;
import net.minecraft.class_312;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_312.class})
public class MouseMixin
implements IMinecraft {
    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")}, cancellable=true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (button != 0 && button != 1) {
            if (MouseMixin.mc.field_1755 == null) {
                for (Module m : Managers.MODULE.getModules()) {
                    if (m.getBind() != button || action != 1) continue;
                    m.toggle(true);
                }
            }
            KeyEvent event = new KeyEvent(button, action);
            Managers.EVENT.call(event);
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @Redirect(method={"updateMouse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    public void onUpdate(class_746 instance, double cursorDeltaX, double cursorDeltaY) {
        MouseUpdateEvent mouseUpdateEvent = new MouseUpdateEvent(cursorDeltaX, cursorDeltaY);
        Managers.EVENT.call(mouseUpdateEvent);
        if (!mouseUpdateEvent.isCanceled()) {
            instance.method_5872(cursorDeltaX, cursorDeltaY);
        }
    }
}
