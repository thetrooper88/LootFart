package dev.loottech.client.gui.click.manage;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.element.Element;
import dev.loottech.api.manager.module.Module;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.components.ColorComponentTest;
import dev.loottech.client.gui.click.components.ModuleComponent;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.modules.client.ModuleColor;
import dev.loottech.client.modules.client.ModuleGUI;
import java.util.ArrayList;
import net.minecraft.class_332;

public class Frame
implements IMinecraft {
    private final ArrayList<Component> components;
    private final String tab;
    private int x;
    private int y;
    private int height;
    private final int width;
    private boolean open = true;
    private boolean dragging;
    private int dragX;
    private int dragY;
    private float animationProgress = 1.0f;
    private long lastAnimationTime;
    public static final float ANIMATION_SPEED = 3.0f;

    public Frame(Module.Category category, int x, int y) {
        this.tab = category.getName();
        this.x = x;
        this.y = y;
        this.width = 100;
        this.dragging = false;
        this.dragX = 0;
        this.dragY = 0;
        this.components = new ArrayList();
        int offset = 16;
        for (Module module : Managers.MODULE.getModules(category)) {
            this.components.add((Object)new ModuleComponent(module, offset, this));
            offset += 16;
        }
        this.height = offset;
        this.refresh();
        this.lastAnimationTime = System.currentTimeMillis();
    }

    public Frame(int x, int y) {
        this.tab = "HUD";
        this.x = x;
        this.y = y;
        this.width = 100;
        this.dragging = false;
        this.dragX = 0;
        this.dragY = 0;
        this.components = new ArrayList();
        int offset = 6;
        for (Element element : Managers.ELEMENT.getElements()) {
            this.components.add((Object)new ModuleComponent(element, offset, this));
            offset += 16;
        }
        this.height = offset;
        this.refresh();
    }

    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        this.refresh();
        if (this.isDragging()) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        this.updateAnimation();
        float easedProgress = Frame.easeInOutCubic(this.animationProgress);
        int animatedHeight = (int)((float)this.height * easedProgress);
        RenderUtils.drawRect(context.method_51448(), this.getX() - 4, this.getY() - 3, this.getX() + this.getWidth() + 4, this.getY() + 13, Managers.CLICK_GUI.getColor());
        if (this.animationProgress > 0.0f) {
            RenderUtils.drawRect(context.method_51448(), this.getX() - 2, this.getY() + 13, this.getX() + this.getWidth() + 2, this.getY() + animatedHeight, ModuleGUI.INSTANCE.backgroundColor.getValue());
            RenderUtils.drawOutline(context.method_51448(), this.getX() - 2, this.getY() + 13, this.getX() + this.getWidth() + 2, this.getY() + animatedHeight, 0.5f, ModuleColor.getColor());
        }
        Managers.FONT.drawWithShadow(context.method_51448(), this.tab, (float)(this.x + 3), (float)(this.y + 1), -1);
        if (this.animationProgress > 0.0f) {
            for (Component component : this.components) {
                float componentY;
                if (!component.isVisible() || !((componentY = (float)(this.y + 13) + (float)component.getOffset() * easedProgress) < (float)(this.y + 13 + animatedHeight))) continue;
                component.render(context, mouseX, mouseY, partialTicks);
                component.prevMouseX = mouseX;
                component.prevMouseY = mouseY;
            }
        }
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float delta = (float)(currentTime - this.lastAnimationTime) / 1000.0f;
        this.lastAnimationTime = currentTime;
        if (this.open && this.animationProgress < 1.0f) {
            this.animationProgress = Math.min((float)1.0f, (float)(this.animationProgress + delta * 3.0f));
        } else if (!this.open && this.animationProgress > 0.0f) {
            this.animationProgress = Math.max((float)0.0f, (float)(this.animationProgress - delta * 3.0f));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX >= this.getX() - 4 && mouseX <= this.getX() + this.getWidth() + 4 && mouseY >= this.getY() - 3 && mouseY <= this.getY() + 13) {
            if (mouseButton == 0) {
                this.setDragging(true);
                this.dragX = mouseX - this.getX();
                this.dragY = mouseY - this.getY();
            }
            if (mouseButton == 1) {
                this.open = !this.open;
                this.lastAnimationTime = System.currentTimeMillis();
            }
        }
        if (this.animationProgress > 0.5f) {
            for (Component component : this.components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.setDragging(false);
        for (Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void charTyped(char typedChar, int keyCode) {
        if (this.isOpen()) {
            for (Component component : this.components) {
                if (!component.isVisible()) continue;
                component.charTyped(typedChar, keyCode);
            }
        }
    }

    public void refresh() {
        int offset = 16;
        for (Component component : this.components) {
            ModuleComponent moduleComponent;
            if (!component.isVisible()) continue;
            component.setOffset(offset);
            offset += 16;
            if (!(component instanceof ModuleComponent) || (moduleComponent = (ModuleComponent)component).getModule().getValues().isEmpty() || !moduleComponent.isOpen()) continue;
            for (Component valueComponent : moduleComponent.getComponents()) {
                if (!valueComponent.isVisible()) continue;
                valueComponent.setOffset(offset);
                offset += valueComponent instanceof ColorComponentTest && ((ColorComponentTest)valueComponent).isOpen() ? 190 : 14;
            }
        }
        this.setHeight(offset);
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isOpen() {
        return this.open;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
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

    public float getAnimationProgress() {
        return Frame.easeInOutCubic(this.animationProgress);
    }

    private static float easeInOutCubic(float t) {
        return (double)t < 0.5 ? 4.0f * t * t * t : 1.0f - (float)Math.pow((double)(-2.0f * t + 2.0f), (double)3.0) / 2.0f;
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }
}
