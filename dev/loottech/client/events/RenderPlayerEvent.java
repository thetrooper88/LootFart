package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_742;

public class RenderPlayerEvent
extends EventArgument {
    private final class_742 entity;
    private float yaw;
    private float pitch;

    @Override
    public void call(EventListener listener) {
        listener.onRenderPlayer(this);
    }

    public RenderPlayerEvent(class_742 entity) {
        this.entity = entity;
    }

    public class_742 getEntity() {
        return this.entity;
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
}
