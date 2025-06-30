package dev.loottech.api.utilities;

import net.minecraft.class_1297;
import net.minecraft.class_2338;

public class EntityUtils {
    public static class_2338 getRoundedBlockPos(class_1297 entity) {
        return new class_2338(entity.method_31477(), (int)Math.round((double)entity.method_23318()), entity.method_31479());
    }
}
