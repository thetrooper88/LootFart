package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_243;

public class CameraPositionEvent
extends EventArgument {
    private double x;
    private double y;
    private double z;
    private final float tickDelta;

    public CameraPositionEvent(double x, double y, double z, float tickDelta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tickDelta = tickDelta;
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

    public float getTickDelta() {
        return this.tickDelta;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setPosition(class_243 pos) {
        this.x = pos.method_10216();
        this.y = pos.method_10214();
        this.z = pos.method_10215();
    }

    public class_243 getPosition() {
        return new class_243(this.x, this.y, this.z);
    }

    @Override
    public void call(EventListener listener) {
        listener.onCameraPosition(this);
    }
}
