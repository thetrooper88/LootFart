package dev.loottech.api.utilities.render;

import net.minecraft.class_1799;
import net.minecraft.class_332;

public abstract class OverlayRenderer {
    class_1799 stack;
    int stackX;
    int stackY;

    public OverlayRenderer(class_1799 stack, int x, int y) {
        this.stack = stack;
        this.stackX = x;
        this.stackY = y;
    }

    public void renderOptional(class_332 context) {
        if (this.canDisplay()) {
            this.calculatePositions();
            this.render(context);
        }
    }

    protected abstract boolean canDisplay();

    protected abstract void calculatePositions();

    protected abstract void render(class_332 var1);
}
