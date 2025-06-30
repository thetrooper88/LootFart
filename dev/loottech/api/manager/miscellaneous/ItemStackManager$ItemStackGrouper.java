package dev.loottech.api.manager.miscellaneous;

import dev.loottech.api.manager.miscellaneous.ItemStackManager;
import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

private static class ItemStackManager.ItemStackGrouper {
    public final class_1799 itemStack;
    public String itemString;

    public ItemStackManager.ItemStackGrouper(class_1799 itemStack) {
        this.itemStack = itemStack;
        this.setItemString();
    }

    private void setItemString() {
        String skullName;
        this.itemString = this.itemStack.method_7909().method_7876();
        if (this.itemStack.method_31574(class_1802.field_8575) && (skullName = ItemStackManager.getSkullName(this.itemStack)) != null) {
            this.itemString = this.itemString + skullName;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ItemStackManager.ItemStackGrouper otherGrouper = (ItemStackManager.ItemStackGrouper)obj;
        return this.itemString.equals((Object)otherGrouper.itemString) && this.itemStack.method_7909().equals((Object)otherGrouper.itemStack.method_7909());
    }

    public int hashCode() {
        return Objects.hash((Object[])new Object[]{this.itemString});
    }
}
