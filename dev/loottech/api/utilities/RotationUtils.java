package dev.loottech.api.utilities;

import dev.loottech.api.utilities.IMinecraft;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public class RotationUtils
implements IMinecraft {
    private static float c;
    private static float b;

    public static float[] getRotations(class_243 pos) {
        return RotationUtils.getRotations(pos.field_1352, pos.field_1351, pos.field_1350);
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        class_746 player = RotationUtils.mc.field_1724;
        double x = posX - player.method_23317();
        double y = posY - (player.method_23318() + (double)player.method_18381(player.method_18376()));
        double z = posZ - player.method_23321();
        double dist = class_3532.method_15355((float)((float)(x * x + z * z)));
        float yaw = (float)(Math.atan2((double)z, (double)x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2((double)y, (double)dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsEntity(class_1297 entity) {
        return RotationUtils.getRotations(entity.method_23317(), entity.method_23320(), entity.method_23321());
    }

    public static class_243 getVecFromRotation(float yaw, float pitch) {
        float yawRad = (float)Math.toRadians((double)(-yaw));
        float pitchRad = (float)Math.toRadians((double)(-pitch));
        float xzLen = class_3532.method_15362((float)pitchRad);
        float x = class_3532.method_15374((float)yawRad) * xzLen;
        float y = class_3532.method_15374((float)pitchRad);
        float z = class_3532.method_15362((float)yawRad) * xzLen;
        return new class_243((double)x, (double)y, (double)z).method_1029();
    }

    public static class_243 getVecFromRotation(float[] rotations) {
        return RotationUtils.getVecFromRotation(rotations[0], rotations[1]);
    }

    public static class_243 getLookVec() {
        return RotationUtils.getVecFromRotation(RotationUtils.mc.field_1724.method_36454(), RotationUtils.mc.field_1724.method_36455());
    }

    public static float todeffyaw(float yaw) {
        float yawn = yaw;
        if (yawn < 0.0f) {
            yawn *= -1.0f;
            yawn += 180.0f;
        } else {
            yawn = 180.0f - yaw;
        }
        if (yawn < 0.0f) {
            yawn *= -1.0f;
            yawn %= 360.0f;
            yawn = 360.0f - yawn;
        }
        return yawn % 360.0f;
    }

    public static float tomineyaw(float yaw) {
        if ((yaw = RotationUtils.mody(yaw)) < 0.0f) {
            yaw = 360.0f - yaw * -1.0f;
        }
        float yawn = yaw;
        if (yaw <= 180.0f) {
            yawn = 180.0f - yaw;
        } else {
            yawn = yaw - 180.0f;
            yawn *= -1.0f;
        }
        return yawn % 360.0f;
    }

    public static class_243 getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static float getRotToPos(double PosX, double PosZ) {
        float deltaX = (float)(PosX - RotationUtils.mc.field_1724.method_23317());
        float deltaZ = (float)(PosZ - RotationUtils.mc.field_1724.method_23321());
        float distance = (float)(Math.sqrt((double)Math.pow((double)deltaX, (double)2.0)) + Math.sqrt((double)Math.pow((double)deltaZ, (double)2.0)));
        float yaw = (float)Math.toDegrees((double)(-Math.atan((double)(deltaX / deltaZ))));
        if (deltaX < 0.0f && deltaZ < 0.0f) {
            yaw = (float)(90.0 + Math.toDegrees((double)Math.atan((double)(deltaZ / deltaX))));
        } else if (deltaX > 0.0f && deltaZ < 0.0f) {
            yaw = (float)(-90.0 + Math.toDegrees((double)Math.atan((double)(deltaZ / deltaX))));
        }
        if (yaw < -360.0f) {
            yaw += 360.0f;
        }
        if (yaw > 360.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }

    public static float pitchdiff(float pitch1, float pitch2) {
        return Math.abs((float)(pitch1 - pitch2));
    }

    public static float yawdiff(float yaw1, float yaw2) {
        return RotationUtils.yawdeffdiff(RotationUtils.todeffyaw(yaw1), RotationUtils.todeffyaw(yaw2));
    }

    public static float yawdeffdiff(float yaw1, float yaw2) {
        if ((yaw1 = RotationUtils.mody(yaw1)) < 0.0f) {
            yaw1 = 360.0f - yaw1 * -1.0f;
        }
        if ((yaw2 = RotationUtils.mody(yaw2)) < 0.0f) {
            yaw2 = 360.0f - yaw2 * -1.0f;
        }
        float ans1 = Math.abs((float)(yaw1 - yaw2));
        float fyawto360 = Math.min((float)(360.0f - yaw1), (float)yaw1);
        float syawto360 = Math.min((float)(360.0f - yaw2), (float)yaw2);
        float ans2 = Math.abs((float)(fyawto360 + syawto360));
        float ans = Math.min((float)ans1, (float)ans2);
        return ans;
    }

    public static float addpitch(float pitch1, float pitch2, float value, boolean over) {
        float absvalue = Math.abs((float)value);
        pitch1 = pitch1 > pitch2 ? (over && pitch1 - value < pitch2 ? pitch2 : (pitch1 -= value)) : (over && pitch1 + value > pitch2 ? pitch2 : (pitch1 += value));
        return pitch1;
    }

    public static float addyaw(float yaw1, float yaw2, float value, boolean over) {
        return RotationUtils.tomineyaw(RotationUtils.adddeffyaw(RotationUtils.todeffyaw(yaw1), RotationUtils.todeffyaw(yaw2), value, over));
    }

    public static float addyaw(float yaw1, float value) {
        return RotationUtils.tomineyaw(RotationUtils.adddeffyaw(RotationUtils.todeffyaw(yaw1), value));
    }

    public static float addyawWithDeffArg(float yaw1, float yaw2, float value, boolean over) {
        return RotationUtils.tomineyaw(RotationUtils.adddeffyaw(yaw1, yaw2, value, over));
    }

    public static float addyawWithDeffArg(float yaw1, float value) {
        return RotationUtils.tomineyaw(RotationUtils.adddeffyaw(yaw1, value));
    }

    public static float adddeffyaw(float yaw1, float yaw2, float value, boolean over) {
        float ans2;
        if ((yaw1 = RotationUtils.mody(yaw1)) < 0.0f) {
            yaw1 = 360.0f - yaw1 * -1.0f;
        }
        if ((yaw2 = RotationUtils.mody(yaw2)) < 0.0f) {
            yaw2 = 360.0f - yaw2 * -1.0f;
        }
        value = RotationUtils.mody(value);
        if (over && RotationUtils.yawdeffdiff(yaw1, yaw2) <= value) {
            return yaw2;
        }
        if (yaw1 >= yaw2) {
            float ans1 = Math.abs((float)(yaw1 - yaw2));
            float fyawto360 = 360.0f - yaw1;
            if (yaw1 <= 180.0f) {
                fyawto360 = yaw1;
            }
            float syawto360 = 360.0f - yaw2;
            if (yaw2 <= 180.0f) {
                syawto360 = yaw2;
            }
            float ans22 = Math.abs((float)(fyawto360 + syawto360));
            float ans = 0.0f;
            if (ans1 <= ans22) {
                ans = yaw1 - value;
                if ((ans = RotationUtils.mody(ans)) < 0.0f) {
                    ans = 360.0f - ans * -1.0f;
                }
                return ans;
            }
            ans = yaw1 + value;
            if ((ans = RotationUtils.mody(ans)) < 0.0f) {
                ans = 360.0f - ans * -1.0f;
            }
            return ans;
        }
        float ans1 = Math.abs((float)(yaw1 - yaw2));
        float fyawto360 = 360.0f - yaw1;
        if (yaw1 <= 180.0f) {
            fyawto360 = yaw1;
        }
        float syawto360 = 360.0f - yaw2;
        if (yaw2 <= 180.0f) {
            syawto360 = yaw2;
        }
        if (ans1 < (ans2 = Math.abs((float)(fyawto360 + syawto360)))) {
            float ans = yaw1 + value;
            if ((ans = RotationUtils.mody(ans)) < 0.0f) {
                ans = 360.0f - ans * -1.0f;
            }
            return ans;
        }
        float ans = yaw1 - value;
        if ((ans = RotationUtils.mody(ans)) < 0.0f) {
            ans *= -1.0f;
            ans = 360.0f - ans;
        }
        return ans;
    }

    public static float adddeffyaw(float yaw1, float value) {
        if ((yaw1 = RotationUtils.mody(yaw1)) < 0.0f) {
            yaw1 = 360.0f - yaw1 * -1.0f;
        }
        value = RotationUtils.mody(value);
        float ans = yaw1 + value;
        if ((ans = RotationUtils.mody(ans)) < 0.0f) {
            ans = 360.0f - ans * -1.0f;
        }
        return ans % 360.0f;
    }

    public static float mody(float yaw) {
        boolean min = yaw < 0.0f;
        float ans = yaw % 360.0f;
        if (min) {
            return ans;
        }
        return ans;
    }
}
