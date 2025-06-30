package org.ladysnake.satin.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import net.minecraft.class_4668;
import net.minecraft.class_5944;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.satin.impl.RenderLayerDuplicator;
import org.ladysnake.satin.mixin.client.render.RenderPhaseAccessor;

public class RenderLayerSupplier {
    private final Consumer<class_1921.class_4688.class_4689> transform;
    private final Map<class_1921, class_1921> renderLayerCache = new HashMap();
    private final String uniqueName;
    @Nullable
    private final class_293 vertexFormat;

    public static RenderLayerSupplier framebuffer(String name, Runnable setupState, Runnable cleanupState) {
        class_4668.class_4678 target = new class_4668.class_4678(name + "_target", setupState, cleanupState);
        return new RenderLayerSupplier(name, (Consumer<class_1921.class_4688.class_4689>)((Consumer)builder -> builder.method_23610(target)));
    }

    public static RenderLayerSupplier shader(String name, class_293 vertexFormat, Supplier<class_5944> shaderSupplier) {
        class_4668 shader = Helper.makeShader(shaderSupplier);
        return new RenderLayerSupplier(name, vertexFormat, (Consumer<class_1921.class_4688.class_4689>)((Consumer)builder -> Helper.applyShader(builder, shader)));
    }

    public RenderLayerSupplier(String name, Consumer<class_1921.class_4688.class_4689> transformer) {
        this(name, null, transformer);
    }

    public RenderLayerSupplier(String name, @Nullable class_293 vertexFormat, Consumer<class_1921.class_4688.class_4689> transformer) {
        this.uniqueName = name;
        this.vertexFormat = vertexFormat;
        this.transform = transformer;
    }

    public class_1921 getRenderLayer(class_1921 baseLayer) {
        class_1921 existing = (class_1921)this.renderLayerCache.get((Object)baseLayer);
        if (existing != null) {
            return existing;
        }
        String newName = ((RenderPhaseAccessor)baseLayer).getName() + "_" + this.uniqueName;
        class_1921 newLayer = RenderLayerDuplicator.copy(baseLayer, newName, this.vertexFormat, this.transform);
        this.renderLayerCache.put((Object)baseLayer, (Object)newLayer);
        return newLayer;
    }

    private static class Helper
    extends class_4668 {
        public static class_4668 makeShader(Supplier<class_5944> shader) {
            return new class_4668.class_5942(shader);
        }

        public static void applyShader(class_1921.class_4688.class_4689 builder, class_4668 shader) {
            builder.method_34578((class_4668.class_5942)shader);
        }

        private Helper(String name, Runnable beginAction, Runnable endAction) {
            super(name, beginAction, endAction);
        }
    }
}
