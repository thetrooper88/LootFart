package org.ladysnake.satin.impl;

import java.util.function.Supplier;
import net.minecraft.class_1921;
import net.minecraft.class_4668;
import net.minecraft.class_5944;

private static class RenderLayerSupplier.Helper
extends class_4668 {
    public static class_4668 makeShader(Supplier<class_5944> shader) {
        return new class_4668.class_5942(shader);
    }

    public static void applyShader(class_1921.class_4688.class_4689 builder, class_4668 shader) {
        builder.method_34578((class_4668.class_5942)shader);
    }

    private RenderLayerSupplier.Helper(String name, Runnable beginAction, Runnable endAction) {
        super(name, beginAction, endAction);
    }
}
