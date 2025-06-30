package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1657;

public class TotemPopEvent
extends EventArgument {
    private final class_1657 player;
    private final int pops;

    public TotemPopEvent(class_1657 player, int pops) {
        this.player = player;
        this.pops = pops;
    }

    @Override
    public void call(EventListener listener) {
        listener.onTotemPop(this);
    }

    public class_1657 getPlayer() {
        return this.player;
    }

    public int getPops() {
        return this.pops;
    }
}
