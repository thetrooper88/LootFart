package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;

public class JumpRotationEvent
extends EventArgument {
    private float yaw;

    public JumpRotationEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public void call(EventListener listener) {
        listener.onJumpRotation(this);
    }
}
