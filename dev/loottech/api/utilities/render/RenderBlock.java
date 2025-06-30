package dev.loottech.api.utilities.render;

import net.minecraft.class_2338;

public class RenderBlock {
    public class_2338 pos;
    public long startTime;

    public RenderBlock(class_2338 pos) {
        this.pos = pos;
        this.startTime = System.currentTimeMillis();
    }
}
