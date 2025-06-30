package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_241;

public class CameraRotationEvent
extends EventArgument {
    private float yaw;
    private float pitch;
    private final float tickDelta;

    public CameraRotationEvent(float yaw, float pitch, float tickDelta) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.tickDelta = tickDelta;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRotation(class_241 rotation) {
        this.yaw = rotation.field_1343;
        this.pitch = rotation.field_1342;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }

    @Override
    public void call(EventListener listener) {
        listener.onCameraRotation(this);
    }
}
