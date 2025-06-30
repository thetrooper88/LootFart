package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.events.TickCounterEvent;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_9779.class_9781.class})
public class RenderTickCounterMixin {
    @Shadow
    private float field_51958;
    @Shadow
    private float field_51959;
    @Shadow
    private long field_51962;
    @Final
    @Shadow
    private float field_51964;

    @Inject(method={"beginRenderTick(J)I"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        TickCounterEvent tickCounterEvent = new TickCounterEvent();
        Managers.EVENT.call(tickCounterEvent);
        if (tickCounterEvent.isCanceled()) {
            this.field_51958 = (float)(timeMillis - this.field_51962) / this.field_51964 * tickCounterEvent.getTicks();
            this.field_51962 = timeMillis;
            this.field_51959 += this.field_51958;
            int i = (int)this.field_51959;
            this.field_51959 -= (float)i;
            cir.setReturnValue((Object)i);
        }
    }
}
