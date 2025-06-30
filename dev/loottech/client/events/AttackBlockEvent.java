package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public class AttackBlockEvent
extends EventArgument {
    private class_2338 pos;
    private class_2350 direction;

    public AttackBlockEvent(class_2338 pos, class_2350 direction) {
        this.pos = pos;
        this.direction = direction;
    }

    @Override
    public void call(EventListener listener) {
        listener.onAttackBlock(this);
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public class_2350 getDirection() {
        return this.direction;
    }
}
