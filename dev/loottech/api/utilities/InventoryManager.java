package dev.loottech.api.utilities;

import com.google.common.collect.Lists;
import dev.loottech.api.manager.Managers;
import dev.loottech.api.manager.event.EventListener;
import dev.loottech.api.utilities.Timer;
import dev.loottech.api.utilities.Util;
import dev.loottech.asm.mixins.accessor.AccessorBundlePacket;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.events.ItemDesyncEvent;
import dev.loottech.client.events.PacketReceiveEvent;
import dev.loottech.client.events.PacketSendEvent;
import dev.loottech.client.events.TickEvent;
import dev.loottech.client.modules.client.AntiCheat;
import dev.loottech.client.modules.player.Replenish;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_2596;
import net.minecraft.class_2653;
import net.minecraft.class_2735;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2868;
import net.minecraft.class_8042;

public class InventoryManager
implements Util,
EventListener {
    private final List<PreSwapData> swapData = new CopyOnWriteArrayList();
    private int slot;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2868) {
            class_2868 packet = (class_2868)class_25962;
            int packetSlot = packet.method_12442();
            if (!class_1661.method_7380((int)packetSlot) || this.slot == packetSlot) {
                event.cancel();
                return;
            }
            this.slot = packetSlot;
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        ArrayList allowedBundle;
        class_2735 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2735) {
            packet = (class_2735)class_25962;
            this.slot = packet.method_11803();
        }
        if (Managers.MODULE.getInstance(Replenish.class).isInInventoryScreen() || !Managers.MODULE.getInstance(AntiCheat.class).grim.getValue()) {
            return;
        }
        class_25962 = event.getPacket();
        if (class_25962 instanceof class_8042) {
            packet = (class_8042)class_25962;
            allowedBundle = new ArrayList();
            for (class_2596 packet1 : packet.method_48324()) {
                if (packet1 instanceof class_2653) continue;
                allowedBundle.add((Object)packet1);
            }
            ((AccessorBundlePacket)packet).setIterable((Iterable<class_2596<?>>)allowedBundle);
        }
        if ((allowedBundle = event.getPacket()) instanceof class_2653) {
            packet = (class_2653)allowedBundle;
            int slot = packet.method_11450() - 36;
            if (slot < 0 || slot > 8) {
                return;
            }
            if (packet.method_11449().method_7960()) {
                return;
            }
            for (PreSwapData data : this.swapData) {
                class_1799 preStack;
                if (data.getSlot() != slot && data.getStarting() != slot || this.isEqual(preStack = data.getPreHolding(slot), packet.method_11449())) continue;
                event.cancel();
                break;
            }
        }
    }

    @Override
    public void onItemDesync(ItemDesyncEvent event) {
        if (this.isDesynced()) {
            event.cancel();
            event.setStack(this.getServerItem());
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
        if (event.getEntity() == InventoryManager.mc.field_1724) {
            this.syncToClient();
        }
    }

    @Override
    public void onTick(TickEvent event) {
        this.swapData.removeIf(PreSwapData::isPassedClearTime);
    }

    public void setSlot(int barSlot) {
        if (this.slot != barSlot && class_1661.method_7380((int)barSlot)) {
            this.setSlotForced(barSlot);
            class_1799[] hotbarCopy = new class_1799[9];
            for (int i = 0; i < 9; ++i) {
                hotbarCopy[i] = InventoryManager.mc.field_1724.method_31548().method_5438(i);
            }
            this.swapData.add((Object)new PreSwapData(hotbarCopy, this.slot, barSlot));
        }
    }

    public void setSlotAlt(int barSlot) {
        if (class_1661.method_7380((int)barSlot)) {
            InventoryManager.mc.field_1761.method_2906(InventoryManager.mc.field_1724.field_7498.field_7763, barSlot + 36, this.slot, class_1713.field_7791, (class_1657)InventoryManager.mc.field_1724);
        }
    }

    public void setSlotForced(int barSlot) {
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2868(barSlot));
    }

    public void syncToClient() {
        if (this.isDesynced()) {
            this.setSlotForced(InventoryManager.mc.field_1724.method_31548().field_7545);
            for (PreSwapData swapData : this.swapData) {
                swapData.beginClear();
            }
        }
    }

    public boolean isDesynced() {
        return InventoryManager.mc.field_1724.method_31548().field_7545 != this.slot;
    }

    public void closeScreen() {
        Managers.NETWORK.sendPacket((class_2596<?>)new class_2815(InventoryManager.mc.field_1724.field_7512.field_7763));
    }

    public int pickupSlot(int slot) {
        return this.click(slot, 0, class_1713.field_7790);
    }

    public void quickMove(int slot) {
        this.click(slot, 0, class_1713.field_7794);
    }

    public void throwSlot(int slot) {
        this.click(slot, 0, class_1713.field_7795);
    }

    public int findEmptySlot() {
        for (int i = 9; i < 36; ++i) {
            class_1799 stack = InventoryManager.mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) continue;
            return i;
        }
        return -999;
    }

    public int click(int slot, int button, class_1713 type) {
        if (slot < 0) {
            return -1;
        }
        class_1703 screenHandler = InventoryManager.mc.field_1724.field_7512;
        class_2371 defaultedList = screenHandler.field_7761;
        int i = defaultedList.size();
        ArrayList list = Lists.newArrayListWithCapacity((int)i);
        for (class_1735 slot1 : defaultedList) {
            list.add((Object)slot1.method_7677().method_7972());
        }
        screenHandler.method_7593(slot, button, type, (class_1657)InventoryManager.mc.field_1724);
        Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
        for (int j = 0; j < i; ++j) {
            class_1799 itemStack2;
            class_1799 itemStack = (class_1799)list.get(j);
            if (class_1799.method_7973((class_1799)itemStack, (class_1799)(itemStack2 = ((class_1735)defaultedList.get(j)).method_7677()))) continue;
            int2ObjectMap.put(j, (Object)itemStack2.method_7972());
        }
        InventoryManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2813(screenHandler.field_7763, screenHandler.method_37421(), slot, button, type, screenHandler.method_34255().method_7972(), (Int2ObjectMap)int2ObjectMap));
        return screenHandler.method_37421();
    }

    public int click2(int slot, int button, class_1713 type) {
        if (slot < 0) {
            return -1;
        }
        class_1703 screenHandler = InventoryManager.mc.field_1724.field_7512;
        class_2371 defaultedList = screenHandler.field_7761;
        int i = defaultedList.size();
        ArrayList list = Lists.newArrayListWithCapacity((int)i);
        for (class_1735 slot1 : defaultedList) {
            list.add((Object)slot1.method_7677().method_7972());
        }
        Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
        for (int j = 0; j < i; ++j) {
            class_1799 itemStack2;
            class_1799 itemStack = (class_1799)list.get(j);
            if (class_1799.method_7973((class_1799)itemStack, (class_1799)(itemStack2 = ((class_1735)defaultedList.get(j)).method_7677()))) continue;
            int2ObjectMap.put(j, (Object)itemStack2.method_7972());
        }
        InventoryManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2813(screenHandler.field_7763, screenHandler.method_37421(), slot, button, type, screenHandler.method_34255().method_7972(), (Int2ObjectMap)int2ObjectMap));
        return screenHandler.method_37421();
    }

    public int getServerSlot() {
        return this.slot;
    }

    public int getClientSlot() {
        return InventoryManager.mc.field_1724.method_31548().field_7545;
    }

    public void setClientSlot(int barSlot) {
        if (InventoryManager.mc.field_1724.method_31548().field_7545 != barSlot && class_1661.method_7380((int)barSlot)) {
            InventoryManager.mc.field_1724.method_31548().field_7545 = barSlot;
            this.setSlotForced(barSlot);
        }
    }

    public class_1799 getServerItem() {
        if (InventoryManager.mc.field_1724 != null && this.getServerSlot() != -1) {
            return InventoryManager.mc.field_1724.method_31548().method_5438(this.getServerSlot());
        }
        return null;
    }

    private boolean isEqual(class_1799 stack1, class_1799 stack2) {
        return stack1.method_7909().equals((Object)stack2.method_7909()) && stack1.method_7964().equals((Object)stack2.method_7964());
    }

    public static class PreSwapData {
        private final class_1799[] preHotbar;
        private final int starting;
        private final int swapTo;
        private Timer clearTime;

        public PreSwapData(class_1799[] preHotbar, int start, int swapTo) {
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
}
