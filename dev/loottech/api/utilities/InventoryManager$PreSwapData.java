package dev.loottech.api.utilities;

import dev.loottech.api.utilities.Timer;
import net.minecraft.class_1799;

public static class InventoryManager.PreSwapData {
    private final class_1799[] preHotbar;
    private final int starting;
    private final int swapTo;
    private Timer clearTime;

    public InventoryManager.PreSwapData(class_1799[] preHotbar, int start, int swapTo) {
        this.preHotbar = preHotbar;
        this.starting = start;
        this.swapTo = swapTo;
    }

    public void beginClear() {
        this.clearTime = new Timer();
        this.clearTime.reset();
    }

    public boolean isPassedClearTime() {
        return this.clearTime != null && this.clearTime.passedMs(300L);
    }

    public class_1799 getPreHolding(int i) {
        return this.preHotbar[i];
    }

    public int getStarting() {
        return this.starting;
    }

    public int getSlot() {
        return this.swapTo;
    }
}
