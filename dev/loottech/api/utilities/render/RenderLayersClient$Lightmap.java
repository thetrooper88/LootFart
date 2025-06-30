package dev.loottech.api.utilities.render;

import dev.loottech.api.manager.Managers;
import dev.loottech.asm.mixins.accessor.AccessorRenderPhase;
import net.minecraft.class_4668;

protected static class RenderLayersClient.Lightmap
extends class_4668.class_4676 {
    public RenderLayersClient.Lightmap() {
        super(false);
        ((AccessorRenderPhase)((Object)this)).hookSetBeginAction(() -> Managers.LIGHTMAP.enable());
        ((AccessorRenderPhase)((Object)this)).hookSetEndAction(() -> Managers.LIGHTMAP.disable());
    }
}
