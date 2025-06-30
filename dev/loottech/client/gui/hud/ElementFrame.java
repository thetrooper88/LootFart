package dev.loottech.client.gui.hud;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.events.Render2DEvent;
import dev.loottech.client.gui.hud.HudEditorScreen;
import java.awt.Color;
import net.minecraft.class_332;

public class ElementFrame
implements IMinecraft {
    private final Element element;
    private float x;
    private float y;
    private float width;
    private float height;
    private float dragX;
    private float dragY;
    private boolean dragging;
    private boolean visible;
    private HudEditorScreen parent;

    public ElementFrame(Element element, float x, float y, float width, float height, HudEditorScreen parent) {
        this.element = element;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.dragging = false;
        this.visible = true;
    }

    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        if (this.element != null && Managers.ELEMENT.isElementEnabled(this.element.getName()) && mc.method_22683() != null) {
            if (this.dragging) {
                this.x = this.element.isSnapped() && this.isHovering(mouseX, mouseY) ? this.element.frame.getX() : this.dragX + (float)mouseX;
                this.y = this.dragY + (float)mouseY;
                if ((double)this.x < 0.0) {
                    this.x = 0.0f;
                }
                if ((double)this.y < 0.0) {
                    this.y = 0.0f;
                }
                if (this.x > (float)mc.method_22683().method_4486() - this.width) {
                    this.x = (float)mc.method_22683().method_4486() - this.width;
                }
                if (this.y > (float)mc.method_22683().method_4502() - this.height) {
                    this.y = (float)mc.method_22683().method_4502() - this.height;
                }
            }
            if (this.dragging) {
                RenderUtils.drawOutline(context.method_51448(), this.element.frame.getX() - 1.0f, this.element.frame.getY() - 1.0f, this.element.frame.getX() + this.element.frame.width + 1.0f, this.element.frame.getY() + this.element.frame.height + 1.0f, 1.0f, Color.WHITE);
                RenderUtils.drawRect(context.method_51448(), this.x, this.y, this.x + this.width, this.y + this.height, new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 100));
            } else {
                RenderUtils.drawRect(context.method_51448(), this.x, this.y, this.x + this.width, this.y + this.height, new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 100));
            }
            this.element.onRender2D(new Render2DEvent(partialTicks, context));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            class_332 context = new class_332(mc, mc.method_22940().method_23000());
            RenderUtils.rect(context.method_51448(), this.x, this.y, this.x + this.width, this.y + this.height, -1);
            this.dragX = this.x - (float)mouseX;
            this.dragY = this.y - (float)mouseY;
            this.dragging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return (float)mouseX >= this.x && (float)mouseX <= this.x + this.width && (float)mouseY >= this.y && (float)mouseY <= this.y + this.height;
    }

    public Element getElement() {
        return this.element;
    }

    public HudEditorScreen getParent() {
        return this.parent;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
