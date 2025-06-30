package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.events.KeyEvent;
import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_309.class})
public class KeyboardMixin
implements IMinecraft {
    @Inject(method={"onKey"}, at={@At(value="HEAD")}, cancellable=true)
    public void injectOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key != -1) {
            if (KeyboardMixin.mc.field_1755 == null) {
                Managers.MODULE.getModules().stream().filter(m -> m.getBind() == key).forEach(m -> {
                    if (action == 1) {
                        m.toggle(true);
                    }
                });
            }
            KeyEvent event = new KeyEvent(key, action);
            Managers.EVENT.call(event);
            if (event.isCanceled()) {
                ci.cancel();
            }
        }
    }
}
