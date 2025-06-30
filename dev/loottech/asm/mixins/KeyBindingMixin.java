package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.movement.ElytraFlight;
import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_304.class})
public abstract class KeyBindingMixin {
    @Final
    @Shadow
    private String field_1660;
    @Unique
    ElytraFlight efly = null;

    @Inject(at={@At(value="RETURN")}, method={"isPressed"}, cancellable=true)
    public void isPressed(CallbackInfoReturnable<Boolean> cir) {
        ElytraFlight elytraFlight = this.efly = this.efly == null ? Managers.MODULE.getInstance(ElytraFlight.class) : this.efly;
        if (this.efly != null && this.efly.isEnabled() && this.efly.enabled() && this.field_1660.equals((Object)"key.forward")) {
            cir.setReturnValue((Object)true);
        }
    }
}
