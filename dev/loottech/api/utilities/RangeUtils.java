package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;

public class RangeUtils
implements IMinecraft {
    public static boolean isInRange(class_243 pos, double range) {
        return RangeUtils.getRange(pos) < range;
    }

    public static boolean isInRange(class_2338 pos, double range) {
        return RangeUtils.getRange(pos) < range;
    }

    public static boolean isInRange(class_1297 entity, double range) {
        return RangeUtils.getRange(entity) < range;
    }

    public static double getRange(class_243 pos) {
        class_243 playerPos = RangeUtils.mc.field_1724.method_19538();
        return Math.sqrt((double)(Math.pow((double)Math.abs((double)(pos.field_1352 - playerPos.field_1352)), (double)2.0) + Math.pow((double)Math.abs((double)(pos.field_1351 - playerPos.field_1351)), (double)2.0) + Math.pow((double)Math.abs((double)(pos.field_1350 - playerPos.field_1350)), (double)2.0)));
    }

    public static double getRange(class_2338 pos) {
        class_243 playerPos = RangeUtils.mc.field_1724.method_19538();
        return Math.sqrt((double)(Math.pow((double)Math.abs((double)((double)pos.method_10263() - playerPos.field_1352)), (double)2.0) + Math.pow((double)Math.abs((double)((double)pos.method_10264() - playerPos.field_1351)), (double)2.0) + Math.pow((double)Math.abs((double)((double)pos.method_10260() - playerPos.field_1350)), (double)2.0)));
    }

    public static double getRange(class_1297 entity) {
        class_243 playerPos = RangeUtils.mc.field_1724.method_19538();
        class_243 pos = entity.method_19538();
        return Math.sqrt((double)(Math.pow((double)Math.abs((double)(pos.field_1352 - playerPos.field_1352)), (double)2.0) + Math.pow((double)Math.abs((double)(pos.field_1351 - playerPos.field_1351)), (double)2.0) + Math.pow((double)Math.abs((double)(pos.field_1350 - playerPos.field_1350)), (double)2.0)));
    }
}
