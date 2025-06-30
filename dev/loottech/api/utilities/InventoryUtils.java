package dev.loottech.api.utilities;

import com.google.common.collect.Lists;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.utilities.EnchantmentUtils;
import dev.loottech.api.utilities.FindItemResult;
import dev.loottech.api.utilities.IMinecraft;
import dev.loottech.asm.mixins.IClientPlayerInteractionManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1831;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2371;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2813;
import net.minecraft.class_2868;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_9334;

public class InventoryUtils
implements IMinecraft {
    private static final Set<class_2596<?>> PACKET_CACHE = new HashSet();
    private static int slot;
    private int serverSlot;
    public static int previousSlot;
    private static final Set<class_1792> SHULKERS;

    public static boolean isShulker(class_1792 item) {
        return SHULKERS.contains((Object)item);
    }

    public static boolean isHolding32k() {
        return InventoryUtils.isHolding32k(1000);
    }

    public static boolean isHolding32k(int lvl) {
        class_1799 mainhand = InventoryUtils.mc.field_1724.method_6047();
        return EnchantmentUtils.getLevel(mainhand, (class_5321<class_1887>)class_1893.field_9118) >= lvl;
    }

    public static int count(class_1792 item) {
        return InventoryUtils.mc.field_1724.method_31548().method_18861(item);
    }

    public static void switchSlot(int targetSlot, boolean silent) {
        Managers.PLAYER.setSwitching(true);
        InventoryUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2868(targetSlot));
        slot = targetSlot;
        InventoryUtils.syncToClient();
        if (!silent) {
            InventoryUtils.mc.field_1724.method_31548().field_7545 = targetSlot;
        }
        Managers.PLAYER.setSwitching(false);
    }

    public static boolean swap(int slot, boolean swapBack) {
        if (slot == 45) {
            return true;
        }
        if (slot < 0 || slot > 8) {
            return false;
        }
        if (swapBack && previousSlot == -1) {
            previousSlot = InventoryUtils.mc.field_1724.method_31548().field_7545;
        } else if (!swapBack) {
            previousSlot = -1;
        }
        InventoryUtils.mc.field_1724.method_31548().field_7545 = slot;
        ((IClientPlayerInteractionManager)InventoryUtils.mc.field_1761).nso$syncSelected();
        return true;
    }

    public static boolean swapBack() {
        if (previousSlot == -1) {
            return false;
        }
        boolean return_ = InventoryUtils.swap(previousSlot, false);
        previousSlot = -1;
        return return_;
    }

    public static void syncToClient() {
        if (InventoryUtils.isDesynced()) {
            InventoryUtils.setSlotForced(InventoryUtils.mc.field_1724.method_31548().field_7545);
        }
    }

    public static void setSlotForced(int barSlot) {
        class_2868 p = new class_2868(barSlot);
        if (mc.method_1562() != null) {
            PACKET_CACHE.add((Object)p);
            Managers.NETWORK.sendPacket((class_2596<?>)p);
        }
    }

    public static boolean isDesynced() {
        return InventoryUtils.mc.field_1724.method_31548().field_7545 != slot;
    }

    public static int findItem(class_1792 item, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; ++i) {
            class_1799 stack = InventoryUtils.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != item) continue;
            return i;
        }
        return -1;
    }

    public static FindItemResult find(class_1792 ... items) {
        return InventoryUtils.find((Predicate<class_1799>)((Predicate)itemStack -> {
            for (class_1792 item : items) {
                if (itemStack.method_7909() != item) continue;
                return true;
            }
            return false;
        }));
    }

    public static class_1799 get(int slot) {
        if (slot == -2) {
            return InventoryUtils.mc.field_1724.method_31548().method_5438(InventoryUtils.mc.field_1724.method_31548().field_7545);
        }
        return InventoryUtils.mc.field_1724.method_31548().method_5438(slot);
    }

    public static FindItemResult findEmpty() {
        return InventoryUtils.find((Predicate<class_1799>)((Predicate)class_1799::method_7960));
    }

    public static FindItemResult findInHotbar(class_1792 ... items) {
        return InventoryUtils.findInHotbar((Predicate<class_1799>)((Predicate)itemStack -> {
            for (class_1792 item : items) {
                if (itemStack.method_7909() != item) continue;
                return true;
            }
            return false;
        }));
    }

    public static FindItemResult findInHotbar(Predicate<class_1799> isGood) {
        if (InventoryUtils.testInOffHand(isGood)) {
            return new FindItemResult(45, InventoryUtils.mc.field_1724.method_6079().method_7947());
        }
        if (InventoryUtils.testInMainHand(isGood)) {
            return new FindItemResult(InventoryUtils.mc.field_1724.method_31548().field_7545, InventoryUtils.mc.field_1724.method_6047().method_7947());
        }
        return InventoryUtils.find(isGood, 0, 8);
    }

    public static FindItemResult find(Predicate<class_1799> isGood) {
        if (InventoryUtils.mc.field_1724 == null) {
            return new FindItemResult(0, 0);
        }
        return InventoryUtils.find(isGood, 0, InventoryUtils.mc.field_1724.method_31548().method_5439());
    }

    public static FindItemResult find(Predicate<class_1799> isGood, int start, int end) {
        if (InventoryUtils.mc.field_1724 == null) {
            return new FindItemResult(0, 0);
        }
        int slot = -1;
        int count = 0;
        for (int i = start; i <= end; ++i) {
            class_1799 stack = InventoryUtils.mc.field_1724.method_31548().method_5438(i);
            if (!isGood.test((Object)stack)) continue;
            if (slot == -1) {
                slot = i;
            }
            count += stack.method_7947();
        }
        return new FindItemResult(slot, count);
    }

    public static boolean testInMainHand(Predicate<class_1799> predicate) {
        return predicate.test((Object)InventoryUtils.mc.field_1724.method_6047());
    }

    public static boolean testInMainHand(class_1792 ... items) {
        return InventoryUtils.testInMainHand((Predicate<class_1799>)((Predicate)itemStack -> {
            for (class_1792 item : items) {
                if (!itemStack.method_31574(item)) continue;
                return true;
            }
            return false;
        }));
    }

    public static boolean testInOffHand(Predicate<class_1799> predicate) {
        return predicate.test((Object)InventoryUtils.mc.field_1724.method_6079());
    }

    public static boolean testInOffHand(class_1792 ... items) {
        return InventoryUtils.testInOffHand((Predicate<class_1799>)((Predicate)itemStack -> {
            for (class_1792 item : items) {
                if (!itemStack.method_31574(item)) continue;
                return true;
            }
            return false;
        }));
    }

    public static boolean testInHands(Predicate<class_1799> predicate) {
        return InventoryUtils.testInMainHand(predicate) || InventoryUtils.testInOffHand(predicate);
    }

    public static boolean testInHands(class_1792 ... items) {
        return InventoryUtils.testInMainHand(items) || InventoryUtils.testInOffHand(items);
    }

    public static boolean testInHotbar(Predicate<class_1799> predicate) {
        if (InventoryUtils.testInHands(predicate)) {
            return true;
        }
        for (int i = 0; i < 8; ++i) {
            class_1799 stack = InventoryUtils.mc.field_1724.method_31548().method_5438(i);
            if (!predicate.test((Object)stack)) continue;
            return true;
        }
        return false;
    }

    public static boolean testInHotbar(class_1792 ... items) {
        return InventoryUtils.testInHotbar((Predicate<class_1799>)((Predicate)itemStack -> {
            for (class_1792 item : items) {
                if (!itemStack.method_31574(item)) continue;
                return true;
            }
            return false;
        }));
    }

    public static void offhandItem(class_1792 item) {
        int slot = InventoryUtils.findItem(item, 0, 35);
        if (slot != -1) {
            Managers.PLAYER.setSwitching(true);
            InventoryUtils.mc.field_1761.method_2906(InventoryUtils.mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, (class_1657)InventoryUtils.mc.field_1724);
            InventoryUtils.mc.field_1761.method_2906(InventoryUtils.mc.field_1724.field_7512.field_7763, 45, 0, class_1713.field_7790, (class_1657)InventoryUtils.mc.field_1724);
            InventoryUtils.mc.field_1761.method_2906(InventoryUtils.mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, (class_1657)InventoryUtils.mc.field_1724);
            InventoryUtils.mc.field_1761.method_2927();
            Managers.PLAYER.setSwitching(false);
        }
    }

    public static boolean isFound(class_1792 item) {
        return InventoryUtils.findItem(item, 0, 35) != -1;
    }

    public static boolean isFoundInHotbar(class_1792 item) {
        return InventoryUtils.findItem(item, 0, 9) != -1;
    }

    public static void setSlot(int barSlot) {
        if (slot != barSlot && class_1661.method_7380((int)barSlot)) {
            InventoryUtils.setSlotForced(barSlot);
        }
    }

    private static void click(int slot, int button, class_1713 type) {
        class_1703 screenHandler = InventoryUtils.mc.field_1724.field_7512;
        class_2371 defaultedList = screenHandler.field_7761;
        int i = defaultedList.size();
        ArrayList list = Lists.newArrayListWithCapacity((int)i);
        for (class_1735 slot1 : defaultedList) {
            list.add((Object)slot1.method_7677().method_7972());
        }
        screenHandler.method_7593(slot, button, type, (class_1657)InventoryUtils.mc.field_1724);
        Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
        for (int j = 0; j < i; ++j) {
            class_1799 itemStack2;
            class_1799 itemStack = (class_1799)list.get(j);
            if (class_1799.method_7973((class_1799)itemStack, (class_1799)(itemStack2 = ((class_1735)defaultedList.get(j)).method_7677()))) continue;
            int2ObjectMap.put(j, (Object)itemStack2.method_7972());
        }
        InventoryUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2813(screenHandler.field_7763, screenHandler.method_37421(), slot, button, type, screenHandler.method_34255().method_7972(), (Int2ObjectMap)int2ObjectMap));
    }

    public static void pickupSlot(int slot) {
        Managers.INVENTORY.click(slot, 0, class_1713.field_7790);
    }

    public static int getBestTool(class_2680 state) {
        int slot = InventoryUtils.getBestToolNoFallback(state);
        return slot != -1 ? slot : InventoryUtils.mc.field_1724.method_31548().field_7545;
    }

    public static int getBestToolNoFallback(class_2680 state) {
        int slot = -1;
        float bestTool = 0.0f;
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = InventoryUtils.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960() || !(stack.method_7909() instanceof class_1831)) continue;
            float speed = stack.method_7924(state);
            int efficiency = class_1890.method_8225((class_6880)((class_6880)InventoryUtils.mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get()), (class_1799)stack);
            if (efficiency > 0) {
                speed += (float)(efficiency * efficiency) + 1.0f;
            }
            if (!(speed > bestTool)) continue;
            bestTool = speed;
            slot = i;
        }
        return slot;
    }

    public static int getEnchantmentLevel(class_1799 itemStack, class_5321<class_1887> enchantment) {
        if (itemStack.method_7960()) {
            return 0;
        }
        Object2IntArrayMap itemEnchantments = new Object2IntArrayMap();
        InventoryUtils.getEnchantments(itemStack, (Object2IntMap<class_6880<class_1887>>)itemEnchantments);
        return InventoryUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)itemEnchantments, enchantment);
    }

    public static int getEnchantmentLevel(Object2IntMap<class_6880<class_1887>> itemEnchantments, class_5321<class_1887> enchantment) {
        for (Object2IntMap.Entry entry : Object2IntMaps.fastIterable(itemEnchantments)) {
            if (!((class_6880)entry.getKey()).method_40225(enchantment)) continue;
            return entry.getIntValue();
        }
        return 0;
    }

    public static void getEnchantments(class_1799 itemStack, Object2IntMap<class_6880<class_1887>> enchantments) {
        enchantments.clear();
        if (itemStack.method_7960()) {
            return;
        }
        if (itemStack.method_7909() == class_1802.field_8598 && itemStack.method_57826(class_9334.field_49643)) {
            Set stored = ((class_9304)itemStack.method_57824(class_9334.field_49643)).method_57539();
            for (Object2IntMap.Entry entry : stored) {
                enchantments.put((Object)((class_6880)entry.getKey()), entry.getIntValue());
            }
        }
        if (itemStack.method_57826(class_9334.field_49633)) {
            Set regular = ((class_9304)itemStack.method_57824(class_9334.field_49633)).method_57539();
            for (Object2IntMap.Entry entry : regular) {
                enchantments.put((Object)((class_6880)entry.getKey()), entry.getIntValue());
            }
        }
    }

    static {
        previousSlot = -1;
        SHULKERS = Set.of((Object[])new class_1792[]{class_1802.field_8545, class_1802.field_8722, class_1802.field_8451, class_1802.field_8627, class_1802.field_8268, class_1802.field_8584, class_1802.field_8676, class_1802.field_8380, class_1802.field_8271, class_1802.field_8548, class_1802.field_8461, class_1802.field_8213, class_1802.field_8829, class_1802.field_8350, class_1802.field_8816, class_1802.field_8520, class_1802.field_8050});
    }

    public static enum SwitchModes {
        Normal,
        Silent,
        Strict;

    }
}
