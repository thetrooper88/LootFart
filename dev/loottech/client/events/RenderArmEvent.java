package dev.loottech.client.events;

import dev.loottech.api.manager.event.EventArgument;
import dev.loottech.api.manager.event.EventListener;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4597;

public class RenderArmEvent
extends EventArgument {
    public class_1268 hand;
    public class_1799 item;
    public float equipProgress;
    public class_4587 matrices;
    public class_4597 vertexConsumers;

    public RenderArmEvent(class_1268 hand, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers) {
        this.hand = hand;
        this.item = item;
        this.equipProgress = equipProgress;
        this.matrices = matrices;
        this.vertexConsumers = vertexConsumers;
    }

    @Override
    public void call(EventListener listener) {
        listener.onRenderArm(this);
    }

    public static class Head
    extends RenderArmEvent {
        public Head(class_1268 hand, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers) {
            super(hand, item, equipProgress, matrices, vertexConsumers);
        }
    }
}
