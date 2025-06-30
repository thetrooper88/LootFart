package dev.loottech.asm.mixins;

import dev.loottech.api.utilities.render.RenderLayersClient;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_8538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_8538.class})
public class TextRenderLayerSetMixin {
    @Inject(method={"of"}, at={@At(value="HEAD")}, cancellable=true)
    private static void hookOf(class_2960 textureId, CallbackInfoReturnable<class_8538> cir) {
        cir.cancel();
        cir.setReturnValue((Object)new class_8538((class_1921)RenderLayersClient.TEXT.apply((Object)textureId), (class_1921)RenderLayersClient.TEXT_SEE_THROUGH.apply((Object)textureId), (class_1921)RenderLayersClient.TEXT_POLYGON_OFFSET.apply((Object)textureId)));
    }

    @Inject(method={"ofIntensity"}, at={@At(value="HEAD")}, cancellable=true)
    private static void hookOfIntensity(class_2960 textureId, CallbackInfoReturnable<class_8538> cir) {
        cir.cancel();
        cir.setReturnValue((Object)new class_8538((class_1921)RenderLayersClient.TEXT_INTENSITY.apply((Object)textureId), (class_1921)RenderLayersClient.TEXT_INTENSITY_SEE_THROUGH.apply((Object)textureId), (class_1921)RenderLayersClient.TEXT_INTENSITY_POLYGON_OFFSET.apply((Object)textureId)));
    }
}
