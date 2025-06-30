package dev.loottech.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.loottech.api.manager.Managers;
import dev.loottech.client.modules.visuals.NoRender;
import net.minecraft.class_4184;
import net.minecraft.class_758;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_758.class})
public abstract class BackgroundRendererMixin {
    @Inject(method={"applyFog"}, at={@At(value="TAIL")}, cancellable=true)
    private static void applyFog(class_4184 camera, class_758.class_4596 fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        NoRender noRender = Managers.MODULE.getInstance(NoRender.class);
        if (Managers.MODULE.getInstance(NoRender.class).noFog.getValue() && fogType == class_758.class_4596.field_20946) {
            RenderSystem.setShaderFogStart((float)(viewDistance * 5.0f));
            RenderSystem.setShaderFogEnd((float)(viewDistance * 5.1f));
        }
        if (!Managers.MODULE.getInstance(NoRender.class).noFog.getValue() && fogType == class_758.class_4596.field_20946) {
            RenderSystem.setShaderFogColor((float)noRender.fogColor.getValue().getRed(), (float)noRender.fogColor.getValue().getGreen(), (float)noRender.fogColor.getValue().getBlue(), (float)((float)noRender.fogColor.getValue().getAlpha() / 10.0f));
            RenderSystem.setShaderFogStart((float)(viewDistance * noRender.fogStart.getValue().floatValue() / 10.0f));
            RenderSystem.setShaderFogEnd((float)(viewDistance * noRender.fogEnd.getValue().floatValue() * 10.0f));
        }
    }
}
