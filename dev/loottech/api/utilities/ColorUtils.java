package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.client.modules.client.ModuleColor;
import java.awt.Color;
import net.minecraft.class_243;

public class ColorUtils
implements IMinecraft {
    public static Color hslToColor(float f, float f2, float f3, float f4) {
        if (f2 < 0.0f || f2 > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
        if (f3 < 0.0f || f3 > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
        }
        if (f4 < 0.0f || f4 > 1.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        }
        f %= 360.0f;
        float f5 = 0.0f;
        f5 = (double)f3 < 0.5 ? f3 * (1.0f + f2) : (f3 /= 100.0f) + (f2 /= 100.0f) - f2 * f3;
        f2 = 2.0f * f3 - f5;
        f3 = Math.max((float)0.0f, (float)ColorUtils.colorCalc(f2, f5, (f /= 360.0f) + 0.33333334f));
        float f6 = Math.max((float)0.0f, (float)ColorUtils.colorCalc(f2, f5, f));
        f2 = Math.max((float)0.0f, (float)ColorUtils.colorCalc(f2, f5, f - 0.33333334f));
        f3 = Math.min((float)f3, (float)1.0f);
        f6 = Math.min((float)f6, (float)1.0f);
        f2 = Math.min((float)f2, (float)1.0f);
        return new Color(f3, f6, f2, f4);
    }

    private static float colorCalc(float f, float f2, float f3) {
        if (f3 < 0.0f) {
            f3 += 1.0f;
        }
        if (f3 > 1.0f) {
            f3 -= 1.0f;
        }
        if (6.0f * f3 < 1.0f) {
            float f4 = f;
            return f4 + (f2 - f4) * 6.0f * f3;
        }
        if (2.0f * f3 < 1.0f) {
            return f2;
        }
        if (3.0f * f3 < 2.0f) {
            float f5 = f;
            return f5 + (f2 - f5) * 6.0f * (0.6666667f - f3);
        }
        return f;
    }

    public static Color wave(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB((int)color.getRed(), (int)color.getGreen(), (int)color.getBlue(), (float[])hsb);
        float brightness = Math.abs((float)(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f));
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB((float)hsb[0], (float)hsb[1], (float)hsb[2]));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double)((double)(System.currentTimeMillis() + (long)(delay * ModuleColor.INSTANCE.rainbowOffset.getValue().intValue())) / 20.0));
        return Color.getHSBColor((float)((float)((rainbowState %= 360.0) / 360.0)), (float)((float)ModuleColor.INSTANCE.rainbowSat.getValue().intValue() / 255.0f), (float)((float)ModuleColor.INSTANCE.rainbowBri.getValue().intValue() / 255.0f));
    }

    public static Color gradient(Color color1, Color color2, int index, int count) {
        double offset = Math.abs((float)(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)(count + 1) * 2.0f) % 2.0f - 1.0f));
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int)offset;
            offset = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static class_243 getVec3d(Color c) {
        return new class_243((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f));
    }
}
