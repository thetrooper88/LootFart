package dev.loottech.client.gui.click.components;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.render.RenderUtils;
import dev.loottech.client.gui.click.manage.Component;
import dev.loottech.client.gui.click.manage.Frame;
import dev.loottech.client.values.impl.ValueColor;
import java.awt.Color;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class ColorComponentTest
extends Component {
    private final ValueColor value;
    public boolean open = false;
    boolean hueDragging;
    float hueWidth;
    float saturationWidth;
    float brightnessHeight;
    boolean hsDragging;
    boolean alphaDragging;
    float alphaWidth;

    public ColorComponentTest(ValueColor value, int offset, Frame parent, Supplier<Boolean> visible) {
        super(offset, parent, visible);
        this.value = value;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float partialTicks) {
        float[] hsb = Color.RGBtoHSB((int)this.value.getValue().getRed(), (int)this.value.getValue().getGreen(), (int)this.value.getValue().getBlue(), null);
        Color color = Color.getHSBColor((float)hsb[0], (float)1.0f, (float)1.0f);
        RenderUtils.drawRect(context.method_51448(), this.getX() + this.getWidth() - 12, this.getY() + 2, this.getX() + this.getWidth() - 2, this.getY() + 12, this.value.getValue());
        RenderUtils.drawOutline(context.method_51448(), this.getX() + this.getWidth() - 12, this.getY() + 2, this.getX() + this.getWidth() - 2, this.getY() + 12, 0.5f, new Color(20, 20, 20));
        if (this.isOpen()) {
            float i = 0.0f;
            while (i + 1.0f < 96.0f) {
                RenderUtils.drawRect(context.method_51448(), (float)(this.getX() + 2) + i, this.getY() + 16, (float)(this.getX() + 2) + i + 1.0f, this.getY() + 27, Color.getHSBColor((float)(i / 96.0f), (float)1.0f, (float)1.0f));
                i += 0.45f;
            }
            RenderUtils.drawOutline(context.method_51448(), this.getX() + 2, this.getY() + 16, this.getX() + this.getWidth() - 2, this.getY() + 27, 0.5f, Color.BLACK);
            RenderUtils.drawOutline(context.method_51448(), (float)(this.getX() + 2) + this.hueWidth, this.getY() + 16, (float)(this.getX() + 2) + this.hueWidth + 2.0f, this.getY() + 27, 0.5f, Color.WHITE);
            RenderUtils.drawSidewaysGradient(context.method_51448(), this.getX() + 2, this.getY() + 29, this.getWidth() - 4, 96.0f, new Color(255, 255, 255), color);
            RenderUtils.drawOutline(context.method_51448(), this.getX() + 2, this.getY() + 29, this.getX() + this.getWidth() - 2, this.getY() + 29 + 96, 0.5f, Color.BLACK);
            RenderUtils.drawOutline(context.method_51448(), (float)(this.getX() + 2) + this.saturationWidth, (float)(this.getY() + 29) - this.brightnessHeight + 96.0f, (float)(this.getX() + 2) + this.saturationWidth + 2.0f, (float)(this.getY() + 29) - this.brightnessHeight + 2.0f + 96.0f, 0.8f, Color.BLACK);
            RenderUtils.drawOutline(context.method_51448(), (float)(this.getX() + 2) + this.saturationWidth, (float)(this.getY() + 29) - this.brightnessHeight + 96.0f, (float)(this.getX() + 2) + this.saturationWidth + 2.0f, (float)(this.getY() + 29) - this.brightnessHeight + 2.0f + 96.0f, 0.5f, Color.WHITE);
            RenderUtils.drawSidewaysGradient(context.method_51448(), this.getX() + 2, this.getY() + 28 + 96, this.getWidth() - 4, 11.0f, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0), color);
            RenderUtils.drawOutline(context.method_51448(), this.getX() + 2, this.getY() + 28 + 96, this.getX() + this.getWidth() - 2, this.getY() + 39 + 96, 0.5f, Color.BLACK);
            RenderUtils.drawOutline(context.method_51448(), (float)(this.getX() + 2) + this.alphaWidth, this.getY() + 28 + 96, (float)(this.getX() + 2) + this.alphaWidth + 2.0f, this.getY() + 39 + 96, 0.5f, Color.WHITE);
            RenderUtils.drawRect(context.method_51448(), this.getX() + 2, this.getY() + 42 + 96, this.getX() + 49, this.getY() + 53 + 96, Managers.CLICK_GUI.getColor());
            class_4587 class_45872 = context.method_51448();
            float f = (float)(this.getX() + 25) - Managers.FONT.getWidth("Copy") / 2.0f;
            int n = this.getY() + 47 + 96;
            Objects.requireNonNull((Object)ColorComponentTest.mc.field_1772);
            Managers.FONT.drawWithShadow(class_45872, "Copy", f, (float)(n - 9 / 2), -1);
            RenderUtils.drawRect(context.method_51448(), this.getX() + 51, this.getY() + 42 + 96, this.getX() + 98, this.getY() + 53 + 96, Managers.CLICK_GUI.getColor());
            class_4587 class_45873 = context.method_51448();
            float f2 = (float)(this.getX() + 75) - Managers.FONT.getWidth("Paste") / 2.0f;
            int n2 = this.getY() + 47 + 96;
            Objects.requireNonNull((Object)ColorComponentTest.mc.field_1772);
            Managers.FONT.drawWithShadow(class_45873, "Paste", f2, (float)(n2 - 9 / 2), -1);
            if (this.value.isRainbow()) {
                RenderUtils.drawRect(context.method_51448(), this.getX() + 2, this.getY() + 56 + 96, this.getX() + this.getWidth() - 2, this.getY() + 70 + 96, Managers.CLICK_GUI.getColor());
            }
            Managers.FONT.drawWithShadow(context.method_51448(), "Rainbow", (float)(this.getX() + 48) - Managers.FONT.getWidth("Rainbow") / 2.0f, (float)(this.getY() + 59 + 96), -1);
            if (this.value.isSync()) {
                RenderUtils.drawRect(context.method_51448(), this.getX() + 2, this.getY() + 73 + 96, this.getX() + this.getWidth() - 2, this.getY() + 87 + 96, Managers.CLICK_GUI.getColor());
            }
            Managers.FONT.drawWithShadow(context.method_51448(), "Sync", (float)(this.getX() + 48) - Managers.FONT.getWidth("Sync") / 2.0f, (float)(this.getY() + 76 + 96), -1);
        }
        Managers.FONT.drawWithShadow(context.method_51448(), this.value.getTag(), (float)(this.getX() + 3), (float)(this.getY() + 3), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (this.isOpen()) {
                if (this.isHoveringHue(mouseX, mouseY)) {
                    this.hueDragging = true;
                }
                if (this.isHoveringHS(mouseX, mouseY)) {
                    this.hsDragging = true;
                }
                if (this.isHoveringAlpha(mouseX, mouseY)) {
                    this.alphaDragging = true;
                }
                if (this.isHoveringCopy(mouseX, mouseY)) {
                    Managers.COLOR_CLIPBOARD = this.value.getActualValue();
                }
                if (this.isHoveringPaste(mouseX, mouseY) && Managers.COLOR_CLIPBOARD != null) {
                    this.value.setValue(Managers.COLOR_CLIPBOARD);
                }
                if (this.isHoveringRainbow(mouseX, mouseY)) {
                    this.value.setRainbow(!this.value.isRainbow());
                }
                if (this.isHoveringSync(mouseX, mouseY)) {
                    this.value.setSync(!this.value.isSync());
                }
            }
        } else if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.hueDragging = false;
        this.hsDragging = false;
        this.alphaDragging = false;
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
        float[] hsb = Color.RGBtoHSB((int)this.value.getValue().getRed(), (int)this.value.getValue().getGreen(), (int)this.value.getValue().getBlue(), null);
        double difference = Math.min((int)95, (int)Math.max((int)0, (int)(mouseX - this.getX())));
        double differenceY = Math.min((int)95, (int)Math.max((int)0, (int)(mouseY - this.getY() - 29)));
        this.hueWidth = 95.5f * (hsb[0] * 360.0f / 360.0f);
        this.saturationWidth = 94.5f * (hsb[1] * 360.0f / 360.0f);
        this.brightnessHeight = 94.5f * (hsb[2] * 360.0f / 360.0f);
        this.alphaWidth = 94.5f * ((float)this.value.getValue().getAlpha() / 255.0f);
        this.changeColor(difference, new Color(Color.HSBtoRGB((float)((float)(difference / 95.0 * 360.0 / 360.0)), (float)hsb[1], (float)hsb[2])), new Color(Color.HSBtoRGB((float)0.0f, (float)hsb[1], (float)hsb[2])), this.hueDragging);
        this.changeHS(difference, differenceY, new Color(Color.HSBtoRGB((float)hsb[0], (float)((float)(difference / 95.0 * 360.0 / 360.0)), (float)(1.0f - (float)(differenceY / 95.0 * 360.0 / 360.0)))), new Color(Color.HSBtoRGB((float)hsb[0], (float)0.0f, (float)0.0f)), this.hsDragging);
        this.changeAlpha(difference, (float)(difference / 95.0 * 255.0 / 255.0), this.alphaDragging);
    }

    private void changeHS(double difference, double difference2, Color color, Color zeroColor, boolean dragging) {
        if (dragging) {
            if (difference == 0.0 && difference2 == 0.0) {
                this.value.setValue(new Color(zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue(), this.value.getValue().getAlpha()));
            } else {
                this.value.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), this.value.getValue().getAlpha()));
            }
        }
    }

    private void changeColor(double difference, Color color, Color zeroColor, boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                this.value.setValue(new Color(zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue(), this.value.getValue().getAlpha()));
            } else {
                this.value.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), this.value.getValue().getAlpha()));
            }
        }
    }

    private void changeAlpha(double difference, float alpha, boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                this.value.setValue(new Color(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), 0));
            } else {
                this.value.setValue(new Color(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), (int)(alpha * 255.0f)));
            }
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean isHoveringHue(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 16 && mouseY < this.getY() + 27;
    }

    public boolean isHoveringHS(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 29 && mouseY < this.getY() + 25 + this.getWidth();
    }

    public boolean isHoveringAlpha(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 28 + this.getWidth() && mouseY < this.getY() + 39 + this.getWidth();
    }

    public boolean isHoveringCopy(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 49 && mouseY > this.getY() + 42 + this.getWidth() && mouseY < this.getY() + 53 + this.getWidth();
    }

    public boolean isHoveringPaste(int mouseX, int mouseY) {
        return mouseX > this.getX() + 51 && mouseX < this.getX() + 98 && mouseY > this.getY() + 42 + this.getWidth() && mouseY < this.getY() + 53 + this.getWidth();
    }

    public boolean isHoveringRainbow(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 98 && mouseY > this.getY() + 56 + this.getWidth() && mouseY < this.getY() + 70 + this.getWidth();
    }

    public boolean isHoveringSync(int mouseX, int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 98 && mouseY > this.getY() + 73 + this.getWidth() && mouseY < this.getY() + 87 + this.getWidth();
    }
}
