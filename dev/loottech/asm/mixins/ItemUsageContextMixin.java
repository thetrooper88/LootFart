package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.ItemDesyncEvent;
import net.minecraft.class_1799;
import net.minecraft.class_1838;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1838.class})
public class ItemUsageContextMixin {
    @Inject(method={"getStack"}, at={@At(value="RETURN")}, cancellable=true)
    public void hookGetStack(CallbackInfoReturnable<class_1799> info) {
        ItemDesyncEvent itemDesyncEvent = new ItemDesyncEvent();
        Managers.EVENT.call(itemDesyncEvent);
        if (Util.mc.field_1724 != null && ((class_1799)info.getReturnValue()).equals((Object)Util.mc.field_1724.method_6047()) && itemDesyncEvent.isCanceled()) {
            info.setReturnValue((Object)itemDesyncEvent.getServerItem());
        }
    }
}
