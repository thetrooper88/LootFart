package dev.loottech.client.modules.player;

import dev.loottech.api.manager.module.Module;
import dev.loottech.api.manager.module.RegisterModule;
import dev.loottech.api.utilities.Timer;
import dev.loottech.client.events.DeathEvent;
import dev.loottech.client.values.impl.ValueBoolean;
import dev.loottech.client.values.impl.ValueNumber;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_476;
import net.minecraft.class_490;
import net.minecraft.class_495;
import net.minecraft.class_746;

@RegisterModule(name="Replenish", tag="Replenish", description="Automatically Replace items in your hotbar.", category=Module.Category.PLAYER)
public class Replenish
extends Module {
    ValueNumber percentConfig = new ValueNumber("Count", "Count", "The minimum percent of total stack before replenishing", (Number)Integer.valueOf((int)12), (Number)Integer.valueOf((int)1), (Number)Integer.valueOf((int)80));
    ValueBoolean resistantConfig = new ValueBoolean("AllowResistant", "AllowResistant", "Refills obsidian with other types of resistant blocks", false);
    private final Map<Integer, class_1799> hotbarCache = new ConcurrentHashMap();
    private final Timer lastDroppedTimer = new Timer();

    @Override
    public void onDisable() {
        this.hotbarCache.clear();
    }

    @Override
    public void onLogout() {
        this.hotbarCache.clear();
    }

    @Override
    public void onDeath(DeathEvent event) {
        if (event.getEntity() instanceof class_746) {
            this.hotbarCache.clear();
        }
    }

    @Override
    public void onTick() {
        class_1799 stack;
        int i;
        boolean pauseReplenish;
        if (Replenish.mc.field_1690.field_1869.method_1434()) {
            this.lastDroppedTimer.reset();
        }
        boolean bl = pauseReplenish = this.isInInventoryScreen() || !this.lastDroppedTimer.passedMs(100L);
        if (!pauseReplenish) {
            for (i = 0; i < 9; ++i) {
                double percentage;
                stack = Replenish.mc.field_1724.method_31548().method_5438(i);
                if (stack.method_7960()) {
                    class_1799 cachedStack = (class_1799)this.hotbarCache.getOrDefault((Object)i, null);
                    if (cachedStack == null || cachedStack.method_7960()) continue;
                    this.replenishStack(i, cachedStack);
                    break;
                }
                if (!stack.method_7946() || !((percentage = (double)stack.method_7947() / (double)stack.method_7914() * 100.0) < (double)this.percentConfig.getValue().intValue())) continue;
                this.replenishStack(i, stack);
                break;
            }
        }
        for (i = 0; i < 9; ++i) {
            stack = Replenish.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7960() && !pauseReplenish) continue;
            if (this.hotbarCache.containsKey((Object)i)) {
                this.hotbarCache.replace((Object)i, (Object)stack.method_7972());
                continue;
            }
            this.hotbarCache.put((Object)i, (Object)stack.method_7972());
        }
    }

    public boolean isInInventoryScreen() {
        return Replenish.mc.field_1755 instanceof class_476 || Replenish.mc.field_1755 instanceof class_495 || Replenish.mc.field_1755 instanceof class_490;
    }

    private void replenishStack(int slot, class_1799 stack) {
        int slot1 = -1;
        boolean outOfObsidian = stack.method_7909() == class_1802.field_8281 && Replenish.mc.field_1724.method_31548().method_18861(class_1802.field_8281) <= 1;
        for (int i = 9; i < 36; ++i) {
            class_1799 itemStack = Replenish.mc.field_1724.method_31548().method_5438(i);
            if (itemStack.method_7960() || !this.isSame(stack, itemStack, outOfObsidian) || !itemStack.method_7946()) continue;
            slot1 = i;
        }
        if (slot1 != -1) {
            Replenish.mc.field_1761.method_2906(0, slot1, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
            Replenish.mc.field_1761.method_2906(0, slot + 36, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
            if (!Replenish.mc.field_1724.field_7512.method_34255().method_7960()) {
                Replenish.mc.field_1761.method_2906(0, slot1, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
            }
        }
    }

    public boolean isSame(class_1799 stack1, class_1799 stack2, boolean outOfObsidian) {
        block5: {
            block6: {
                if (this.resistantConfig.getValue() && stack1.method_7909() == class_1802.field_8281 && outOfObsidian) {
                    return stack2.method_7909() == class_1802.field_8466 || stack2.method_7909() == class_1802.field_22421;
                }
                class_1792 class_17922 = stack1.method_7909();
                if (!(class_17922 instanceof class_1747)) break block5;
                class_1747 blockItem = (class_1747)class_17922;
                class_17922 = stack2.method_7909();
                if (!(class_17922 instanceof class_1747)) break block6;
                class_1747 blockItem1 = (class_1747)class_17922;
                if (blockItem.method_7711() == blockItem1.method_7711()) break block5;
            }
            return false;
        }
        if (!stack1.method_7964().getString().equals((Object)stack2.method_7964().getString())) {
            return false;
        }
        return stack1.method_7909().equals((Object)stack2.method_7909());
    }
}
