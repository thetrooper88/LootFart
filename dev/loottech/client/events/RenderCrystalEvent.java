package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1511;
import net.minecraft.class_4587;
import net.minecraft.class_630;

public class RenderCrystalEvent
extends EventArgument {
    public final class_1511 endCrystalEntity;
    public final float f;
    public final float g;
    public final class_4587 matrixStack;
    public final int i;
    public final class_630 core;
    public final class_630 frame;
    public float spin = 1.0f;
    public float scale = 1.0f;
    public boolean bounce = true;

    public RenderCrystalEvent(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, int i, class_630 core, class_630 frame) {
        this.endCrystalEntity = endCrystalEntity;
        this.f = f;
        this.g = g;
        this.matrixStack = matrixStack;
        this.i = i;
        this.core = core;
        this.frame = frame;
    }

    public float getSpin() {
        return this.spin;
    }

    public float getScale() {
        return this.scale;
    }

    public boolean getBounce() {
        return this.bounce;
    }

    public void setSpin(float spin) {
        this.spin = spin;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setBounce(boolean bounce) {
        this.bounce = bounce;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderCrystal(this);
    }
}
