package dev.loottech.client.events;

import dev.loottech.client.events.RenderArmEvent;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4597;

public static class RenderArmEvent.Head
extends RenderArmEvent {
    public RenderArmEvent.Head(class_1268 hand, class_1799 item, float equipProgress, class_4587 matrices, class_4597 vertexConsumers) {
        super(hand, item, equipProgress, matrices, vertexConsumers);
    }
}
