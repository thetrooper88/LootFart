package dev.loottech.api.utilities.render;

import net.minecraft.class_1657;

public class RenderPlayer {
    public class_1657 player;
    public long startTime;

    public RenderPlayer(class_1657 player) {
        this.player = player;
        this.startTime = System.currentTimeMillis();
    }
}
