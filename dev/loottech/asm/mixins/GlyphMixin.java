package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.client.VanillaFont;
import net.minecraft.class_379;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_379.class})
public interface GlyphMixin {
    @Inject(method={"getShadowOffset"}, at={@At(value="HEAD")}, cancellable=true)
    private void getShadowOffset(CallbackInfoReturnable<Float> info) {
        if (Managers.MODULE != null && Managers.MODULE.getInstance(VanillaFont.class).isEnabled() && !Managers.MODULE.getInstance(VanillaFont.class).textShadow.getValue().equals((Object)VanillaFont.ShadowModes.DEFAULT)) {
            info.setReturnValue((Object)Float.valueOf((float)Managers.MODULE.getInstance(VanillaFont.class).getShadowValue()));
        }
    }
}
