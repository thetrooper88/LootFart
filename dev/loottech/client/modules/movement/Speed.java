package dev.loottech.client.modules.movement;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.entity.FakePlayerEntity;
import dev.loottech.client.values.impl.ValueEnum;
import dev.loottech.client.values.impl.ValueNumber;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;

@RegisterModule(name="Speed", description="Make you go zzoooooommmm.", category=Module.Category.MOVEMENT)
public class Speed
extends Module {
    private final ValueEnum mode = new ValueEnum("Mode", "Mode", "What type of speed", (Enum)Modes.Vanilla);
    private final ValueNumber speed = new ValueNumber("Speed", "Speed", "how fast to zooooooom.", (Number)Float.valueOf((float)1.0f), (Number)Float.valueOf((float)0.1f), (Number)Float.valueOf((float)10.0f));

    @Override
    public void onTick() {
        class_243 moveVec;
        if (Speed.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals((Object)Modes.Vanilla)) {
            if (Speed.mc.field_1690.field_1894.method_1434() || Speed.mc.field_1690.field_1913.method_1434() || Speed.mc.field_1690.field_1849.method_1434() || Speed.mc.field_1690.field_1881.method_1434()) {
                moveVec = class_243.field_1353;
                if (Speed.mc.field_1690.field_1894.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)Speed.mc.field_1724.method_36454()).method_1029());
                }
                if (Speed.mc.field_1690.field_1881.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 180.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1849.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 90.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1913.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() - 90.0f)).method_1029());
                }
                moveVec = moveVec.method_1029().method_1021(this.speed.getValue().doubleValue());
                Speed.mc.field_1724.method_18800(moveVec.field_1352, Speed.mc.field_1724.method_18798().field_1351, moveVec.field_1350);
            } else {
                Speed.mc.field_1724.method_18800(0.0, Speed.mc.field_1724.method_18798().field_1351, 0.0);
            }
        }
        if (this.mode.getValue().equals((Object)Modes.Strafe)) {
            if (Speed.mc.field_1690.field_1894.method_1434() || Speed.mc.field_1690.field_1913.method_1434() || Speed.mc.field_1690.field_1849.method_1434() || Speed.mc.field_1690.field_1881.method_1434()) {
                moveVec = class_243.field_1353;
                if (Speed.mc.field_1690.field_1894.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)Speed.mc.field_1724.method_36454()).method_1029());
                }
                if (Speed.mc.field_1690.field_1881.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 180.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1849.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 90.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1913.method_1434()) {
                    moveVec = moveVec.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() - 90.0f)).method_1029());
                }
                moveVec = moveVec.method_1029().method_1021(this.speed.getValue().doubleValue() / 3.0);
                Speed.mc.field_1724.method_18800(moveVec.field_1352, Speed.mc.field_1724.method_18798().field_1351, moveVec.field_1350);
                if (Speed.mc.field_1724.method_24828()) {
                    Speed.mc.field_1724.method_6043();
                }
            } else {
                Speed.mc.field_1724.method_18800(0.0, Speed.mc.field_1724.method_18798().field_1351, 0.0);
            }
        }
        if (this.mode.getValue().equals((Object)Modes.Grim)) {
            int collisions = 0;
            for (class_1297 entity : Speed.mc.field_1687.method_18112()) {
                if (!this.checkIsCollidingEntity(entity) || !((double)class_3532.method_15355((float)((float)Speed.mc.field_1724.method_5858(entity))) <= 1.5)) continue;
                ++collisions;
            }
            if (collisions > 0) {
                class_243 velocity = Speed.mc.field_1724.method_18798();
                double factor = 0.08 * (double)collisions;
                class_241 strafe = this.handleStrafeMotion((float)factor);
                Speed.mc.field_1724.method_18800(velocity.field_1352 + (double)strafe.field_1343, velocity.field_1351, velocity.field_1350 + (double)strafe.field_1342);
            }
        }
        if (this.mode.getValue().equals((Object)Modes.LowHop)) {
            if (Speed.mc.field_1690.field_1894.method_1434() || Speed.mc.field_1690.field_1913.method_1434() || Speed.mc.field_1690.field_1849.method_1434() || Speed.mc.field_1690.field_1881.method_1434()) {
                class_243 moveVec2 = class_243.field_1353;
                if (Speed.mc.field_1690.field_1894.method_1434()) {
                    moveVec2 = moveVec2.method_1019(class_243.method_1030((float)0.0f, (float)Speed.mc.field_1724.method_36454()).method_1029());
                }
                if (Speed.mc.field_1690.field_1881.method_1434()) {
                    moveVec2 = moveVec2.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 180.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1849.method_1434()) {
                    moveVec2 = moveVec2.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() + 90.0f)).method_1029());
                }
                if (Speed.mc.field_1690.field_1913.method_1434()) {
                    moveVec2 = moveVec2.method_1019(class_243.method_1030((float)0.0f, (float)(Speed.mc.field_1724.method_36454() - 90.0f)).method_1029());
                }
                moveVec2 = moveVec2.method_1029().method_1021(this.speed.getValue().doubleValue());
                Speed.mc.field_1724.method_18800(moveVec2.field_1352, Speed.mc.field_1724.method_18798().field_1351 - 0.05, moveVec2.field_1350);
                if (Speed.mc.field_1724.method_24828()) {
                    Speed.mc.field_1724.method_6043();
                }
            } else {
                Speed.mc.field_1724.method_18800(0.0, Speed.mc.field_1724.method_18798().field_1351, 0.0);
            }
        }
    }

    public boolean checkIsCollidingEntity(class_1297 entity) {
        return entity != null && entity != Speed.mc.field_1724 && entity instanceof class_1309 && !(entity instanceof FakePlayerEntity) && !(entity instanceof class_1531);
    }

    public class_241 handleStrafeMotion(float speed) {
        float forward = Speed.mc.field_1724.field_3913.field_3905;
        float strafe = Speed.mc.field_1724.field_3913.field_3907;
        float yaw = Speed.mc.field_1724.field_5982 + (Speed.mc.field_1724.method_36454() - Speed.mc.field_1724.field_5982);
        if (forward == 0.0f && strafe == 0.0f) {
            return class_241.field_1340;
        }
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += forward > 0.0f ? -45.0f : 45.0f;
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += forward > 0.0f ? 45.0f : -45.0f;
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        float rx = (float)Math.cos((double)Math.toRadians((double)yaw));
        float rz = (float)(-Math.sin((double)Math.toRadians((double)yaw)));
        return new class_241(forward * speed * rz + strafe * speed * rx, forward * speed * rx - strafe * speed * rz);
    }

    private static enum Modes {
        Vanilla,
        Strafe,
        Grim,
        LowHop;

    }
}
