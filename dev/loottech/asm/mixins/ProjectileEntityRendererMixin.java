package dev.loottech.asm.mixins;

import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_1665;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_876;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_876.class})
public abstract class ProjectileEntityRendererMixin<T extends class_1665> {
    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void render(T entity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        if (Managers.MODULE.getInstance(NoRender.class).arrows.getValue()) {
            ci.cancel();
        }
    }
}
