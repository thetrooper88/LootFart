package dev.loottech.api.utilities.render;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.minecraft.class_8030;

public class ScissorStack {
    private final Deque<class_8030> stack = new ArrayDeque();

    public class_8030 push(class_8030 rect) {
        class_8030 screenRect = (class_8030)this.stack.peekLast();
        if (screenRect != null) {
            class_8030 screenRect2 = (class_8030)Objects.requireNonNullElse((Object)rect.method_49701(screenRect), (Object)class_8030.method_48248());
            this.stack.addLast((Object)screenRect2);
            return screenRect2;
        }
        this.stack.addLast((Object)rect);
        return rect;
    }

    public class_8030 pop() {
        if (this.stack.isEmpty()) {
            throw new IllegalStateException("Scissor stack underflow");
        }
        this.stack.removeLast();
        return (class_8030)this.stack.peekLast();
    }
}
