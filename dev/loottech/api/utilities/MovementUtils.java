package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import java.util.Objects;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1657;

public class MovementUtils
implements IMinecraft {
    public static boolean isMoving(class_1657 player) {
        return player.field_6212 != 0.0f || player.field_6250 != 0.0f;
    }

    public static boolean isMovingInput() {
        return MovementUtils.mc.field_1724.field_3913.field_3905 != 0.0f || MovementUtils.mc.field_1724.field_3913.field_3907 != 0.0f;
    }

    public static double getBaseMoveSpeed(double speed) {
        double baseSpeed = speed;
        if (MovementUtils.mc.field_1724 != null && MovementUtils.mc.field_1724.method_6059(class_1294.field_5904)) {
            int amplifier = ((class_1293)Objects.requireNonNull((Object)MovementUtils.mc.field_1724.method_6112(class_1294.field_5904))).method_5578();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getJumpBoost() {
        double defaultSpeed = 0.0;
        if (MovementUtils.mc.field_1724.method_6059(class_1294.field_5913)) {
            int amplifier = MovementUtils.mc.field_1724.method_6112(class_1294.field_5913).method_5578();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }
}
