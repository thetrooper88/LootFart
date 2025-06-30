package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.manage.AnimatedComponent;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueBoolean;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.class_332;

public class BooleanComponent
extends AnimatedComponent {
    private final ValueBoolean value;
    private float toggleProgress = 0.0f;
    private long lastToggleTime = System.currentTimeMillis();

    public BooleanComponent(ValueBoolean value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        this.updateToggleAnimation();
        float progress = this.getEasedProgress();
        if (progress <= 0.0f) {
            return;
        }
        int alpha = (int)(progress * 255.0f);
        int y = (int)((float)this.getY() + (1.0f - progress) * 5.0f);
        int fullWidth = this.getWidth() - 0;
        int animatedWidth = (int)((double)fullWidth * (1.0 - Math.pow((double)(1.0f - this.toggleProgress), (double)3.0)));
        if (animatedWidth > 0) {
            RenderUtils.drawRect(context.method_51448(), this.getX() + 1, this.getY(), this.getX() + animatedWidth - 1, this.getY() + 14, Managers.CLICK_GUI.getColor());
        }
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag(), (float)(this.getX() + 3), (float)(y + 3), new Color(255, 255, 255, alpha).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            this.value.setValue(!this.value.getValue());
        }
    }

    private void updateToggleAnimation() {
        long now = System.currentTimeMillis();
        float delta = (float)(now - this.lastToggleTime) / 1000.0f;
        this.lastToggleTime = now;
        float speed = 6.5f;
        this.toggleProgress = this.value.getValue() ? Math.min((float)1.0f, (float)(this.toggleProgress + delta * speed)) : Math.max((float)0.0f, (float)(this.toggleProgress - delta * speed));
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
    }

    public ValueBoolean getValue() {
        return this.value;
    }
}
