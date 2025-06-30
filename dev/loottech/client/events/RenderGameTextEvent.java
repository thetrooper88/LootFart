package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_4587;

public class RenderGameTextEvent
extends EventArgument {
    String text;
    int color;
    float x;
    float y;
    boolean shadow;
    class_4587 matrices;

    public RenderGameTextEvent(String text, int color, float x, float y, boolean shadow, class_4587 matrices) {
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
        this.shadow = shadow;
        this.matrices = matrices;
    }

    public boolean isShadow() {
        return this.shadow;
    }

    public String getText() {
        return this.text;
    }

    public int getColor() {
        return this.color;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public class_4587 getMatrices() {
        return this.matrices;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderGameText(this);
    }
}
