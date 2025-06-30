package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;

public class DirectionUtils {
    public static class_2350 getDirection(class_2338 pos) {
        class_243 eyesPos = new class_243(IMinecraft.mc.field_1724.method_23317(), IMinecraft.mc.field_1724.method_23318() + (double)IMinecraft.mc.field_1724.method_18381(IMinecraft.mc.field_1724.method_18376()), IMinecraft.mc.field_1724.method_23321());
        if ((double)pos.method_10264() > eyesPos.field_1351) {
            if (IMinecraft.mc.field_1687.method_8320(pos.method_10069(0, -1, 0)).method_45474()) {
                return class_2350.field_11033;
            }
            return IMinecraft.mc.field_1724.method_5735().method_10153();
        }
        if (!IMinecraft.mc.field_1687.method_8320(pos.method_10069(0, 1, 0)).method_45474()) {
            return IMinecraft.mc.field_1724.method_5735().method_10153();
        }
        return class_2350.field_11036;
    }

    public static enum EightWayDirections {
        NORTH(class_2350.field_11043),
        SOUTH(class_2350.field_11035),
        EAST(class_2350.field_11034),
        WEST(class_2350.field_11039),
        NORTHEAST(class_2350.field_11043, class_2350.field_11034),
        NORTHWEST(class_2350.field_11043, class_2350.field_11039),
        SOUTHEAST(class_2350.field_11035, class_2350.field_11034),
        SOUTHWEST(class_2350.field_11035, class_2350.field_11039);

        private final List<class_2350> directions;

        private EightWayDirections(class_2350 ... directions) {
            this.directions = Arrays.asList((Object[])directions);
        }

        public class_2338 offset(class_2338 pos) {
            class_2338 result = pos;
            for (class_2350 direction : this.directions) {
                result = result.method_10093(direction);
            }
            return result;
        }
    }
}
