package org.ladysnake.satin.mixin.client.blockrenderlayer;

import com.google.common.collect.ImmutableList;
import net.minecraft.class_1921;
import net.minecraft.class_4668;
import org.ladysnake.satin.impl.BlockRenderLayerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1921.class})
public abstract class RenderLayerMixin
extends class_4668 {
    private RenderLayerMixin() {
        super(null, null, null);
    }

    @Inject(method={"getBlockLayers"}, at={@At(value="RETURN")}, cancellable=true)
    private static void getBlockLayers(CallbackInfoReturnable<ImmutableList<Object>> info) {
        info.setReturnValue((Object)ImmutableList.builder().addAll((Iterable)info.getReturnValue()).addAll(BlockRenderLayerRegistry.INSTANCE.getLayers()).build());
    }
}
