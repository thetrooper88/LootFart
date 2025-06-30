package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.Chams;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4604;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_897.class})
public abstract class EntityRendererMixin<T extends class_1297> {
    @Inject(method={"renderLabelIfPresent"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderLabelIfPresent(T entity, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        if (entity instanceof class_1657 && (Managers.MODULE.isModuleEnabled("Nametags") || Managers.MODULE.isModuleEnabled("NametagsRew"))) {
            ci.cancel();
        }
    }

    @Inject(method={"shouldRender"}, at={@At(value="HEAD")}, cancellable=true)
    public void shouldRenderHook(T entity, class_4604 frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        Chams chamsModule;
        if ((entity instanceof class_1511 || entity instanceof class_1657) && (chamsModule = Managers.MODULE.getInstance(Chams.class)) != null && chamsModule.isEnabled() && chamsModule.wallsConfig.getValue()) {
            cir.setReturnValue((Object)true);
        }
    }
}
