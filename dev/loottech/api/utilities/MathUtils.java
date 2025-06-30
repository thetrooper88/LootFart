package dev.loottech.api.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.class_124;
import net.minecraft.class_243;
import net.minecraft.class_2583;
import net.minecraft.class_5481;

public class MathUtils {
    public static double square(double input) {
        return input * input;
    }

    public static float square(float input) {
        return input * input;
    }

    public static double roundToPlaces(double number, int places) {
        BigDecimal decimal = new BigDecimal(number);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static class_243 roundVector(class_243 vec3d, int places) {
        return new class_243(MathUtils.roundToPlaces(vec3d.field_1352, places), MathUtils.roundToPlaces(vec3d.field_1351, places), MathUtils.roundToPlaces(vec3d.field_1350, places));
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((double)((x - x1) * (x - x1) + (y - y1) * (y - y1)));
    }

    public static float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    public static String orderedTextToString(class_5481 orderedText) {
        StringBuilder builder = new StringBuilder();
        class_2583[] lastStyle = new class_2583[]{class_2583.field_24360};
        orderedText.accept((index, style, codePoint) -> {
            if (!style.equals((Object)lastStyle[0])) {
                class_124 formatting;
                if (style.method_10973() != null && !style.method_10973().equals((Object)lastStyle[0].method_10973()) && (formatting = class_124.method_533((String)style.method_10973().method_27721())) != null && formatting.method_543()) {
                    builder.append((Object)formatting);
                }
                if (style.method_10984() && !lastStyle[0].method_10984()) {
                    builder.append((Object)class_124.field_1067);
                }
                if (style.method_10966() && !lastStyle[0].method_10966()) {
                    builder.append((Object)class_124.field_1056);
                }
                if (style.method_10965() && !lastStyle[0].method_10965()) {
                    builder.append((Object)class_124.field_1073);
                }
                if (style.method_10986() && !lastStyle[0].method_10986()) {
                    builder.append((Object)class_124.field_1055);
                }
                if (style.method_10987() && !lastStyle[0].method_10987()) {
                    builder.append((Object)class_124.field_1051);
                }
                if (style.method_10967() && !lastStyle[0].method_10967()) {
                    builder.append((Object)class_124.field_1070);
                }
                lastStyle[0] = style;
            }
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }
}
