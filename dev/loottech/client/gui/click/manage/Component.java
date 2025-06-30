package dev.loottech.client.gui.click.manage;

import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.gui.click.manage.Frame;
import java.util.function.Supplier;
import net.minecraft.class_332;

public class Component
implements IMinecraft {
    private Supplier<Boolean> visible;
    private int offset;
    private final int width;
    private int height;
    private final Frame parent;
    private float animationProgress = 1.0f;
    int prevMouseX;
    int prevMouseY;
    private long lastAnimationTime;
    protected static final float ANIMATION_SPEED = 3.0f;

    public Component(int offset, Frame parent, Supplier<Boolean> visible) {
        this.offset = offset;
        this.parent = parent;
        this.width = parent.getWidth();
        this.height = 14;
        this.visible = visible;
        this.lastAnimationTime = System.currentTimeMillis();
    }

    public void render(class_332 context, int mouseX, int mouseY, float delta) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void charTyped(char c, int keyCode) {
    }

    public void onClose() {
    }

    public void update(int mouseX, int mouseY, float partialTicks) {
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight();
    }

    public boolean isVisible() {
        return (Boolean)this.visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible = () -> visible;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getX() {
        return this.parent.getX();
    }

    public int getY() {
        return this.parent.getY() + (int)((float)this.offset * this.parent.getAnimationProgress());
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Frame getParent() {
        return this.parent;
    }

    protected float getEasedProgress() {
        return 1.0f - (float)Math.pow((double)(1.0f - this.animationProgress), (double)3.0);
    }
}
