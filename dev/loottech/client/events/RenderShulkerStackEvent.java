package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1799;

public class RenderShulkerStackEvent
extends EventArgument {
    public class_1799 stack;
    public int x;
    public int y;
    public String countLabel;

    @Override
    public void call(EventListener listener) {
        listener.onRenderShulkerStack(this);
    }
}
