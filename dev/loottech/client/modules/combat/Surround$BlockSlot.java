package dev.loottech.client.modules.combat;

import net.minecraft.class_2248;

public record Surround.BlockSlot(class_2248 block, int slot) {
    public boolean equals(Object obj) {
        Surround.BlockSlot b;
        return obj instanceof Surround.BlockSlot && (b = (Surround.BlockSlot)((Object)obj)).block() == this.block;
    }
}
