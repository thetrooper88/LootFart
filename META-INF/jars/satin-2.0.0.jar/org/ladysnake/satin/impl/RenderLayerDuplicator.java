package org.ladysnake.satin.impl;

import java.util.function.Consumer;
import net.minecraft.class_1921;
import net.minecraft.class_293;
import org.jetbrains.annotations.Nullable;

public final class RenderLayerDuplicator {
    public static class_1921 copy(class_1921 existing, String newName, Consumer<class_1921.class_4688.class_4689> op) {
        return RenderLayerDuplicator.copy(existing, newName, null, op);
    }

    public static class_1921 copy(class_1921 existing, String newName, @Nullable class_293 vertexFormat, Consumer<class_1921.class_4688.class_4689> op) {
        RenderLayerDuplicator.checkDefaultImpl(existing);
        return ((SatinRenderLayer)existing).satin$copy(newName, vertexFormat, op);
    }

    public static class_1921.class_4688 copyPhaseParameters(class_1921 existing, Consumer<class_1921.class_4688.class_4689> op) {
        RenderLayerDuplicator.checkDefaultImpl(existing);
        return ((SatinRenderLayer)existing).satin$copyPhaseParameters(op);
    }

    private static void checkDefaultImpl(class_1921 existing) {
        if (!(existing instanceof SatinRenderLayer)) {
            throw new IllegalArgumentException("Unrecognized RenderLayer implementation " + String.valueOf((Object)existing.getClass()) + ". Layer duplication is only applicable to the default (MultiPhase) implementation");
        }
    }

    public static interface SatinRenderLayer {
        public class_1921 satin$copy(String var1, @Nullable class_293 var2, Consumer<class_1921.class_4688.class_4689> var3);

        public class_1921.class_4688 satin$copyPhaseParameters(Consumer<class_1921.class_4688.class_4689> var1);
    }
}
