package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.KeyboardTickEvent;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_743.class})
public class KeyboardInputMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookTick$Pre(boolean slowDown, float slowDownFactor, CallbackInfo info) {
        KeyboardTickEvent event = new KeyboardTickEvent((class_744)this);
        Managers.EVENT.call(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
}
