package dev.loottech.client.font;

import dev.loottech.client.font.FontRenderer;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5348;

public enum FontHelper {
    INSTANCE;

    private static class_310 mc;
    private FontRenderer clientFont = new FontRenderer("Arial", 20.0f, 1.0f);

    public float getStringWidth(String string, boolean customFont) {
        if (customFont) {
            return this.clientFont.getStringWidth(this.clientFont.stripControlCodes(string));
        }
        return FontHelper.mc.field_1772.method_1727(string);
    }

    public float getStringHeight(String string, boolean customFont) {
        if (customFont) {
            return this.clientFont.getStringHeight(this.clientFont.stripControlCodes(string));
        }
        Objects.requireNonNull((Object)FontHelper.mc.field_1772);
        return 9.0f;
    }

    public float getStringWidth(class_2561 string) {
        return FontHelper.mc.field_1772.method_27525((class_5348)string);
    }

    public void drawWithShadow(class_4587 matrixStack, String text, float x, float y, int color, boolean customFont) {
        if (customFont) {
            this.clientFont.drawString(matrixStack, text, x, y, FontRenderer.FontType.SHADOW_THIN, color);
        } else {
            FontHelper.mc.field_1772.method_27522(text, x + 0.5f, y + 0.5f, -16777216, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
            FontHelper.mc.field_1772.method_27522(text, x, y, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public void draw(class_4587 matrixStack, String text, float x, float y, int color, boolean customFont) {
        if (customFont) {
            this.clientFont.drawString(matrixStack, text, x, y, FontRenderer.FontType.NORMAL, color);
        } else {
            FontHelper.mc.field_1772.method_27522(text, x, y, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public void drawCenteredString(class_4587 matrixStack, String string, float x, float y, int color, boolean customFont, boolean shadow) {
        float newX = x - this.getStringWidth(string, customFont) / 2.0f;
        if (customFont) {
            this.clientFont.drawString(matrixStack, string, newX, y, shadow ? FontRenderer.FontType.SHADOW_THIN : FontRenderer.FontType.NORMAL, color);
        } else {
            FontHelper.mc.field_1772.method_27522(string, newX + 0.5f, y + 0.5f, -16777216, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
            FontHelper.mc.field_1772.method_27522(string, newX, y, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
        }
    }

    public void drawWithShadow(class_4587 matrixStack, class_2561 text, float x, float y, int color) {
        String s = text.getString();
        this.draw(matrixStack, s, x + 0.5f, y + 0.5f, -16777216, true);
        this.draw(matrixStack, s, x, y, color, true);
    }

    public void draw(class_4587 matrixStack, class_2561 text, float x, float y, int color) {
        FontHelper.mc.field_1772.method_27522(String.valueOf((Object)text), x, y, color, true, matrixStack.method_23760().method_23761(), (class_4597)mc.method_22940().method_23000(), class_327.class_6415.field_33994, 0, -1, false);
    }

    public void drawCenteredString(class_4587 matrixStack, class_2561 string, float x, float y, int color) {
        float newX = x - this.getStringWidth(string) / 2.0f;
        this.drawWithShadow(matrixStack, string, newX, y, color);
    }

    public String fix(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        for (int i = 0; i < 9; ++i) {
            if (!s.contains((CharSequence)("\u00a7" + i))) continue;
            s = s.replace((CharSequence)("\u00a7" + i), (CharSequence)"");
        }
        return s.replace((CharSequence)"\u00a7a", (CharSequence)"").replace((CharSequence)"\u00a7b", (CharSequence)"").replace((CharSequence)"\u00a7c", (CharSequence)"").replace((CharSequence)"\u00a7d", (CharSequence)"").replace((CharSequence)"\u00a7e", (CharSequence)"").replace((CharSequence)"\u00a7f", (CharSequence)"").replace((CharSequence)"\u00a7g", (CharSequence)"");
    }

    public boolean setClientFont(FontRenderer font) {
        try {
            this.clientFont = font;
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public FontRenderer getClientFont() {
        return this.clientFont;
    }

    static {
        mc = class_310.method_1551();
    }
}
