package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class MovementPacketsEvent
extends EventArgument {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public MovementPacketsEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
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

    public boolean isOnGround() {
        return this.onGround;
    }

    public float getRotationYaw() {
        return this.yaw;
    }

    public float getRotationPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRotationAngles(float[] angles) {
        this.yaw = angles[0];
        this.pitch = angles[1];
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void call(EventListener listener) {
        listener.onMovementPackets(this);
    }
}
