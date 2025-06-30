package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_2596;

public class PacketReceiveEvent
extends EventArgument {
    private final class_2596<?> packet;

    public PacketReceiveEvent(class_2596<?> packet) {
        this.packet = packet;
    }

    public class_2596<?> getPacket() {
        return this.packet;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPacketReceive(this);
    }
}
