package dev.loottech.asm.mixins;

import dev.loottech.api.utilities.render.RenderLayersClient;
import net.minecraft.class_1059;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_4696;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_4696.class})
public abstract class RenderLayersMixin {
    @Shadow
    public static class_1921 method_23679(class_2680 state) {
        return null;
    }

    @Inject(method={"getItemLayer"}, at={@At(value="HEAD")}, cancellable=true)
    private static void hookGetItemLayer(class_1799 stack, boolean direct, CallbackInfoReturnable<class_1921> cir) {
        cir.cancel();
        class_1792 item = stack.method_7909();
        if (item instanceof class_1747) {
            class_2248 block = ((class_1747)item).method_7711();
            class_1921 renderLayer = RenderLayersMixin.method_23679(block.method_9564());
            if (renderLayer == class_1921.method_23583()) {
                if (!class_310.method_29611()) {
                    cir.setReturnValue((Object)((class_1921)RenderLayersClient.ENTITY_TRANSLUCENT_CULL.apply((Object)class_1059.field_5275)));
                } else {
                    cir.setReturnValue((Object)(direct ? (class_1921)RenderLayersClient.ENTITY_TRANSLUCENT_CULL.apply((Object)class_1059.field_5275) : (class_1921)RenderLayersClient.ITEM_ENTITY_TRANSLUCENT_CULL_2.apply((Object)class_1059.field_5275)));
                }
            } else {
                cir.setReturnValue((Object)((class_1921)RenderLayersClient.ENTITY_CUTOUT.apply((Object)class_1059.field_5275)));
            }
        } else {
            cir.setReturnValue((Object)(direct ? (class_1921)RenderLayersClient.ENTITY_TRANSLUCENT_CULL.apply((Object)class_1059.field_5275) : (class_1921)RenderLayersClient.ITEM_ENTITY_TRANSLUCENT_CULL_2.apply((Object)class_1059.field_5275)));
        }
    }
}
