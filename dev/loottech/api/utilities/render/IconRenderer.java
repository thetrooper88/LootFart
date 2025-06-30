package dev.loottech.api.utilities.render;

import dev.loottech.api.manager.miscellaneous.ContainerManager;
import dev.loottech.api.utilities.render.OverlayRenderer;
import dev.loottech.asm.ducks.IDrawContext;
import net.minecraft.class_1799;
import net.minecraft.class_332;

public class IconRenderer
extends OverlayRenderer {
    public float scale;
    public float xOffset;
    public float yOffset;
    public float zOffset;

    public IconRenderer(ContainerManager containerParser, class_1799 displayStack, int x, int y) {
        super(displayStack, x, y);
    }

    @Override
    protected void calculatePositions() {
        this.xOffset = 4.0f;
        this.yOffset = -4.0f;
        this.zOffset = 200.0f;
        this.scale = 10.0f;
    }

    @Override
    protected void render(class_332 context) {
        ((IDrawContext)context).loottech$setAdjustSize(true);
        context.method_51445(this.stack, this.stackX, this.stackY);
        ((IDrawContext)context).loottech$setAdjustSize(false);
    }

    @Override
    public void renderOptional(class_332 context) {
        if (this.canDisplay()) {
            this.calculatePositions();
            this.render(context);
        }
    }

    @Override
    protected boolean canDisplay() {
        return this.stack != null && this.stack.method_7909() != null;
    }
}
