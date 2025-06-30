package dev.loottech.api.utilities;

import net.minecraft.class_243;

public record SetbackData(class_243 position, long timeMS, int teleportID) {
    public long timeSince() {
        return System.currentTimeMillis() - this.timeMS;
    }
}
