package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_2818;

public class ChunkDataEvent
extends EventArgument {
    public class_2818 chunk;

    public ChunkDataEvent(class_2818 chunk) {
        this.chunk = chunk;
    }

    public class_2818 getChunk() {
        return this.chunk;
    }

    @Override
    public void call(EventListener listener) {
        listener.onChunkData(this);
    }
}
