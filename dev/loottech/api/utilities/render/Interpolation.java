package dev.loottech.api.utilities.render;

import dev.loottech.api.utilities.Util;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class Interpolation
implements Util {
    public static class_243 getRenderPosition(class_243 pos, class_243 lastPos, float tickDelta) {
        return new class_243(pos.field_1352 - class_3532.method_16436((double)tickDelta, (double)lastPos.field_1352, (double)pos.field_1352), pos.field_1351 - class_3532.method_16436((double)tickDelta, (double)lastPos.field_1351, (double)pos.field_1351), pos.field_1350 - class_3532.method_16436((double)tickDelta, (double)lastPos.field_1350, (double)pos.field_1350));
    }

    public static class_243 getRenderPosition(class_1297 entity, float tickDelta) {
        return new class_243(entity.method_23317() - class_3532.method_16436((double)tickDelta, (double)entity.field_6038, (double)entity.method_23317()), entity.method_23318() - class_3532.method_16436((double)tickDelta, (double)entity.field_5971, (double)entity.method_23318()), entity.method_23321() - class_3532.method_16436((double)tickDelta, (double)entity.field_5989, (double)entity.method_23321()));
    }

    public static class_243 getInterpolatedPosition(class_1297 entity, float tickDelta) {
        return new class_243(entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)tickDelta, entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)tickDelta, entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)tickDelta);
    }

    public static float interpolateFloat(float prev, float value, float factor) {
        return prev + (value - prev) * factor;
    }

    public static double interpolateDouble(double prev, double value, double factor) {
        return prev + (value - prev) * factor;
    }

    public static class_238 getInterpolatedBox(class_238 prevBox, class_238 box) {
        double delta = mc.method_1493() ? 1.0 : (double)mc.method_60646().method_60637(true);
        return new class_238(Interpolation.interpolateDouble(prevBox.field_1323, box.field_1323, delta), Interpolation.interpolateDouble(prevBox.field_1322, box.field_1322, delta), Interpolation.interpolateDouble(prevBox.field_1321, box.field_1321, delta), Interpolation.interpolateDouble(prevBox.field_1320, box.field_1320, delta), Interpolation.interpolateDouble(prevBox.field_1325, box.field_1325, delta), Interpolation.interpolateDouble(prevBox.field_1324, box.field_1324, delta));
    }

    public static class_238 getInterpolatedEntityBox(class_1297 entity) {
        class_238 box = entity.method_5829();
        class_238 prevBox = entity.method_5829().method_989(entity.field_6014 - entity.method_23317(), entity.field_6036 - entity.method_23318(), entity.field_5969 - entity.method_23321());
        return Interpolation.getInterpolatedBox(prevBox, box);
    }
}
