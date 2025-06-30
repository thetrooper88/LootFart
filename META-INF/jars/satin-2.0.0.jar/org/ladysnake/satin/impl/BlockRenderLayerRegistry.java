package org.ladysnake.satin.impl;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Set;
import net.minecraft.class_1921;

public final class BlockRenderLayerRegistry {
    public static final BlockRenderLayerRegistry INSTANCE = new BlockRenderLayerRegistry();
    private final Set<class_1921> renderLayers = new ObjectArraySet();
    private boolean registryLocked = false;

    private BlockRenderLayerRegistry() {
    }

    public void registerRenderLayer(class_1921 layer) {
        if (this.registryLocked) {
            throw new IllegalStateException(String.format((String)"RenderLayer %s was added too late.", (Object[])new Object[]{layer}));
        }
        this.renderLayers.add((Object)layer);
    }

    public Set<class_1921> getLayers() {
        this.registryLocked = true;
        return this.renderLayers;
    }
}
