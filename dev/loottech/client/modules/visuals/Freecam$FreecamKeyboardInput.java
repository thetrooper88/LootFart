package dev.loottech.client.modules.visuals;

import net.minecraft.class_241;
import net.minecraft.class_315;
import net.minecraft.class_743;

public class Freecam.FreecamKeyboardInput
extends class_743 {
    private final class_315 options;

    public Freecam.FreecamKeyboardInput(class_315 options) {
        super(options);
        this.options = options;
    }

    public void method_3129(boolean slowDown, float slowDownFactor) {
        if (Freecam.this.control) {
            super.method_3129(slowDown, slowDownFactor);
        } else {
            this.unset();
            float speed = Freecam.this.motionSpeed.getValue().floatValue() / 10.0f;
            float fakeMovementForward = Freecam.this.getMovementMultiplier(this.options.field_1894.method_1434(), this.options.field_1881.method_1434());
            float fakeMovementSideways = Freecam.this.getMovementMultiplier(this.options.field_1913.method_1434(), this.options.field_1849.method_1434());
            class_241 dir = Freecam.this.handleVanillaMotion(speed, fakeMovementForward, fakeMovementSideways);
            float y = 0.0f;
            if (this.options.field_1903.method_1434()) {
                y += speed;
            } else if (this.options.field_1832.method_1434()) {
                y -= speed;
            }
            Freecam.this.lastPosition = Freecam.this.position;
            Freecam.this.position = Freecam.this.position.method_1031((double)dir.field_1343, (double)y, (double)dir.field_1342);
        }
    }

    private void unset() {
        this.field_3910 = false;
        this.field_3909 = false;
        this.field_3908 = false;
        this.field_3906 = false;
        this.field_3905 = 0.0f;
        this.field_3907 = 0.0f;
        this.field_3904 = false;
        this.field_3903 = false;
    }
}
