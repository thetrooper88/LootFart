package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_243;
import net.minecraft.class_3414;
import net.minecraft.class_3419;

public class PlaySoundEvent
extends EventArgument {
    private final class_243 pos;
    private final class_3414 event;
    private final class_3419 category;

    public PlaySoundEvent(class_243 pos, class_3414 event, class_3419 category) {
        this.pos = pos;
        this.event = event;
        this.category = category;
    }

    public class_243 getPos() {
        return this.pos;
    }

    public class_3414 getSoundEvent() {
        return this.event;
    }

    public class_3419 getCategory() {
        return this.category;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPlaySound(this);
    }
}
