package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1799;
import net.minecraft.class_332;

public class RenderTooltipEvent
extends EventArgument {
    public final class_332 context;
    private final class_1799 stack;
    private final int x;
    private final int y;

    public RenderTooltipEvent(class_332 context, class_1799 stack, int x, int y) {
        this.context = context;
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public class_332 getContext() {
        return this.context;
    }

    public class_1799 getStack() {
        return this.stack;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderTooltip(this);
    }
}
