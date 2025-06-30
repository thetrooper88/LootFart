package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Util;
import dev.loottech.client.events.PacketSendEvent;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_3532;

public class PositionManager
implements Util,
EventListener {
    private double x;
    private double y;
    private double z;
    private class_2338 blockPos;
    private boolean sneaking;
    private boolean sprinting;
    private boolean onGround;

    public void setPosition(class_243 vec3d) {
        this.setPosition(vec3d.method_10216(), vec3d.method_10214(), vec3d.method_10215());
    }

    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.setPositionClient(x, y, z);
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2828.class_2830(x, y, z, yaw, pitch, this.isOnGround()));
    }

    public void setPosition(double x, double y, double z) {
        this.setPositionClient(x, y, z);
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2828.class_2829(x, y, z, this.isOnGround()));
    }

    public void setPositionClient(double x, double y, double z) {
        if (PositionManager.mc.field_1724.method_3144()) {
            PositionManager.mc.field_1724.method_5854().method_5814(x, y, z);
            return;
        }
        PositionManager.mc.field_1724.method_5814(x, y, z);
    }

    public void setPositionXZ(double x, double z) {
        this.setPosition(x, this.y, z);
    }

    public void setPositionY(double y) {
        this.setPosition(this.x, y, this.z);
    }

    public class_243 getPos() {
        return new class_243(this.getX(), this.getY(), this.getZ());
    }

    public class_243 getEyePos() {
        return this.getPos().method_1031(0.0, (double)PositionManager.mc.field_1724.method_5751(), 0.0);
    }

    public final class_243 getCameraPosVec(float tickDelta) {
        double d = class_3532.method_16436((double)tickDelta, (double)PositionManager.mc.field_1724.field_6014, (double)this.getX());
        double e = class_3532.method_16436((double)tickDelta, (double)PositionManager.mc.field_1724.field_6036, (double)this.getY()) + (double)PositionManager.mc.field_1724.method_5751();
        double f = class_3532.method_16436((double)tickDelta, (double)PositionManager.mc.field_1724.field_5969, (double)this.getZ());
        return new class_243(d, e, f);
    }

    public double squaredDistanceTo(class_1297 entity) {
        float f = (float)(this.getX() - entity.method_23317());
        float g = (float)(this.getY() - entity.method_23318());
        float h = (float)(this.getZ() - entity.method_23321());
        return class_3532.method_41190((double)f, (double)g, (double)h);
    }

    public double squaredReachDistanceTo(class_1297 entity) {
        class_243 cam = this.getCameraPosVec(1.0f);
        float f = (float)(cam.method_10216() - entity.method_23317());
        float g = (float)(cam.method_10214() - entity.method_23318());
        float h = (float)(cam.method_10215() - entity.method_23321());
        return class_3532.method_41190((double)f, (double)g, (double)h);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (PositionManager.mc.field_1724 != null && PositionManager.mc.field_1687 != null) {
            class_2596<?> class_25962 = event.getPacket();
            if (class_25962 instanceof class_2828) {
                class_2828 packet = (class_2828)class_25962;
                this.onGround = packet.method_12273();
                if (packet.method_36171()) {
                    this.x = packet.method_12269(this.x);
                    this.y = packet.method_12268(this.y);
                    this.z = packet.method_12274(this.z);
                    this.blockPos = class_2338.method_49637((double)this.x, (double)this.y, (double)this.z);
                }
            } else {
                class_25962 = event.getPacket();
                if (class_25962 instanceof class_2848) {
                    class_2848 packet = (class_2848)class_25962;
                    switch (packet.method_12365()) {
                        case field_12981: {
                            this.sprinting = true;
                            break;
                        }
                        case field_12985: {
                            this.sprinting = false;
                            break;
                        }
                        case field_12979: {
                            this.sneaking = true;
                            break;
                        }
                        case field_12984: {
                            this.sneaking = false;
                        }
                    }
                }
            }
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public class_2338 getBlockPos() {
        return this.blockPos;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public boolean isOnGround() {
        return this.onGround;
    }
}
