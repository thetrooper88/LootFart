package dev.loottech.api.utilities;

import java.util.Arrays;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public static enum DirectionUtils.EightWayDirections {
    NORTH(class_2350.field_11043),
    SOUTH(class_2350.field_11035),
    EAST(class_2350.field_11034),
    WEST(class_2350.field_11039),
    NORTHEAST(class_2350.field_11043, class_2350.field_11034),
    NORTHWEST(class_2350.field_11043, class_2350.field_11039),
    SOUTHEAST(class_2350.field_11035, class_2350.field_11034),
    SOUTHWEST(class_2350.field_11035, class_2350.field_11039);

    private final List<class_2350> directions;

    private DirectionUtils.EightWayDirections(class_2350 ... directions) {
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
