package dev.loottech.api.utilities;

import dev.loottech.api.utilities.Util;
import net.minecraft.class_1296;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1421;
import net.minecraft.class_1439;
import net.minecraft.class_1477;
import net.minecraft.class_1493;
import net.minecraft.class_1560;
import net.minecraft.class_1569;
import net.minecraft.class_1590;
import net.minecraft.class_1690;
import net.minecraft.class_1694;
import net.minecraft.class_1695;
import net.minecraft.class_1696;
import net.minecraft.class_2338;
import net.minecraft.class_4466;

public class EntityUtil
implements Util {
    public static class_2338 getRoundedBlockPos(class_1297 entity) {
        return new class_2338(entity.method_31477(), (int)Math.round((double)entity.method_23318()), entity.method_31479());
    }

    public static float getHealth(class_1297 entity) {
        if (entity instanceof class_1309) {
            class_1309 e = (class_1309)entity;
            return e.method_6032() + e.method_6067();
        }
        return 0.0f;
    }

    public static boolean isMonster(class_1297 e) {
        return e instanceof class_1569 && !EntityUtil.isNeutralInternal(e);
    }

    private static boolean isNeutralInternal(class_1297 e) {
        class_4466 bee;
        class_1439 ironGolem;
        class_1493 wolf;
        class_1590 piglin;
        class_1560 enderman;
        return e instanceof class_1560 && !(enderman = (class_1560)e).method_6510() || e instanceof class_1590 && !(piglin = (class_1590)e).method_6510() || e instanceof class_1493 && !(wolf = (class_1493)e).method_6510() || e instanceof class_1439 && !(ironGolem = (class_1439)e).method_6510() || e instanceof class_4466 && !(bee = (class_4466)e).method_6510();
    }

    public static boolean isNeutral(class_1297 e) {
        return e instanceof class_1560 || e instanceof class_1590 || e instanceof class_1493 || e instanceof class_1439;
    }

    public static boolean isPassive(class_1297 e) {
        return e instanceof class_1296 || e instanceof class_1421 || e instanceof class_1477;
    }

    public static boolean isVehicle(class_1297 e) {
        return e instanceof class_1690 || e instanceof class_1695 || e instanceof class_1696 || e instanceof class_1694;
    }
}
