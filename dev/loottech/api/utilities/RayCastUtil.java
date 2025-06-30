package dev.loottech.api.utilities;

import dev.loottech.api.utilities.RotationUtils;
import dev.loottech.api.utilities.Util;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_4184;

public final class RayCastUtil {
    public static class_239 raycastEntity(double reach) {
        class_4184 view = Util.mc.field_1773.method_19418();
        class_243 vec3d = view.method_19326();
        class_243 vec3d2 = RotationUtils.getVecFromRotation(view.method_19329(), view.method_19330());
        class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * reach, vec3d2.field_1351 * reach, vec3d2.field_1350 * reach);
        class_238 box = view.method_19331().method_5829().method_18804(vec3d2.method_1021(reach)).method_1009(1.0, 1.0, 1.0);
        return class_1675.method_18075((class_1297)view.method_19331(), (class_243)vec3d, (class_243)vec3d3, (class_238)box, entity -> !entity.method_7325() && entity.method_5863(), (double)(reach * reach));
    }

    public static class_239 rayCast(double reach, class_243 position, float[] angles) {
        if (Float.isNaN((float)angles[0]) || Float.isNaN((float)angles[1])) {
            return null;
        }
        class_243 rotationVector = RotationUtils.getVecFromRotation(angles[1], angles[0]);
        return Util.mc.field_1687.method_17742(new class_3959(position, position.method_1031(rotationVector.field_1352 * reach, rotationVector.field_1351 * reach, rotationVector.field_1350 * reach), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)Util.mc.field_1724));
    }
}
