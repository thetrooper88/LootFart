package dev.loottech.client.modules.miscellaneous;

import dev.loottech.client.modules.miscellaneous.AutoSpawner;
import net.minecraft.class_2338;

private static class AutoSpawner.BlockPlacement {
    public final class_2338 pos;
    public final int slot;
    public final AutoSpawner.ItemType itemType;

    public AutoSpawner.BlockPlacement(class_2338 pos, int slot, AutoSpawner.ItemType itemType) {
        this.pos = pos;
        this.slot = slot;
        this.itemType = itemType;
    }
}
