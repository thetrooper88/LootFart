package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1921;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;

public class RenderEntityEvent<T extends class_1309>
extends EventArgument {
    public final class_922<T, class_583<T>> renderer;
    public final class_1309 entity;
    public final float f;
    public final float g;
    public final class_4587 matrixStack;
    public final class_4597 vertexConsumerProvider;
    public final int i;
    public final class_1921 layer;
    public final class_583 model;
    public final List<class_3887<T, class_583<T>>> features;

    public RenderEntityEvent(class_922<T, class_583<T>> renderer, class_1309 entity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, class_583 model, class_1921 layer, List<class_3887<T, class_583<T>>> features) {
        this.renderer = renderer;
        this.entity = entity;
        this.f = f;
        this.g = g;
        this.matrixStack = matrixStack;
        this.vertexConsumerProvider = vertexConsumerProvider;
        this.i = i;
        this.model = model;
        this.layer = layer;
        this.features = features;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderEntity(this);
    }
}
