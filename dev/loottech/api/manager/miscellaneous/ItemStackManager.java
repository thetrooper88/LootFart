package dev.loottech.api.manager.miscellaneous;

import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.minecraft.class_9296;
import net.minecraft.class_9334;

public class ItemStackManager {
    private static Map<ItemStackGrouper, Integer> groupItemStacks(Iterable<class_1799> itemIterable) {
        return (Map)StreamSupport.stream((Spliterator)itemIterable.spliterator(), (boolean)false).collect(Collectors.groupingBy(ItemStackGrouper::new, (Collector)Collectors.summingInt(class_1799::method_7947)));
    }

    public static class_1799 getDisplayStackFromIterable(Iterable<class_1799> itemIterable) {
        int itemThreshold = 1;
        return (class_1799)ItemStackManager.groupItemStacks(itemIterable).entrySet().stream().filter(entry -> (Integer)entry.getValue() >= itemThreshold).max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).map(grouper -> grouper.itemStack).orElse(null);
    }

    public static class_1799 getItemFromCustomName(class_1799 itemStack) {
        class_2561 customName = (class_2561)itemStack.method_57353().method_57829(class_9334.field_49631);
        if (customName == null) {
            return null;
        }
        class_2960 itemId = class_2960.method_12829((String)customName.getString());
        if (itemId == null) {
            return null;
        }
        class_1792 item = (class_1792)class_7923.field_41178.method_17966(itemId).orElse(null);
        if (item == null) {
            return null;
        }
        return new class_1799((class_1935)item);
    }

    private static String getSkullName(class_1799 itemStack) {
        class_9296 profileComponent = (class_9296)itemStack.method_57824(class_9334.field_49617);
        if (profileComponent == null) {
            return null;
        }
        return (String)profileComponent.comp_2410().orElse(null);
    }

    private static class ItemStackGrouper {
        public final class_1799 itemStack;
        public String itemString;

        public ItemStackGrouper(class_1799 itemStack) {
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
            ItemStackGrouper otherGrouper = (ItemStackGrouper)obj;
            return this.itemString.equals((Object)otherGrouper.itemString) && this.itemStack.method_7909().equals((Object)otherGrouper.itemStack.method_7909());
        }

        public int hashCode() {
            return Objects.hash((Object[])new Object[]{this.itemString});
        }
    }
}
